package io.vithor.yamvpframework.rest.upload.v1

import com.google.gson.annotations.SerializedName
import io.vithor.yamvpframework.core.extensions.unwrap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.File
import java.io.IOException

/**
 * Created by Hazer on 6/7/16.
 */
object UploadClient {
    fun uploadFile(retrofit: Retrofit, file: File, type: String, listener: CountingRequestBody.Listener?): PhotoUploaded {
        val (body, description) = prepareFile(file, "hello, this is description speaking", listener)

        // create upload service client
        val service = retrofit.create(PhotoAPI::class.java)
        // finally, execute the request

        val call = service.upload(type, description, body)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body()
            }
            return PhotoUploaded.No(errorBody = response.errorBody())
        } catch (e: IOException) {
            return PhotoUploaded.No(exception = e)
        }
    }

    private fun prepareFile(file: File, description: String, listener: CountingRequestBody.Listener?): Pair<MultipartBody.Part, RequestBody> {
        // create RequestBody instance from file
        val requestFile = CountingRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), file))

        listener.unwrap {
            requestFile.listener = it
        }

        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // add another part within the multipart request
        val descriptionBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), description)
        return Pair(body, descriptionBody)
    }

    sealed class PhotoUploaded {
        class Yes(@SerializedName("url") val url: String) : PhotoUploaded()
        class No(val errorBody: ResponseBody? = null, val exception: IOException? = null) : PhotoUploaded()
    }
}