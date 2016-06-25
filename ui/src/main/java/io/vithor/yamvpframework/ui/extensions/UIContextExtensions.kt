package io.vithor.yamvpframework.ui.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.vithor.yamvpframework.core.extensions.unwrap

/**
 * Created by Hazer on 6/7/16.
 */

//fun Context.showAlert(message: String, onClose: (() -> Unit)? = null) {
//    alert {
//        title(R.string.app_name)
//        message(message)
//        cancellable(true)
//        neutralButton { onClose?.invoke() }
//    }.show()
//}

//fun <T: Activity> Context.dispatchTestNotification(clasz: Class<T>){
//    val intent = Intent(this, clasz)
//    //intent.putExtra("notificationId", notification.getNotificationId());
//    val randomId = Random().nextInt()
//    ​
//    val i = PendingIntent.getActivity(this, randomId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//    App.instance?.presentNotification(randomId, getString(R.string.app_name), "teste", this, i)
//}

fun Context.loadImage(image: String?, view: ImageView) {
    image.unwrap {
        Glide.with(this)
                .load(it)
                .crossFade()
                .into(view)
    }
}