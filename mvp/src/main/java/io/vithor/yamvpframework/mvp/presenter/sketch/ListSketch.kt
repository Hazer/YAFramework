package io.vithor.yamvpframework.mvp.presenter.sketch

import android.support.annotation.MainThread

/**
 * Created by Vithorio Polten on 5/5/15.
 */
@MainThread
interface ListSketch<T> : Sketch {
    fun onRefreshCompleted(t: T)

    fun onMoreDataFetched(t: T)

    fun showData(t: T)
}
