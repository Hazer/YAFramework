package io.vithor.yamvpframework.core.extensions

import android.text.Editable

/**
 * Created by Vithorio Polten on 2/18/16.
 */

fun Editable.onlyNumbers(): String? = this.replace("[^\\d]".toRegex(), "")