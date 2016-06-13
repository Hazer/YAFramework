package io.vithor.yamvpframework.rest.upload.v1

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Created by Hazer on 6/7/16.
 */
interface PhotoAPI {
    @Multipart
    @POST("/api/v1/photo/")
    fun upload(
            @Query("type") type: String,
            @Part("description") description: RequestBody,
            @Part file: MultipartBody.Part
    ): Call<UploadClient.PhotoUploaded.Yes>
}