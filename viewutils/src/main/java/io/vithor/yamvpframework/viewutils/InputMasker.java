package io.vithor.yamvpframework.viewutils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class InputMasker implements TextWatcher {

    private final String replaceRegex;
    protected boolean isUpdating;
    protected String mOldString = "";
    protected String mMask;
    protected final EditText mEditText;

    public InputMasker(EditText editText, @InputMasks.Mask String mask, String replaceRegex) {
        mEditText = editText;
        mEditText.addTextChangedListener(this);
        mMask = mask;
        this.replaceRegex = replaceRegex;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = s.toString().replaceAll(replaceRegex, "");
        StringBuilder mask = new StringBuilder();
        if (isUpdating) {
            mOldString = str;
            isUpdating = false;
            return;
        }
        int i = 0;
        for (char m : mMask.toCharArray()) {
            if (m != '#' && str.length() > mOldString.length()) {
                mask.append(m);
                continue;
            }
            try {
                mask.append(str.charAt(i));
            } catch (Exception e) {
                break;
            }
            i++;
        }
        isUpdating = true;
        mEditText.setText(mask.toString());
        mEditText.setSelection(mask.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
