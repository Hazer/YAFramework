package io.vithor.yamvpframework.ui.extensions

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import org.jetbrains.anko.intentFor

/**
 * Created by Vithorio Polten on 2/22/16.
 */
inline fun <reified T : Activity> Activity.navigate(id: String, sharedView: View? = null,
                                                    transitionName: String? = null) {
    var options: ActivityOptionsCompat? = null

    if (sharedView != null && transitionName != null) {
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedView, transitionName)
    }

    ActivityCompat.startActivity(this, intentFor<T>("id" to id), options?.toBundle())
}

fun Activity.getNavigationId(): String {
    val intent = intent
    return intent.getStringExtra("id")
}

val Activity.safeContext: Context?
    get() {
        return if (isFinishing == false) this else null
    }
