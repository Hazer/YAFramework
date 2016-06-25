package io.vithor.yamvpframework.ui

import android.widget.EditText

/**
 * Default input mask this will change the `#` char to an number

 * Fork from code by Leonardo Rossetto
 * https://github.com/leonardoxh/Masks/blob/ecde296ddf9816fb2a153b5a704913cb50aadd3f/library/library/src/main/java/com/github/leonardoxh/masks/InputMask.java
 */

open class DigitsInputMasker(editText: EditText, @InputMasks.Mask mask: String) : InputMasker(editText, mask, "[^\\d]")

