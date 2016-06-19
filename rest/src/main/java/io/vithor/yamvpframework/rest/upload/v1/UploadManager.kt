package io.vithor.yamvpframework.rest.upload.v1

import io.vithor.yamvpframework.core.extensions.debugLog
import io.vithor.yamvpframework.core.onMainLooper
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.Executors

/**
 * Created by Hazer on 6/7/16.
 */
class UploadManager(private val restClient: Retrofit, val maxAttempts: Int = 2, var continueOnFailure: Boolean = true) {
    companion object {
        private val SINGLE_JOB_TAG = "SINGLE_JOB"
    }

    enum class State {
        Uploading, Stopped, Completed
    }

    data class JobListener(
            val success: (tag: String, url: String, file: File) -> Unit,
            val failure: (tag: String, file: File) -> Unit,
            val retry: ((tag: String) -> Unit)?,
            val progress: ((tag: String, bytesWritten: Long, contentLength: Long) -> Unit)?
    )

    private var state = State.Stopped

    private val pendingJobs = mutableMapOf<String, UploadJob>()
    private val completedJobs = mutableMapOf<String, UploadJob>()

    private val jobListeners = mutableMapOf<String, JobListener>()

    val shouldStartUploading: Boolean
            get() { return state != State.Uploading && pendingJobs.count() != 0 }

    val executor by lazy { Executors.newSingleThreadExecutor() }

    fun enqueue(tag: String?, fileToUpload: File,
                success: (tag: String, url: String, file: File) -> Unit,
                failure: (tag: String, file: File) -> Unit,
                retry: ((tag: String) -> Unit)? = null,
                progress: ((tag: String, bytesWritten: Long, contentLength: Long) -> Unit)? = null) {

        val finalTag = tag ?: SINGLE_JOB_TAG

        if (completedJobs.containsKey(finalTag)) {
            if (completedJobs[finalTag]?.fileToUpload?.absolutePath.equals(fileToUpload.absolutePath))
                throw IllegalArgumentException("File with same tag already completed")
        }
        if (pendingJobs.containsKey(finalTag)) {
            if (pendingJobs[finalTag]?.fileToUpload?.absolutePath.equals(fileToUpload.absolutePath))
                throw IllegalArgumentException("File with same tag already enqueued")
        }

        val listener = JobListener(success, failure, retry, progress)

        jobListeners.put(finalTag, listener)

        val job = UploadJob(this, finalTag, fileToUpload)
        pendingJobs.put(finalTag, job)
    }

    fun getJob(tag: String? = SINGLE_JOB_TAG): UploadJob? {
        return completedJobs[tag] ?: pendingJobs[tag] ?: null
    }

    fun getJobs(tags: List<String>): List<UploadJob> {
        return mutableListOf<UploadJob>().apply {
            tags.mapNotNullTo(this) { getJob(it) }
        }
    }

    fun startUploads() {
        if (shouldStartUploading) {
            executor.execute {
                state = State.Uploading
                do {
                    debugLog { "Sending batch" }
                } while (runPending() == State.Uploading)
                state = State.Completed
            }
        }
    }

    private fun runPending(): State {
        for ((tag, job) in pendingJobs) {
            debugLog { "Sending job ${job.toString()}" }
            when (job.execute()) {
                is UploadClient.PhotoUploaded.Yes -> {
                    pendingJobs.remove(tag)
                    completedJobs.put(tag, job)
                    job.onCompleted()
                }
                else -> {
                    if (job.canRetry) {
                        job.onRetry()
                        return State.Uploading
                    }
                    job.onFailed()
                    debugLog { "Continue jobs on failure: ${continueOnFailure}" }
                    if (continueOnFailure) {
                        return State.Stopped
                    }
                }
            }
            if (pendingJobs.size == 0) {
                return State.Completed
            }
        }
        return State.Completed
    }

    class UploadJob(val uploadManager: UploadManager, val tag: String, val fileToUpload: File) {
        var response: UploadClient.PhotoUploaded? = null

        private var retryAttempts = 0

        val canRetry: Boolean
            get() = retryAttempts < uploadManager.maxAttempts

        val isCompleted: Boolean
            get() = response is UploadClient.PhotoUploaded.Yes

        fun execute(): UploadClient.PhotoUploaded {
            val upload = UploadClient.uploadFile(uploadManager.restClient, fileToUpload, tag,
                    object : CountingRequestBody.Listener {
                        override fun onRequestProgress(bytesWritten: Long, contentLength: Long) {
                            val listener = uploadManager.jobListeners[tag]
                            onMainLooper {
                                listener?.progress?.invoke(tag, bytesWritten, contentLength)
                            }
                        }
                    })
            response = upload
            return upload
        }

        fun onCompleted() {
            debugLog { "Job Completed: ${toString()}" }
            val listener = uploadManager.jobListeners[tag]
            onMainLooper {
                listener?.success?.invoke(tag, (response as UploadClient.PhotoUploaded.Yes).url, fileToUpload)
            }
        }

        fun onFailed() {
            debugLog { "Job Failed: ${toString()}" }
            val listener = uploadManager.jobListeners[tag]
            onMainLooper {
                listener?.failure?.invoke(tag, fileToUpload)
            }
        }

        fun onRetry() {
            debugLog { "Trying to retry" }
            if (canRetry) {
                retryAttempts++

                debugLog { "Retrying Job: ${toString()}\nRetry Attempt: ${retryAttempts}" }

                val listener = uploadManager.jobListeners[tag]
                onMainLooper {
                    listener?.retry?.invoke(tag)
                }
            }
        }

        override fun toString(): String {
            return "Job[$tag] URL: ${fileToUpload.name}\nCompleted: $isCompleted"
        }
    }
}