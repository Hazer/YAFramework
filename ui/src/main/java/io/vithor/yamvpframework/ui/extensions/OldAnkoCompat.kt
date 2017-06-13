package org.jetbrains.anko

import android.app.Fragment
import android.content.Context
import org.jetbrains.anko.doAsync

/**
 * Created by Hazer on 25/05/17.
 */

@Deprecated("Use doAsync(task) instead.", ReplaceWith("doAsync(task = task)", imports = "org.jetbrains.anko.doAsync"))
fun <T> T.async(task: AnkoAsyncContext<T>.() -> Unit)
        = doAsync(task = task)


@Deprecated("Use uiThread(f) instead.", ReplaceWith("uiThread(f)", imports = "org.jetbrains.anko.uiThread"))
fun <T> AnkoAsyncContext<T>.onUiThread(f: (T) -> Unit): Boolean
    = uiThread(f)

@Deprecated("Use runOnUiThread(f) instead.", ReplaceWith("runOnUiThread(f)", imports = "org.jetbrains.anko.runOnUiThread"))
inline fun Fragment.onUiThread(crossinline f: () -> Unit) = runOnUiThread(f)

@Deprecated("Use runOnUiThread(f) instead.", ReplaceWith("runOnUiThread(f)", imports = "org.jetbrains.anko.runOnUiThread"))
fun Context.onUiThread(f: Context.() -> Unit) = runOnUiThread(f)