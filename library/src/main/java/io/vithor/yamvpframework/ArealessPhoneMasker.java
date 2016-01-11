package io.vithor.yamvpframework;

import android.widget.EditText;

public class ArealessPhoneMasker extends DigitsInputMasker {

    public ArealessPhoneMasker(EditText editText) {
        super(editText, InputMasks.BRAZIL_PHONE_NUMBER_MASK);
    }

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = unmask(s.toString());
        String mask;
        String defaultMask = getDefaultMask(str);
        switch (str.length()) {
            case 9:
                mask = InputMasks.BRAZIL_PHONE_NUMBER_9DIGITS_MASK;
                break;
            default:
                mask = defaultMask;
                break;
        }

        String mascara = "";
        if (isUpdating) {
            mOldString = str;
            isUpdating = false;
            return;
        }
        int i = 0;
        for (char m : mask.toCharArray()) {
            if ((m != '#' && str.length() > mOldString.length()) || (m != '#' && str.length() < mOldString.length() && str.length() != i)) {
                mascara += m;
                continue;
            }

            try {
                mascara += str.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        isUpdating = true;
        mEditText.setText(mascara);
        mEditText.setSelection(mascara.length());
    }

    private static String getDefaultMask(String str) {
        String defaultMask = InputMasks.BRAZIL_PHONE_NUMBER_MASK;
        if (str.length() > 9) {
            defaultMask = InputMasks.BRAZIL_PHONE_NUMBER_9DIGITS_MASK;
        }
        return defaultMask;
    }

}
