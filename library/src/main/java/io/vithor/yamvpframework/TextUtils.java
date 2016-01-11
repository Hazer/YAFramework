package io.vithor.yamvpframework;

import android.text.Editable;

/**
 * Created by Hazer on 1/5/16.
 */
public class TextUtils {
    public static boolean isEmpty(String... strings) {
        for (String str : strings) {
            if (str.isEmpty())
            return true;
        }
        return false;
    }

    public static String onlyNumbers(Editable text) {
        return text.toString().replaceAll("[^\\d]", "");
    }

    public static String clearText(Editable text) {
        return text.toString().replaceAll("[^a-zA-Z0-9]", "");
    }
}
