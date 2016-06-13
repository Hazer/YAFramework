package io.vithor.yamvpframework.core.extensions

/**
 * Created by Hazer on 3/10/16.
 */

fun <T> T?.unwrap(action: (T) -> Unit) {
    if (this != null) {
        action.invoke(this)
    }
}
