package io.vithor.yamvpframework.rest.api

import com.google.gson.annotations.SerializedName

data class OAuthToken(val scope: String?,
                      @SerializedName("refresh_token") val refreshToken: String?,
                      @SerializedName("access_token") val accessToken: String?,
                      @SerializedName("token_type") val tokenType: String?)