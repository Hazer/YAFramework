package io.vithor.yamvpframework.ui.extensions

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by Vithorio Polten on 2/22/16.
 */

inline fun <reified T : Fragment> FragmentManager.find(tag: String): T? = findFragmentByTag(tag) as? T
inline fun <reified T : Fragment> FragmentManager.find(id: Int): T? = findFragmentById(id) as? T

val Fragment.safeContext: Context?
    get() {
        val ctx = context as? Activity
        return if (ctx?.isFinishing == false) ctx else null
    }