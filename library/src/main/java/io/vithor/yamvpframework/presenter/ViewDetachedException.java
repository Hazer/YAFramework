package io.vithor.yamvpframework.presenter;

/**
 * Created by hazer on 5/6/15.
 *
 * When {BasePresenter.getView()} would return null. It throws this Exception.
 * Presenter stop processing and ignores the ViewDetachedException.
 *
 */
public class ViewDetachedException extends Exception {
    public ViewDetachedException() {
        super();
    }
}