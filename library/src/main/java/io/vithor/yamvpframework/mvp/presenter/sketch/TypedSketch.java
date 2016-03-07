package io.vithor.yamvpframework.mvp.presenter.sketch;

import android.support.annotation.MainThread;

/**
 * Created by Vithorio Polten on 4/20/15.
 */
@MainThread
public interface TypedSketch<T> extends Sketch {
    void showData(T t);
}
