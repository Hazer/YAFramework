package io.vithor.yamvpframework.mvp.presenter.sketch

import android.support.annotation.MainThread
import io.vithor.yamvpframework.mvp.presenter.Presenter

import io.vithor.yamvpframework.mvp.presenter.PresenterAction

/**
 * Created by Vithorio Polten on 1/8/16.
 */
@MainThread
interface Sketch : Presenter.Contract.View {
    /**
     * Display a loading view while loading data in background.
     * **The loading view must have the id = R.id.loadingView**

     * @param action true, if pull-to-refresh has been invoked loading.
     */
    fun showLoading(action: PresenterAction)

    /**
     * Show the content view.

     * **The content view must have the id = R.id.contentView**
     */
    fun showContent()

    /**
     * Show the error view when a Network Error occurs.

     * @param pullToRefresh true, if the exception was thrown during pull-to-refresh, otherwise
     * * false.
     */
    fun showNetworkError(pullToRefresh: Boolean)

    /**
     * Show the error view when a Server Error occurs.

     * @param pullToRefresh true, if the exception was thrown during pull-to-refresh, otherwise
     * * false.
     */
    fun showServerError(pullToRefresh: Boolean)
}
