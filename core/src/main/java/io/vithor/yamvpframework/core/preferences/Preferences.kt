package io.vithor.yamvpframework.core.preferences

/**
 * Created by Vithorio Polten on 2/15/16.
 */
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

private val defaultInit: Any.() -> Unit = {}

fun SharedPreferences.getString(key: String, defValue: String = ""): String = getString(key, defValue)
fun SharedPreferences.getStringSet(key: String, defValues: Set<String> = emptySet()): Set<String> = getStringSet(key, defValues)
fun SharedPreferences.getInt(key: String, defValue: Int = 0): Int = getInt(key, defValue)
fun SharedPreferences.getLong(key: String, defValue: Long = 0): Long = getLong(key, defValue)
fun SharedPreferences.getFloat(key: String, defValue: Float = 0.0f): Float = getFloat(key, defValue)
fun SharedPreferences.getBoolean(key: String, defValue: Boolean = false): Boolean = getBoolean(key, defValue)

fun Context.preferences(init: SharedPreferences.() -> Unit = defaultInit): SharedPreferences {
    val defaultPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    defaultPreferences.init()

    return defaultPreferences
}

fun SharedPreferences.onChanged(listener: (SharedPreferences?, key:String?) -> Unit): Unit
        = registerOnSharedPreferenceChangeListener(listener)