package io.vithor.yamvpframework.validation

import android.content.Context
import android.support.annotation.StringRes
import android.widget.EditText

/**
 * Created by Vithorio Polten on 2/16/16.
 */
class LengthRule(val min: Int = 0, val max: Int, message: String? = null, @StringRes messageId: Int? = null) : Rule<CharSequence>(0, message, messageId) {

    private val trim: Boolean = true

    override fun extractValue(field: EditText): CharSequence = field.text

    override fun getMessage(context: Context): String {
        if (messageId != null) {
            return context.getString(messageId)
        }
        return message ?: "Invalid length"
    }

    override fun isValid(validatable: CharSequence): Boolean {
        val ruleMin = min
        val ruleMax = max

        // Assert min is <= max
        assertMinMax(ruleMin, ruleMax)

        // Trim?
        val length = if (trim) validatable.trim().length else validatable.length

        // Check for min length
        var minIsValid = true
        if (ruleMin != Integer.MIN_VALUE) {
            // Min is set
            minIsValid = length >= ruleMin
        }

        // Check for max length
        var maxIsValid = true
        if (ruleMax != Integer.MAX_VALUE) {
            // Max is set
            maxIsValid = length <= ruleMax
        }

        return minIsValid && maxIsValid
    }

    private fun assertMinMax(min: Int, max: Int) {
        if (min > max) {
            val message = String.format(
                    "'min' (%d) should be less than or equal to 'max' (%d).", min, max)
            throw IllegalStateException(message)
        }
    }

}