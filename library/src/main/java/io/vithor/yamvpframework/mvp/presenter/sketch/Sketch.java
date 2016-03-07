package io.vithor.yamvpframework.mvp.presenter.sketch;

import android.support.annotation.MainThread;

import io.vithor.yamvpframework.mvp.presenter.PresenterAction;

/**
 * Created by Vithorio Polten on 1/8/16.
 */
@MainThread
public interface Sketch {
    /**
     * Display a loading view while loading data in background.
     * <b>The loading view must have the id = R.id.loadingView</b>
     *
     * @param action true, if pull-to-refresh has been invoked loading.
     */
    void showLoading(PresenterAction action);

    /**
     * Show the content view.
     *
     * <b>The content view must have the id = R.id.contentView</b>
     */
    void showContent();

    /**
     * Show the error view when a Network Error occurs.
     *
     * @param pullToRefresh true, if the exception was thrown during pull-to-refresh, otherwise
     * false.
     */
    void showNetworkError(boolean pullToRefresh);

    /**
     * Show the error view when a Server Error occurs.
     *
     * @param pullToRefresh true, if the exception was thrown during pull-to-refresh, otherwise
     * false.
     */
    void showServerError(boolean pullToRefresh);
}
