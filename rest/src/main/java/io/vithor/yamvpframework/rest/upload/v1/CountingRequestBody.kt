package io.vithor.yamvpframework.rest.upload.v1

/**
 * Created by Hazer on 6/8/16.
 */

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * Decorates an OkHttp request body to count the number of bytes written when writing it. Can
 * decorate any request body, but is most useful for tracking the upload progress of large
 * multipart requests.

 * @author Leo Nikkilä
 */
open class CountingRequestBody(protected var delegate: RequestBody, var listener: Listener? = null) : RequestBody() {

    lateinit protected var countingSink: CountingSink

    override fun contentType(): MediaType {
        return delegate.contentType()
    }

    override fun contentLength(): Long {
        try {
            return delegate.contentLength()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return -1
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val bufferedSink: BufferedSink

        countingSink = CountingSink(sink)
        bufferedSink = Okio.buffer(countingSink)

        delegate.writeTo(bufferedSink)

        bufferedSink.flush()
    }

    protected inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {

        private var bytesWritten: Long = 0

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)

            bytesWritten += byteCount
            listener?.onRequestProgress(bytesWritten, contentLength())
        }
    }

    interface Listener {
        fun onRequestProgress(bytesWritten: Long, contentLength: Long)
    }
}