package io.vithor.yamvpframework.ui

import android.widget.EditText

class ArealessPhoneMasker(editText: EditText) : DigitsInputMasker(editText, InputMasks.BRAZIL_PHONE_NUMBER_MASK) {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val str = unmask(s.toString())
        val mask: String
        val defaultMask = getDefaultMask(str)
        when (str.length) {
            9 -> mask = InputMasks.BRAZIL_PHONE_NUMBER_9DIGITS_MASK
            else -> mask = defaultMask
        }

        var mascara = ""
        if (isUpdating) {
            mOldString = str
            isUpdating = false
            return
        }
        var i = 0
        for (m in mask.toCharArray()) {
            if (m != '#' && str.length > mOldString.length || m != '#' && str.length < mOldString.length && str.length != i) {
                mascara += m
                continue
            }

            try {
                mascara += str[i]
            } catch (e: Exception) {
                break
            }

            i++
        }
        isUpdating = true
        mEditText.setText(mascara)
        mEditText.setSelection(mascara.length)
    }

    companion object {

        fun unmask(s: String): String {
            return s.replace("[^0-9]*".toRegex(), "")
        }

        private fun getDefaultMask(str: String): String {
            var defaultMask = InputMasks.BRAZIL_PHONE_NUMBER_MASK
            if (str.length > 9) {
                defaultMask = InputMasks.BRAZIL_PHONE_NUMBER_9DIGITS_MASK
            }
            return defaultMask
        }
    }

}
