package io.vithor.yamvpframework.extensions

import android.content.Context

/**
 * Created by Vithorio Polten on 2/22/16.
 */

fun Context.getDimen(dimenRes: Int): Int {
    return resources.getDimensionPixelSize(dimenRes)
}

fun Context.getAttrId(themeRes: Int, attrRes: Int): Int {
    val a = theme.obtainStyledAttributes(themeRes, intArrayOf(attrRes));
    val attributeResourceId = a.getResourceId(0, 0);
    a.recycle()
    return attributeResourceId
}