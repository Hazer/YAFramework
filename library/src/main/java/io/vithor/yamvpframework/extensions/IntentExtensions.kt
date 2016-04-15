package io.vithor.yamvpframework.extensions

import android.content.Intent
import android.support.v4.content.IntentCompat

/**
 * Created by Hazer on 3/4/16.
 */
inline fun Intent.clearTaskCompat(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK)
    return addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}