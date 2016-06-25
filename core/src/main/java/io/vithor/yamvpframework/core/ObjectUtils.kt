package io.vithor.yamvpframework.core

/**
 * Created by Vithorio Polten on 1/5/16.
 */

fun isNull(vararg objects: Any?): Boolean {
    return objects.any { it == null }
}