package io.vithor.yamvpframework.mvp;

/**
 * Created by Vithorio Polten on 5/16/15.
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
