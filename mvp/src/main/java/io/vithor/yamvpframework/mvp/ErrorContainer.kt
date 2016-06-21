package io.vithor.yamvpframework.mvp

/**
 * Created by Vithorio Polten on 5/16/15.
 */
data class ErrorContainer<E : Throwable>(val error: E)
