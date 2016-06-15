package io.vithor.yamvpframework.mvp;

/**
 * Created by Vithorio Polten on 5/16/15.
 */
public interface RepositoryCallback<T, R, E extends Throwable> {
    /**
     * Successful HTTP response.
     *
     * @param t Response Body Deserialized
     * @param response ResponseContainer
     */
    void success(T t, ResponseContainer<R> response);

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     *
     * @param error ErrorContainer
     */
    void failure(ErrorContainer<E> error);
}