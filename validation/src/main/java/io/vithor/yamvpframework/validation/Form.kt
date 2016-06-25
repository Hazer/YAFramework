package io.vithor.yamvpframework.validation

import android.widget.EditText
import org.jetbrains.anko.async

/**
 * Created by Vithorio Polten on 2/16/16.
 */

//internal fun validationForm(arg: () -> EditText) {
//
//}


fun <T : Rule<*>> EditText.rules(vararg rules: T): () -> ValidableField {
    return { ValidableField(this, rules) }
}


fun validationForm(vararg functions: () -> ValidableField): ValidationForm {
    return ValidationForm(functions.map {
        it.invoke()
    })
}

data class FailedRule(val rule: Rule<*>, val editText: EditText)

class ValidationForm(val map: List<ValidableField>) {

    fun validateAll(success: () -> Unit, failed: (invalidRules: List<FailedRule>) -> Unit) {
        var failedRules: List<FailedRule> = mutableListOf()

        for (field in map) {
            val editText = field.editText
            for (rule in field.rules) {
                if (!rule.isValid(editText)) {
                    failedRules += FailedRule(rule = rule, editText = editText)
                }
            }
        }
        if (failedRules.isNotEmpty()) {
            failed.invoke(failedRules)
        } else {
            success.invoke()
        }
    }

    fun validate(success: () -> Unit, failed: (invalidRules: List<FailedRule>) -> Unit) {
        var failedRules: List<FailedRule> = mutableListOf()

        for (field in map) {
            val editText = field.editText
            for (rule in field.rules) {
                if (!rule.isValid(editText)) {
                    failedRules += FailedRule(rule = rule, editText = editText)
                    break
                }
            }
        }
        if (failedRules.isNotEmpty()) {
            failed.invoke(failedRules)
        } else {
            success.invoke()
        }
    }

    fun validateAsync(success: () -> Unit, failed: (invalidRules: List<FailedRule>) -> Unit) {
        async() { validate(success, failed) }
    }

}

class ValidableField {

    val editText: EditText
    val rules: Array<out Rule<*>>

    constructor(editText: EditText, rules: Array<out Rule<*>>) {
        this.editText = editText
        this.rules = rules
    }

}