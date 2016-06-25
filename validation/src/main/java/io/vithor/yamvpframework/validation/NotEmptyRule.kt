package io.vithor.yamvpframework.validation

import android.content.Context
import android.widget.EditText

/**
 * Created by Vithorio Polten on 2/16/16.
 */
class NotEmptyRule(message: String? = null, messageId: Int? = null) : Rule<CharSequence?>(0, message = message, messageId = messageId) {

    private val trim: Boolean = true

    override fun extractValue(field: EditText): CharSequence? = field.text

    override fun getMessage(context: Context): String {
        if (messageId != null) {
            return context.getString(messageId)
        }
        return message ?: "Empty field"
    }

    override fun isValid(validatable: CharSequence?): Boolean {
        if (trim) {
            return !validatable.isNullOrBlank()
        } else {
            return !validatable.isNullOrEmpty()
        }
    }

}