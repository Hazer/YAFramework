package io.vithor.yamvpframework.core

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Vithorio Polten on 6/25/16.
 */
object YafDelegates {
    fun <T: Any> mustInitialize(message: String): ReadWriteProperty<Any?, T> = MustInitializeVar(message)
    fun <R, T> mutableLazy(initializer: () -> T): ReadWriteProperty<R, T> = MutableLazy(initializer)
}

private class MustInitializeVar<T: Any>(val message: String) : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException(message, IllegalStateException("Property ${property.name} should be initialized before get."))
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

private class MutableLazy<R, T>(initializer: () -> T) : ReadWriteProperty<R, T> {
    private var initializer: (() -> T)? = initializer

    @Volatile var instance: T? = null

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        if (initializer != null) {
            return initializer!!.invoke().let {
                instance = it
                initializer = null
                return@let it
            }
        }
        return instance!!
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        if (initializer != null) {
            initializer = null
        }
        instance = value
    }
}