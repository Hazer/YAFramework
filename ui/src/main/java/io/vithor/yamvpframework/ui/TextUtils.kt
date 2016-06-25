package io.vithor.yamvpframework

import android.text.Editable

/**
 * Created by Vithorio Polten on 1/5/16.
 */
object TextUtils {
    fun isEmpty(vararg strings: String?): Boolean {
        return strings.any { it.isNullOrEmpty() }
    }

    fun isBlank(vararg strings: String?): Boolean {
        return strings.any { it.isNullOrBlank() }
    }

    fun onlyNumbers(text: Editable): String {
        return text.toString().replace("[^\\d]".toRegex(), "")
    }

    fun clearText(text: Editable): String {
        return text.toString().replace("[^a-zA-Z0-9]".toRegex(), "")
    }
}
