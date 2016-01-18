package io.vithor.yamvpframework.presenter;

import android.util.SparseArray;

import com.orhanobut.logger.Logger;

/**
 * Created by hazer on 5/6/15.
 */
final class PresenterBucket {
    final static SparseArray<BasePresenter> bucket = new SparseArray<>(2);

    private PresenterBucket() {}

    /**
     * Add Presenter to Bucket. Now it will not be destroyed on orientation change.
     *
     * DON'T FORGET TO RELEASE IT LATER!!!
     *
     * @param presenter Presenter to persist in memory.
     */
    public static void add(BasePresenter presenter) {
//        Logger.d("Class: " + presenter.getClass().getSimpleName() + " - " + presenter.getClass().hashCode());
        bucket.put(presenter.getClass().hashCode(), presenter);
    }

    public static void release(BasePresenter presenter) {
        release(presenter.getClass());
    }

    public static void release(Class<? extends BasePresenter> presenterClass) {
//        Logger.d("Class: " + presenterClass.getSimpleName() + " - " + presenterClass.hashCode());
        bucket.remove(presenterClass.hashCode());
    }

    public static BasePresenter getRetainedInstance(Class<? extends BasePresenter> presenterClass) {
        return bucket.get(presenterClass.hashCode());
    }
}
