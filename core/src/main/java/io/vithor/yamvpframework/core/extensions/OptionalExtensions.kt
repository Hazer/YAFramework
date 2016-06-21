package io.vithor.yamvpframework.core.extensions

/**
 * Created by Hazer on 3/10/16.
 */

/**
 * Execute action closure with {this} as argument when this is not null.
 * @param action invoke (T) -> Unit using {this} as argument if not null
 *
 * @return itself
 */
inline fun <reified T> T?.unwrap(action: (T) -> Unit): T? {
    if (this != null) {
        action.invoke(this)
    }
    return this
}

/**
 * Execute action closure when this is null.
 * @param action invoke if null.
 *
 * @return itself
 */
inline fun <reified T> T?.whenNull(action: () -> Unit): T? {
    if (this == null) {
        action.invoke()
    }
    return this
}
