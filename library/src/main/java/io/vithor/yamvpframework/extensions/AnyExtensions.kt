package io.vithor.yamvpframework.extensions

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Hazer on 4/15/16.
 */

inline fun Any.debugLog(vararg messages: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        Log.d(this.javaClass.simpleName, messages.joinToString(separator = "\n"), throwable)
    }
}

inline fun Any.debugLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        Log.d(this.javaClass.simpleName, message ?: "", throwable)
    }
}

inline fun Any.infoLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        Log.i(this.javaClass.simpleName, message ?: "", throwable)
    }
}

inline fun Any.errorLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        Log.e(this.javaClass.simpleName, message ?: "", throwable)
    }
}

inline fun Any.wtfLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        Log.wtf(this.javaClass.simpleName, message ?: "", throwable)
    }
}

inline fun Any.wtfLog(throwable: Throwable? = null) {
    debugBuildOnly {
        Log.w(this.javaClass.simpleName, throwable)
    }
}

inline fun Any.wLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        Log.w(this.javaClass.simpleName, message ?: "", throwable)
    }
}

inline fun Any.wLog(throwable: Throwable? = null) {
    debugBuildOnly {
        Log.w(this.javaClass.simpleName, throwable)
    }
}

fun Any.vLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        Log.v(this.javaClass.simpleName, message ?: "", throwable)
    }
}

inline fun Any.debugLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    debugLog(messageLambda.invoke(), throwable)
}

inline fun Any.infoLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    infoLog(messageLambda.invoke(), throwable)
}

inline fun Any.errorLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    errorLog(messageLambda.invoke(), throwable)
}

inline fun Any.wtfLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    wtfLog(messageLambda.invoke(), throwable)
}

inline fun Any.wLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    wLog(messageLambda.invoke(), throwable)
}

inline fun Any.vLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    vLog(messageLambda.invoke(), throwable)
}

inline fun Any.debugJson(jsonLambda: () -> String?) {
    val json = jsonLambda.invoke()?.trim()
    if (json.isNullOrEmpty()) {
        debugLog("Empty/Null json content")
        return;
    }
    debugLog {
        try {
            if (json!!.startsWith("{")) {
                val jsonObject = JSONObject(json)
                return@debugLog jsonObject.toString(4)
            } else if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                return@debugLog jsonArray.toString(4)
            }
        } catch(e: JSONException) {
            errorLog { "${e.cause?.message}\n${json}" }
        }
        return@debugLog "invalid json:\n${json}"
    }
}

inline fun debugBuildOnly(lambda: () -> Unit) {
//    lambda.invoke()
    if (LogConfig.DEBUG) {
        lambda.invoke()
    }
}

/**
 * Workaround for BuildConfig.DEBUG always be False in Libraries.
 */
object LogConfig {
    var DEBUG = false
        private set

    fun init(debug: Boolean) {
        this.DEBUG = debug
    }
}

