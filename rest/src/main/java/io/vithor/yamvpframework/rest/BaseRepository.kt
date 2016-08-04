package io.vithor.yamvpframework.rest

/**
 * Created by Lucas M Paim on 13/06/16.
 */
import android.support.annotation.Keep
import io.vithor.yamvpframework.rest.api.ApiClient
import okhttp3.Interceptor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Keep
abstract class AbstractRepository<API>(val clazz: Class<API>) {
    abstract val authInterceptor: Interceptor?

    abstract val baseURL: String

    open protected val versionPathInterceptor: Interceptor? by lazy { Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder().method(original.method(), original.body())

        if (original.url().pathSegments()[2].equals("oauth", ignoreCase = true)) {
            request.url(original.url().newBuilder().removePathSegment(1).build())
        }

        chain.proceed(request.build())
    } }

    protected val client by lazy { createClient() }

    protected val service: API by lazy {
        client.createService(clazz)
    }

    private fun createClient(): ApiClient {
        val client = ApiClient(baseURL, authInterceptor, versionPathInterceptor)
        return client
    }

    @Keep
    class DefaultCallback<T>(val onSuccess: (T) -> Unit, val onError: (Error, ResponseBody?) -> Unit) : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            onError(Error.Factory.from(call.request(), t), null)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
//                Logger.d("Code: " + response.code())
//                Logger.d(response.message())
                onSuccess(response.body())
            } else {
                val responseBody = response.errorBody()
                onError(Error.Factory.from(call.request(), response), responseBody)
            }
        }
    }

//    class SessionAuthInterceptor : AuthRequestInterceptor() {
//        override var provider: AuthProvider? = null
//            get() {
//                return DelivererSession.active
//            }
//    }
}