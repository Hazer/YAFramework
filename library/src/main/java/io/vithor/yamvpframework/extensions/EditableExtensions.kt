package io.vithor.yamvpframework.extensions

import android.text.Editable

/**
 * Created by Hazer on 2/18/16.
 */

fun Editable.onlyNumbers(): String? = this.replace("[^\\d]".toRegex(), "")