package io.vithor.yamvpframework.gson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * Created by Vithorio Polten on 2/19/16.
 */

fun JsonElement.asObjectOrNull(): JsonObject? {
    if (this.isJsonNull) return null
    return if (this.isJsonObject) this.asJsonObject else null
}

fun JsonElement.asArrayOrNull(): JsonArray? {
    if (this.isJsonNull) return null
    return if (this.isJsonArray) this.asJsonArray else null
}

fun JsonElement.asStringOrNull(): String? {
    if (this.isJsonNull) return null
    return if (this.isJsonPrimitive) this.asString else null
}

fun JsonElement.getString(key: String): String? {
    return this.asObjectOrNull()?.getString(key)
}

fun JsonObject.getString(key: String): String? {
    if (this.isJsonNull) return null
    return this.get(key).asStringOrNull()
}

fun JsonElement.forEach(function: (json: JsonElement) -> Unit) {
    this.asArrayOrNull()?.forEach(function)
}