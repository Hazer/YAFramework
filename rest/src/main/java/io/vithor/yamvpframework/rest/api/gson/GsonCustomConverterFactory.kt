package io.vithor.yamvpframework.rest.api.gson

import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/**
 * Created by Vithorio Polten on 2/16/16.
 */
class GsonCustomConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    private val gsonConverterFactory: GsonConverterFactory

    init {
        this.gsonConverterFactory = GsonConverterFactory.create(gson)
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        if (type == String::class.java)
            return GsonResponseBodyConverterToString<Any>(gson, type)
        else
            return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
    }

    override fun stringConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, String> {
        return super.stringConverter(type, annotations, retrofit)
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody> {
        return gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    companion object {
        fun create(gson: Gson): GsonCustomConverterFactory {
            return GsonCustomConverterFactory(gson)
        }
    }

}
