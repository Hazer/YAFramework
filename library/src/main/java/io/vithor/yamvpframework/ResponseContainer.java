package io.vithor.yamvpframework;

/**
 * Created by Vithorio Polten on 5/16/15.
 */
public class ResponseContainer<R> {
    private final R response;

    public ResponseContainer(R response) {
        this.response = response;
    }

    public R getResponse() {
        return response;
    }
}
