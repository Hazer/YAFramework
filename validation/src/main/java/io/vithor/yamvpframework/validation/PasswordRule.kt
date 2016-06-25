package io.vithor.yamvpframework.validation

import android.content.Context
import android.widget.EditText

/**
 * Created by Vithorio Polten on 2/16/16.
 */
class PasswordRule(val min: Int = 6,
                   val scheme: Scheme = PasswordRule.Scheme.ANY,
                   message: String? = null,
                   messageId: Int? = null) : Rule<CharSequence>(0, message, messageId) {

    override fun extractValue(field: EditText): CharSequence = field.text

    override fun getMessage(context: Context): String {
        if (messageId != null) {
            return context.getString(messageId)
        }
        return message ?: "Invalid password"
    }

    override fun isValid(validatable: CharSequence): Boolean {
        val hasMinChars = validatable.length >= min
        val matchesScheme = validatable.matches(regexFor(scheme).toRegex())
        return hasMinChars && matchesScheme
    }

    enum class Scheme {
        ANY, ALPHA, ALPHA_MIXED_CASE,
        NUMERIC, ALPHA_NUMERIC, ALPHA_NUMERIC_MIXED_CASE,
        ALPHA_NUMERIC_SYMBOLS, ALPHA_NUMERIC_MIXED_CASE_SYMBOLS
    }

    /*
     * http://stackoverflow.com/questions/1559751/
     * regex-to-make-sure-that-the-string-contains-at-least-one-lower-case-char-upper
     */
    private fun regexFor(scheme: Scheme): String {
        return when (scheme) {
            PasswordRule.Scheme.ANY -> ".+"
            PasswordRule.Scheme.ALPHA -> "\\w+"
            PasswordRule.Scheme.ALPHA_MIXED_CASE -> "(?=.*[a-z])(?=.*[A-Z]).+"
            PasswordRule.Scheme.NUMERIC -> "\\d+"
            PasswordRule.Scheme.ALPHA_NUMERIC -> "(?=.*[a-zA-Z])(?=.*[\\d]).+"
            PasswordRule.Scheme.ALPHA_NUMERIC_MIXED_CASE -> "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).+"
            PasswordRule.Scheme.ALPHA_NUMERIC_SYMBOLS -> "(?=.*[a-zA-Z])(?=.*[\\d])(?=.*([^\\w])).+"
            PasswordRule.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS -> "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*([^\\w])).+"
        }
    }

    /*
     * http://stackoverflow.com/questions/1559751/
     * regex-to-make-sure-that-the-string-contains-at-least-one-lower-case-char-upper
     */


    //    private val SCHEME_PATTERNS = object : HashMap<Password.Scheme, String>() {
    //        init {
    //            put(Password.Scheme.ANY, ".+")
    //            put(Password.Scheme.ALPHA, "\\w+")
    //            put(Password.Scheme.ALPHA_MIXED_CASE, "(?=.*[a-z])(?=.*[A-Z]).+")
    //            put(Password.Scheme.NUMERIC, "\\d+")
    //            put(Password.Scheme.ALPHA_NUMERIC, "(?=.*[a-zA-Z])(?=.*[\\d]).+")
    //            put(Password.Scheme.ALPHA_NUMERIC_MIXED_CASE,
    //                    "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).+")
    //            put(Password.Scheme.ALPHA_NUMERIC_SYMBOLS,
    //                    "(?=.*[a-zA-Z])(?=.*[\\d])(?=.*([^\\w])).+")
    //            put(Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,
    //                    "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*([^\\w])).+")
    //        }
}