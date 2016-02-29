package io.vithor.yamvpframework.validation

import android.content.Context
import android.support.annotation.StringRes
import android.widget.EditText

/**
 * Created by Hazer on 2/16/16.
 */
class EqualsRule(val field: EditText, message: String? = null, @StringRes messageId: Int? = null) : Rule<CharSequence>(0, message, messageId) {

    override fun extractValue(field: EditText): CharSequence = field.text.trim()

    override fun isValid(validatable: CharSequence): Boolean {
        return validatable.equals(field.text.trim())
    }

    override fun getMessage(context: Context): String {
        if (messageId != null) {
            return context.getString(messageId)
        }
        return message ?: "Fields not equal"
    }


}