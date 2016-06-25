package io.vithor.yamvpframework.core.extensions

import android.util.Log
import io.vithor.yamvpframework.core.YafConfig
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Hazer on 4/15/16.
 */

fun Any.debugLog(vararg messages: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        messages.map {
            formatMessage(it)
        }.reduce { left, right ->
            return@reduce left.toMutableList().apply { addAll(right) }
        }.caseSingle {
            Log.d(this.javaClass.simpleName, it, throwable)
        }.caseMany { index, message ->
            Log.d(formatTag(this.javaClass.simpleName, index), message, throwable)
        }
    }
}

fun Any.debugLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        formatMessage(message).caseSingle {
            Log.d(this.javaClass.simpleName, it, throwable)
        }.caseMany { index, message ->
            Log.d(formatTag(this.javaClass.simpleName, index), message, throwable)
        }
    }
}

fun Any.infoLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        formatMessage(message).caseSingle {
            Log.i(this.javaClass.simpleName, it, throwable)
        }.caseMany { index, message ->
            Log.i(formatTag(this.javaClass.simpleName, index), message, throwable)
        }
    }
}

fun Any.errorLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        formatMessage(message).caseSingle {
            Log.e(this.javaClass.simpleName, it, throwable)
        }.caseMany { index, message ->
            Log.e(formatTag(this.javaClass.simpleName, index), message, throwable)
        }
    }
}

fun Any.wtfLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        formatMessage(message).caseSingle {
            Log.wtf(this.javaClass.simpleName, it, throwable)
        }.caseMany { index, message ->
            Log.wtf(formatTag(this.javaClass.simpleName, index), message, throwable)
        }
    }
}

fun Any.wtfLog(throwable: Throwable? = null) {
    debugBuildOnly {
        Log.wtf(this.javaClass.simpleName, throwable)
    }
}

fun Any.wLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        formatMessage(message).caseSingle {
            Log.w(this.javaClass.simpleName, it, throwable)
        }.caseMany { index, message ->
            Log.w(formatTag(this.javaClass.simpleName, index), message, throwable)
        }
    }
}

fun Any.wLog(throwable: Throwable? = null) {
    debugBuildOnly {
        Log.w(this.javaClass.simpleName, throwable)
    }
}

fun Any.vLog(message: String?, throwable: Throwable? = null) {
    debugBuildOnly {
        formatMessage(message).caseSingle {
            Log.v(this.javaClass.simpleName, it, throwable)
        }.caseMany { index, message ->
            Log.v(formatTag(this.javaClass.simpleName, index), message, throwable)
        }
    }
}

fun Any.debugLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    debugLog(messageLambda.invoke(), throwable)
}

fun Any.infoLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    infoLog(messageLambda.invoke(), throwable)
}

fun Any.errorLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    errorLog(messageLambda.invoke(), throwable)
}

fun Any.wtfLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    wtfLog(messageLambda.invoke(), throwable)
}

fun Any.wLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    wLog(messageLambda.invoke(), throwable)
}

fun Any.vLog(throwable: Throwable? = null, messageLambda: () -> String?) {
    vLog(messageLambda.invoke(), throwable)
}

fun Any.debugJson(jsonLambda: () -> String?) {
    val json = jsonLambda.invoke()?.trim()
    if (json.isNullOrEmpty()) {
        debugLog("Empty/Null json content")
        return
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

private fun formatTag(tag: String, index: Int): String {
    return "$tag - PART [${index.toString().padStart(length = 3, padChar = '0')}]"
}

private fun formatMessage(message: String?): List<String> {
    if (message == null) return listOf("")
    var local = message!!
    val list = mutableListOf<String>()
    while (local.count() / 4000 > 1) {
        list += local.take(4000)
        local = local.substring(4001)
    }
    list += local
    return list
}

fun debugBuildOnly(lambda: () -> Unit) {
//    lambda.invoke()
    if (YafConfig.DEBUG) {
        lambda.invoke()
    }
}

