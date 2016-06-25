package io.vithor.yamvpframework.ui.extensions

import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.IntentCompat

/**
 * Created by Hazer on 3/4/16.
 */
fun Intent.clearTaskCompat(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK)
    return addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

fun textIntent(message: String): Intent {
    return Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
}

fun Context.chooser(intent: Intent, @StringRes title: Int): Intent? {
    return Intent.createChooser(intent, resources.getText(title))
}

fun Fragment.chooser(intent: Intent, @StringRes title: Int): Intent? {
    return Intent.createChooser(intent, resources.getText(title))
}

fun Intent.start(context: Context?) {
    context?.startActivity(this)
}
