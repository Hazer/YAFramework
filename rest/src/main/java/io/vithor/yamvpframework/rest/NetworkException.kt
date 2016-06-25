package io.vithor.yamvpframework.rest

/**
 * Created by Vithorio Polten on 4/20/15.
 */
class NetworkException : Exception {

    constructor() {
    }

    constructor(detailMessage: String) : super(detailMessage) {
    }

    constructor(detailMessage: String, throwable: Throwable) : super(detailMessage, throwable) {
    }

    constructor(throwable: Throwable) : super(throwable) {
    }

}
