package io.vithor.yamvpframework.presenter.sketch;

import android.support.annotation.MainThread;

/**
 * Created by hazer on 4/20/15.
 */
@MainThread
public interface TypedSketch<T> extends Sketch {
    void showData(T t);
}
