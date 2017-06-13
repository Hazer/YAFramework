package io.vithor.yamvpframework.core

import android.view.ViewConfiguration

/**
 * Created by Vithorio Polten on 7/15/16.
 */
class NoRaceRunnable(val interval: Int = ViewConfiguration.getDoubleTapTimeout(), val run: () -> Unit) : Runnable {
    private var lastRun: Long = 0

    private val lastRunTimeout: Long
        get() {
            return System.currentTimeMillis() - lastRun
        }

    override fun run() {
        if (lastRunTimeout > interval) {
            lastRun = System.currentTimeMillis()
            run.invoke()
        }
    }
}


