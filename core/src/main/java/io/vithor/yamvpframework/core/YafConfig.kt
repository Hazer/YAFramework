package io.vithor.yamvpframework.core

import android.app.Application

/**
 * Workaround for BuildConfig.DEBUG always be False in Libraries.
 *
 */
object YafConfig {
    var DEBUG: Boolean by YafDelegates.mustInitialize<Boolean>("YafConfig::init not called in your Application.\nPlease configure it before using this framework.")
        private set

    @JvmStatic fun init(app: Application, isDebuggable: Boolean) {
        this.DEBUG = isDebuggable
    }
}