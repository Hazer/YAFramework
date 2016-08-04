package io.vithor.yamvpframework.rest.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ApiClient(var baseUrl: String, val interceptor: Interceptor? = null, vararg var providedInterceptors: Interceptor?) {

    var apiAuthorizations: MutableMap<String, Interceptor>? = null
        get() = apiAuthorizations

    var okClient: OkHttpClient? = null
        private set

    lateinit var adapterBuilder: Retrofit.Builder

    init {
        if (!baseUrl.endsWith("/"))
            baseUrl += "/"
        apiAuthorizations = LinkedHashMap<String, Interceptor>()
        createDefaultAdapter(baseUrl)
    }

    fun createDefaultAdapter(baseUrl: String) {
        val gson = GsonBuilder()
                // .registerTypeAdapter(Date.class, new GsonDateMultiDeserializer())
                .setDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
                .create()

        val okBuilder = OkHttpClient.Builder()
                .addInterceptor {
                    val original = it.request()

                    val request = original.newBuilder()
                            .addHeader("Accept", "application/json")
                            .method(original.method(), original.body())
                            .build()

                    it.proceed(request)
                }

        if(interceptor != null) {
            okBuilder.addInterceptor(interceptor)
        }

//        okBuilder.addInterceptor(GzipInterceptor()) // TODO: Find another way to gzip, not working

        for (interceptor in providedInterceptors) {
            if (interceptor != null) {
                okBuilder.addInterceptor(interceptor)
            }
        }

//        if (BuildConfig.DEBUG) {
            okBuilder.addInterceptor(CurlInterceptor(Loggable { message ->
                Log.d("Ok2Curl", message);
            }))

        okBuilder.addNetworkInterceptor(CurlInterceptor())


//        okBuilder.addNetworkInterceptor(StethoInterceptor())
//        }

        okClient = okBuilder.build()

        adapterBuilder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addConverterFactory(GsonCustomConverterFactory.create(gson))
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return adapterBuilder.build().create(serviceClass)

    }
}

