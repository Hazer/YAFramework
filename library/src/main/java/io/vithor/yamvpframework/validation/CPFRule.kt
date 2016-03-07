package io.vithor.yamvpframework.validation

import android.content.Context
import android.widget.EditText

/**
 * Created by Vithorio Polten on 2/18/16.
 */
class CPFRule(message: String? = null, messageId: Int? = null) : Rule<CharSequence?>(0, message, messageId) {

    override fun extractValue(field: EditText): CharSequence? = field.text

    override fun getMessage(context: Context): String {
        if (messageId != null) {
            return context.getString(messageId)
        }
        return message ?: "Invalid CPF"
    }

    override fun isValid(validatable: CharSequence?): Boolean {
        var cpf: String? = null
        if (!validatable.isNullOrBlank()) {
            cpf = validatable!!.replace("\\.".toRegex(), "").replace("\\-".toRegex(), "")
        }
        return validateCPF(cpf)
    }

    private fun validateCPF(cpf: String?): Boolean {
        if (cpf == null || cpf.length != 11 || isDefaultCPF(cpf))
            return false

        try {
            java.lang.Long.parseLong(cpf)
        } catch (e: NumberFormatException) {
            // CPF não possui somente números
            return false
        }

        return calcDigVerif(cpf.substring(0, 9)) == cpf.substring(9, 11)
    }

    private fun isDefaultCPF(cpf: String): Boolean {
        return cpf == "11111111111" || cpf == "22222222222"
                || cpf == "33333333333"
                || cpf == "44444444444"
                || cpf == "55555555555"
                || cpf == "66666666666"
                || cpf == "77777777777"
                || cpf == "88888888888"
                || cpf == "99999999999"

    }

    private fun calcDigVerif(num: String): String {
        val primDig: Int?
        val segDig: Int?
        var soma = 0
        var peso = 10
        for (i in 0..num.length - 1)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--

        if ((soma % 11 == 0) or (soma % 11 == 1))
            primDig = 0
        else
            primDig = 11 - soma % 11

        soma = 0
        peso = 11
        for (i in 0..num.length - 1)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--

        soma += primDig * 2
        if ((soma % 11 == 0) or (soma % 11 == 1))
            segDig = 0
        else
            segDig = 11 - soma % 11

        return primDig.toString() + segDig.toString()
    }
}