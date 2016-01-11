package io.vithor.yamvpframework;

/**
 * Created by hazer on 5/16/15.
 */
public interface RepositoryCallback<T, R, E extends Throwable> {
    /**
     * Successful HTTP response.
     */
    void success(T t, ResponseContainer<R> response);

    /**
     * Unsuccessful HTTP response due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    void failure(ErrorContainer<E> error);
}
