@file:Suppress("UNUSED_PARAMETER", "unused")

package io.vithor.yamvpframework.ui

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.IntentCompat

/**
 * Created by Vithorio Polten on 1/4/16.
 */
object IntentHelper {

    fun getCameraIntent(values: ContentValues, imageUri: Uri, contentResolver: ContentResolver): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
    }

    val galleryIntent: Intent
        get() {
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            i.type = "image/*"
            return i
        }

    fun isIntentSafe(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        return activities.size > 0
    }

    fun newClearTask(context: Context, cls: Class<*>): Intent {
        return Intent(context, cls).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
