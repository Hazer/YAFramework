package io.vithor.yamvpframework;

import android.widget.EditText;

/**
 * Default input mask this will change the <code>#</code> char to an number
 * <p/>
 * Fork from code by Leonardo Rossetto
 * https://github.com/leonardoxh/Masks/blob/ecde296ddf9816fb2a153b5a704913cb50aadd3f/library/library/src/main/java/com/github/leonardoxh/masks/InputMask.java
 */

public class DigitsInputMasker extends InputMasker {

    public DigitsInputMasker(EditText editText, @InputMasks.Mask String mask) {
        super(editText, mask, "[^\\d]");
    }
}

