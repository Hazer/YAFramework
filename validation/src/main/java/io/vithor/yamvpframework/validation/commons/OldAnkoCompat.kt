package org.jetbrains.anko

import org.jetbrains.anko.doAsync

/**
 * Created by Hazer on 25/05/17.
 */

@Deprecated("Use doAsync(task) instead.", ReplaceWith("doAsync(task = task)", imports = "org.jetbrains.anko.doAsync"))
fun <T> T.async(task: AnkoAsyncContext<T>.() -> Unit)
        = doAsync(task = task)