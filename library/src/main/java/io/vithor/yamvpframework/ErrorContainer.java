package io.vithor.yamvpframework;

/**
 * Created by hazer on 5/16/15.
 */
public class ErrorContainer<E extends Throwable> {
    private final E error;

    public ErrorContainer(E error) {
        this.error = error;
    }

    public E getError() {
        return error;
    }
}
