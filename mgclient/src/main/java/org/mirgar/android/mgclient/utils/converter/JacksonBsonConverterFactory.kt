package org.mirgar.android.mgclient.utils.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import de.undercouch.bson4jackson.BsonFactory
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

class JacksonBsonConverterFactory private constructor(private val mapper: ObjectMapper) : Converter.Factory() {
    companion object {
        fun create(): JacksonBsonConverterFactory {
            return create(ObjectMapper(BsonFactory()))
        }

        /** Create an instance using `mapper` for conversion.  */
        // Guarding public API nullability.
        fun create(mapper: ObjectMapper): JacksonBsonConverterFactory {
            return JacksonBsonConverterFactory(mapper)
        }
    }

    private fun typeOf(type: Type?) = mapper.typeFactory.constructType(type)

    override fun responseBodyConverter(
        type: Type?, annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *>? {
        return JacksonBsonResponseBodyConverter<Any?>(typeOf(type).let { mapper.readerFor(it) })
    }

    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation?>?,
        methodAnnotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody>? {
        return JacksonBsonRequestBodyConverter<Any>(typeOf(type).let { mapper.writerFor(it) })
    }
}

internal class JacksonBsonResponseBodyConverter<T>(private val adapter: ObjectReader) :
    Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        return value.use {
            adapter.readValue(it.byteStream())
        }
    }
}

internal class JacksonBsonRequestBodyConverter<T>(private val adapter: ObjectWriter) :
    Converter<T, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val bytes = adapter.writeValueAsBytes(value)
        return RequestBody.create(MEDIA_TYPE, bytes)
    }

    companion object {
        private val MEDIA_TYPE =
            MediaType.get("application/bson")
    }

}
