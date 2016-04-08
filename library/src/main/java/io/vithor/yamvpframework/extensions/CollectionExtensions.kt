package io.vithor.yamvpframework.extensions

/**
 * Created by Hazer on 4/8/16.
 */

fun <E> Collection<E>?.asMutableList(): MutableList<E>? {
    return this as? MutableList<E>
}