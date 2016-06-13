package com.guizion.mercuryo.api.client.retrofit2

object StringUtil {
    /**
     * Check if the given array contains the given value (with case-insensitive comparison).

     * @param array The array
     * *
     * @param value The value to search
     * *
     * @return true if the array contains the value
     */
    fun containsIgnoreCase(array: Array<String?>, value: String?): Boolean {
        for (str in array) {
            if (value == null && str == null) return true
            if (value != null && value.equals(str, ignoreCase = true)) return true
        }
        return false
    }

    /**
     * Join an array of strings with the given separator.
     *
     *
     * Note: This might be replaced by utility method from commons-lang or guava someday
     * if one of those libraries is added as dependency.
     *

     * @param array     The array of strings
     * *
     * @param separator The separator
     * *
     * @return the resulting string
     */
    fun join(array: Array<String>, separator: String): String {
        val len = array.size
        if (len == 0) return ""

        val out = StringBuilder()
        out.append(array[0])
        for (i in 1..len - 1) {
            out.append(separator).append(array[i])
        }
        return out.toString()
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    fun toIndentedString(o: Any?): String {
        if (o == null) return "null"
        return o.toString().replace("\n", "\n    ")
    }
}
