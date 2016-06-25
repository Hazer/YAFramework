package io.vithor.yamvpframework.rest.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Hazer on 3/30/16.
 */
open class AuthRequestInterceptor() : Interceptor {
    open var provider: AuthProvider? = null

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        val request = original.newBuilder().header("Accept", "application/json").method(original.method(), original.body())

        if (original.url().pathSegments()[2].equals("oauth", ignoreCase = true)) {
            request.header("Content-Type", "application/x-www-form-urlencoded")
        } else {
            request.header("Content-Type", "application/json")

            provider?.authorization?.let {
                request.header("Authorization", it)
            }
        }

        return chain.proceed(request.build())
    }
}

interface AuthProvider {
    val authorization: String?
}
