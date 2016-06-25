package io.vithor.yamvpframework.core.preferences

/**
 * Created by Vithorio Polten on 2/15/16.
 */

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

fun SharedPreferences.Editor.erase() {
    clear()
    commit()
}

fun SharedPreferences.Editor.eraseAsync() {
    clear()
    apply()
}

fun Context.preferencesEditor (vararg pairs: Pair<String, Any?>): SharedPreferences.Editor {
    val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()

    for ((key, value) in pairs) {
        when (value) {
            is String -> editor.putString(key, value)
            is Set<*> -> {
                if (!value.all { it is String }) {
                    throw IllegalArgumentException("Only Set<String> is supported")
                }

                @Suppress("UNCHECKED_CAST")
                editor.putStringSet(key, value as Set<String>)
            }
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Any? -> editor.remove(key)
            else -> throw IllegalArgumentException("Unsupported value type: ${value?.javaClass}")
        }
    }

    if (pairs.size > 0) editor.commit()

    return editor
}

fun Context.preferencesEditorAsync (vararg pairs: Pair<String, Any>): SharedPreferences.Editor {
    val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()

    for ((key, value) in pairs) {
        when (value) {
            is String -> editor.putString(key, value)
            is Set<*> -> {
                if (!value.all { it is String }) {
                    throw IllegalArgumentException("Only Set<String> is supported")
                }

                @Suppress("UNCHECKED_CAST")
                editor.putStringSet(key, value as Set<String>)
            }
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Boolean -> editor.putBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass}")
        }
    }

    if (pairs.size > 0) editor.apply()

    return editor
}