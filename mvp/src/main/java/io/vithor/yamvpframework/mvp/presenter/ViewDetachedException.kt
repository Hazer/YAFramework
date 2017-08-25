package io.vithor.yamvpframework.mvp.presenter

/**
 * Created by Vithorio Polten on 5/6/15.

 * When {BasePresenter.getView()} would return null. It throws this Exception.
 * Presenter stop processing and ignores the ViewDetachedException.

 */
class ViewDetachedException : RuntimeException() {}