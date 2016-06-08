package io.vithor.yamvpframework.core

import android.os.Handler
import android.os.Looper

/**
 * Created by Hazer on 6/8/16.
 */

fun onMainLooper(function: () -> Unit) {
    if (Looper.getMainLooper().thread == Thread.currentThread()) function() else Handler(Looper.getMainLooper()).post(function)
}