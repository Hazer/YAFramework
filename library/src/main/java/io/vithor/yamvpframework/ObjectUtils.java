package io.vithor.yamvpframework;

/**
 * Created by Hazer on 1/5/16.
 */
public class ObjectUtils {
    public static boolean isNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null)
                return true;
        }
        return false;
    }
}
