package io.vithor.yamvpframework.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.io.Serializable

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

fun Context.navigateToActivity(kClass: Class<Activity>, hasHistory: Boolean = true, extra: Map<String, Serializable>? = null) {
    val intent = Intent(this, kClass)
    if (!hasHistory) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    if (extra != null) {
        for (key in extra.keys) {
            intent.putExtra(key, extra[key])
        }
    }

    startActivity(intent)
}