package io.vithor.yamvpframework.mvp

/**
 * Created by Vithorio Polten on 5/16/15.
 */
interface RepositoryCallback<T, R, E : Throwable> {
    /**
     * Successful HTTP response.

     * @param t Response Body Deserialized
     * *
     * @param response ResponseContainer
     */
    fun success(t: T, response: ResponseContainer<R>)

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.

     * @param error ErrorContainer
     */
    fun failure(error: ErrorContainer<E>)
}