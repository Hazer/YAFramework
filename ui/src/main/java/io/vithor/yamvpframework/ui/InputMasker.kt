package io.vithor.yamvpframework.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

open class InputMasker(protected val mEditText: EditText, @InputMasks.Mask protected var mMask: String, private val replaceRegex: String) : TextWatcher {
    protected var isUpdating: Boolean = false
    protected var mOldString = ""

    init {
        mEditText.addTextChangedListener(this)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val str = s.toString().replace(replaceRegex.toRegex(), "")
        val mask = StringBuilder()
        if (isUpdating) {
            mOldString = str
            isUpdating = false
            return
        }
        var i = 0
        for (m in mMask.toCharArray()) {
            if (m != '#' && str.length > mOldString.length) {
                mask.append(m)
                continue
            }
            try {
                mask.append(str[i])
            } catch (e: Exception) {
                break
            }

            i++
        }
        isUpdating = true
        mEditText.setText(mask.toString())
        mEditText.setSelection(mask.length)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable) {
    }

}
