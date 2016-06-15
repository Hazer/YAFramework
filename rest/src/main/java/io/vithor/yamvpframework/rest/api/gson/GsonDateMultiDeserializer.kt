package io.vithor.yamvpframework.rest.api.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vithorio Polten on 1/7/16.
 */
class GsonDateMultiDeserializer : JsonDeserializer<Date> {

    private val format1 = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
    private val format2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date? {
        try {
            val j = json.asJsonPrimitive.asString
            return parseDate(j)
        } catch (e: ParseException) {
            throw JsonParseException(e.message, e)
        }
    }

    @Throws(ParseException::class)
    private fun parseDate(dateString: String?): Date? {
        if (dateString != null && dateString.trim().length > 0) {
            try {
                return format1.parse(dateString)
            } catch (pe: ParseException) {
                return format2.parse(dateString)
            }

        } else {
            return null
        }
    }

}
