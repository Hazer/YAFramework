package io.vithor.yamvpframework

import android.app.Activity
import android.os.Build
import android.support.annotation.IntRange
import android.support.v4.app.Fragment
import com.afollestad.assent.Assent
import com.afollestad.assent.AssentCallback

/**
 * Created by Hazer on 4/19/16.
 */

val Activity.PERMISSION_REQUEST_CODE: Int get() { return 221 }

val Activity.MULTIPLE_PERMISSIONS_REQUEST_CODE: Int get() { return 220 }

val Fragment.PERMISSION_REQUEST_CODE: Int get() { return 223 }

val Fragment.MULTIPLE_PERMISSIONS_REQUEST_CODE: Int get() { return 222 }

fun Activity.askPermission(permission: String, granted: () -> Unit, notGranted: () -> Unit) {
    askPermissionImpl(permission, PERMISSION_REQUEST_CODE, granted, notGranted)
}

fun Fragment.askPermission(permission: String, granted: () -> Unit, notGranted: () -> Unit) {
    askPermissionImpl(permission, PERMISSION_REQUEST_CODE, granted, notGranted)
}

fun Activity.askPermissions(vararg permissions: String, granted: () -> Unit, notGranted: () -> Unit) {
    askPermissionsImpl(permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE, granted, notGranted)
}

fun Fragment.askPermissions(vararg permissions: String, granted: () -> Unit, notGranted: () -> Unit) {
    askPermissionsImpl(permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE, granted, notGranted)
}

private fun askPermissionImpl(permission: String, @IntRange(from = 0, to = 255) requestCode: Int, granted: () -> Unit, notGranted: () -> Unit) {
    if (requestCode !in 0..255) {
        throw IllegalArgumentException("RequestCode: ${requestCode} is not a valid requestCode, valid codes are from 0 to 255")
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        granted.invoke()
    } else {
        if (Assent.isPermissionGranted(permission)) {
            granted()
        } else {
            Assent.requestPermissions(AssentCallback {
                if (it.allPermissionsGranted()) {
                    granted()
                } else {
                    notGranted()
                }
            }, requestCode, permission)
        }
    }
}

private fun askPermissionsImpl(permissions: Array<out String>, @IntRange(from = 0, to = 255) requestCode: Int, granted: () -> Unit, notGranted: () -> Unit) {
    if (requestCode !in 0..255) {
        throw IllegalArgumentException("RequestCode: ${requestCode} is not a valid requestCode, valid codes are from 0 to 255")
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        granted.invoke()
    } else {
        val permissionsGranted = !permissions.any { !Assent.isPermissionGranted(it) }
        if (permissionsGranted) {
            granted()
        } else {
            Assent.requestPermissions({ result ->
                if (result?.allPermissionsGranted() == true) {
                    granted()
                } else {
                    notGranted()
                }
            }, requestCode, permissions)
        }
    }
}
