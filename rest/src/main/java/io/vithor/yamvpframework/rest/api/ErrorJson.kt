package io.vithor.yamvpframework.rest.api

import com.google.gson.annotations.SerializedName

/**
 * Created by Vithorio Polten on 1/7/16.
 */
class ErrorJson {


    @SerializedName("message")
    var message: String? = null
        internal set
}
