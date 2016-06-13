package io.vithor.yamvpframework.core.extensions

/**
 * Created by Hazer on 4/8/16.
 */

fun <E> Collection<E>?.asMutableList(): MutableList<E>? {
    return this as? MutableList<E>
}

fun <T> Array<T>.enumerated(): List<Pair<Int, T>> {
    return indices.zip(this)
}

inline fun <T> Collection<T>.caseMany(action: (Int, T) -> Unit): Collection<T> {
    if (count() > 1) {
        forEachIndexed { index, obj ->
            action.invoke(index, obj)
        }
    }
    return this
}

inline fun <T> Collection<T>.caseSingle(action: (T) -> Unit): Collection<T> {
    if (count() == 1) {
        action.invoke(first())
    }
    return this
}

inline fun <T> Iterable<T>.caseSingle(action: (T) -> Unit): Iterable<T> {
    if (count() == 1) {
        action.invoke(first())
    }
    return this
}


inline fun <T> Iterable<T>.caseMany(action: (Int, T) -> Unit): Iterable<T> {
    if (count() > 1) {
        forEachIndexed { index, obj ->
            action.invoke(index, obj)
        }
    }
    return this
}
