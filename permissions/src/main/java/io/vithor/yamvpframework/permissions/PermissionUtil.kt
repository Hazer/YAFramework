package io.vithor.yamvpframework.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.IntRange
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.afollestad.assent.Assent
import com.afollestad.assent.AssentCallback

/**
 * Created by Hazer on 4/19/16.
 */

val Activity.PERMISSION_REQUEST_CODE: Int get() { return 221 }

val Activity.MULTIPLE_PERMISSIONS_REQUEST_CODE: Int get() { return 220 }

val Fragment.PERMISSION_REQUEST_CODE: Int get() { return 223 }

val Fragment.MULTIPLE_PERMISSIONS_REQUEST_CODE: Int get() { return 222 }

fun Activity.askPermission(permission: String, granted: () -> Unit, notGranted: (() -> Unit)? = null) {
    askPermissionImpl(this, permission, PERMISSION_REQUEST_CODE, granted, notGranted)
}

fun Fragment.askPermission(permission: String, granted: () -> Unit, notGranted: (() -> Unit)? = null) {
    askPermissionImpl(context, permission, PERMISSION_REQUEST_CODE, granted, notGranted)
}

fun Activity.askPermissions(vararg permissions: String, mode: Modes = Modes.All, granted: () -> Unit, notGranted: (() -> Unit)? = null) {
    when (mode) {
        Modes.All -> askPermissionsImplAll(this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE, granted, notGranted)
        Modes.Any -> askPermissionsImplAny(this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE, granted, notGranted)
    }
}

fun Fragment.askPermissions(vararg permissions: String, mode: Modes = Modes.All, granted: () -> Unit, notGranted: (() -> Unit)? = null) {
    when (mode) {
        Modes.All -> askPermissionsImplAll(context, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE, granted, notGranted)
        Modes.Any -> askPermissionsImplAny(context, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE, granted, notGranted)
    }
}

private fun askPermissionImpl(context: Context, permission: String, @IntRange(from = 0, to = 255) requestCode: Int, granted: () -> Unit, notGranted: (() -> Unit)? = null) {
    if (requestCode !in 0..255) {
        throw IllegalArgumentException("RequestCode: ${requestCode} is not a valid requestCode, valid codes are from 0 to 255")
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        granted.invoke()
    } else {
        if (context.isPermissionGranted(permission)) {
            granted()
        } else {
            Assent.requestPermissions(AssentCallback {
                if (it.allPermissionsGranted()) {
                    granted()
                } else {
                    notGranted?.invoke()
                }
            }, requestCode, permission)
        }
    }
}

private fun askPermissionsImpl(permissions: Array<out String>, @IntRange(from = 0, to = 255) requestCode: Int, granted: () -> Unit, notGranted: (() -> Unit)? = null) {
    if (requestCode !in 0..255) {
        throw IllegalArgumentException("RequestCode: ${requestCode} is not a valid requestCode, valid codes are from 0 to 255")
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        granted.invoke()
    } else {
        val permissionsGranted = permissions.any { Assent.isPermissionGranted(it) }
        if (permissionsGranted) {
            granted()
        } else {
            Assent.requestPermissions({ result ->
                if (result?.allPermissionsGranted() == true) {
                    granted()
                } else {
                    notGranted?.invoke()
                }
            }, requestCode, permissions)
        }
    }
}

enum class Modes {
    All,
    Any,
//    Each
}

private fun askPermissionsImplEach(context: Context, permissions: Array<out String>, @IntRange(from = 0, to = 255) requestCode: Int, granted: ((String) -> Unit)?, notGranted: ((String) -> Unit)? = null) {
    if (requestCode !in 0..255) {
        throw IllegalArgumentException("RequestCode: ${requestCode} is not a valid requestCode, valid codes are from 0 to 255")
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        granted?.apply { permissions.forEach { invoke(it) } }
    } else {
        Assent.requestPermissions({ result ->
            if (result != null) {
                result.permissions.forEach { if (context.isPermissionGranted(it)) granted?.invoke(it) else notGranted?.invoke(it) }
            }
        }, requestCode, permissions)
    }
}

private fun askPermissionsImplAll(context: Context, permissions: Array<out String>, @IntRange(from = 0, to = 255) requestCode: Int, granted: (() -> Unit)?, notGranted: (() -> Unit)? = null) {
    if (requestCode !in 0..255) {
        throw IllegalArgumentException("RequestCode: ${requestCode} is not a valid requestCode, valid codes are from 0 to 255")
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        granted?.invoke()
    } else if (permissions.all { context.isPermissionGranted(it) }) {
        granted?.invoke()
    } else {
        Assent.requestPermissions({ result ->
            if (result != null) {
                if (result.allPermissionsGranted() == true) {
                    granted?.invoke()
                } else {
                    notGranted?.invoke()
                }
            }
        }, requestCode, permissions)
    }
}

private fun askPermissionsImplAny(context: Context, permissions: Array<out String>, @IntRange(from = 0, to = 255) requestCode: Int, granted: (() -> Unit)?, notGranted: (() -> Unit)? = null) {
    if (requestCode !in 0..255) {
        throw IllegalArgumentException("RequestCode: ${requestCode} is not a valid requestCode, valid codes are from 0 to 255")
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        granted?.invoke()
    } else if (permissions.any { context.isPermissionGranted(it) }) {
        granted?.invoke()
    } else {
        Assent.requestPermissions({ result ->
            if (result != null) {
                if (result.permissions.any { context.isPermissionGranted(it) }) {
                    granted?.invoke()
                } else {
                    notGranted?.invoke()
                }
            }
        }, requestCode, permissions)
    }
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
