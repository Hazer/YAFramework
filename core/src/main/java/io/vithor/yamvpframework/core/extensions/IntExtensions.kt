package io.vithor.yamvpframework.core.extensions

import android.content.res.Resources

/**
 * Created by Vithorio Polten on 2/29/16.
 */

fun Int.asDP(): Float = this / (Resources.getSystem().displayMetrics.density + 0.5f)
fun Int.asPX(): Int = this * (Resources.getSystem().displayMetrics.density + 0.5f).toInt()