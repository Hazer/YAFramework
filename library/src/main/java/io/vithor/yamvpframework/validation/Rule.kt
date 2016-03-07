package io.vithor.yamvpframework.validation

/**
 * Created by Vithorio Polten on 2/16/16.
 */


import android.content.Context
import android.support.annotation.StringRes
import android.widget.EditText

/**
 * This is a base interface for {@link com.mobsandgeeks.saripaar.AnnotationRule} and
 * {@link com.mobsandgeeks.saripaar.QuickRule}.
 *
 * @param <VALIDATABLE>  A data type for an {@link com.mobsandgeeks.saripaar.AnnotationRule} and
 *      a {@link android.view.View} for a {@link com.mobsandgeeks.saripaar.QuickRule}.
 *
 * @author Ragunath Jawahar {@literal <rj@mobsandgeeks.com>}
 * @since 1.0
 */
abstract class Rule<VALIDATABLE>(protected val sequence: Int, val message: String? = null, @StringRes val messageId: Int? = null) {

    abstract fun extractValue(field: EditText): VALIDATABLE


    fun isValid(field: EditText): Boolean = isValid(extractValue(field))

    /**
     * Checks if the rule is valid.

     * @param validatable  Element on which the validation is applied, could be a data type or a
     * *      [android.view.View].
     * *
     * *
     * @return true if valid, false otherwise.
     */
    abstract fun isValid(validatable: VALIDATABLE): Boolean

    /**
     * Returns a failure message associated with the rule.

     * @param context  Any [android.content.Context] instance, usually an
     * *      [android.app.Activity].
     * *
     * *
     * @return A failure message.
     */
    abstract fun getMessage(context: Context): String
}