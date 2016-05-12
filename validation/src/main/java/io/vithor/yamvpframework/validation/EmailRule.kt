package io.vithor.yamvpframework.validation

import android.content.Context
import android.widget.EditText
import io.vithor.yamvpframework.validation.commons.EmailValidator

/**
 * Created by Vithorio Polten on 2/16/16.
 */
class EmailRule : Rule<String>(0) {

    override fun extractValue(field: EditText): String = field.text.toString()

    private val allowLocal: Boolean = true

    override fun isValid(validatable: String): Boolean {
        return EmailValidator.getInstance(allowLocal).isValid(validatable);
    }

    override fun getMessage(context: Context): String {
        return "Email not valid..."
    }
}