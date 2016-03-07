package io.vithor.yamvpframework.mvp.presenter.sketch;

import android.support.annotation.MainThread;

/**
 * Created by Vithorio Polten on 5/5/15.
 */
@MainThread
public interface ListSketch<T> extends Sketch {
    void onRefreshCompleted(T t);

    void onMoreDataFetched(T t);

    void showData(T t);
}
