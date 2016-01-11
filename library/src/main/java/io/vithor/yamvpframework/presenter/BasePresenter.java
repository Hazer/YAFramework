package io.vithor.yamvpframework.presenter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

import io.vithor.yamvpframework.ErrorContainer;
import io.vithor.yamvpframework.RepositoryCallback;
import io.vithor.yamvpframework.ResponseContainer;
import io.vithor.yamvpframework.presenter.sketch.Sketch;

/**
 * Created by Hazer on 1/8/16.
 */
public abstract class BasePresenter<SK extends Sketch> implements Presenter  {

    private WeakReference<SK> viewWeak;
    private boolean shouldLoadData = true;
    protected final Object mLock = new Object();

    public BasePresenter() {
        Logger.d("Presenter Generated");
        PresenterBucket.add(this);
    }

    protected SK getView() throws ViewDetachedException {
        if (!isViewAttached()) {
            throw new ViewDetachedException();
        }
        return viewWeak.get();
    }

    protected abstract void onViewAttached();

    public Context getContext() {
        try {
            if (getView() instanceof Context) {
                return (Context) getView();

            } else if (getView() instanceof Fragment) {
                return ((Fragment) getView()).getActivity();

            }
        } catch (ViewDetachedException e) {
            Logger.e(e, "Context null");
        }
        return null;
    }

    @CallSuper
    public void attachView(SK view) {
        Logger.d("View Attached");
        this.viewWeak = new WeakReference<>(view);
        onViewAttached();
    }

    /**
     * Checks if a viewWeak is attached to this presenter. You should always call this method before
     * calling {@link #getView()} to get the viewWeak instance.
     */
    final protected boolean isViewAttached() {
        return viewWeak != null && viewWeak.get() != null;
    }


    protected boolean shouldLoadData() {
        return shouldLoadData;
    }

    protected void setShouldLoadData(boolean shouldLoadData) {
        this.shouldLoadData = shouldLoadData;
    }

    protected abstract void handleRestFailure(ErrorContainer error, PresenterAction action) throws ViewDetachedException;

    /**
     * Will be called if the viewWeak has been destroyed. Typically this method will be invoked from
     * <code>Activity.detachView()</code> or <code>Fragment.onDestroyView()</code>
     */
    @CallSuper
    public void detachView() {
        if (viewWeak != null) {
            viewWeak.clear();
            viewWeak = null;
        }
    }

    /**
     * Called in {onDestroy()} to remove this presenter from in-memory persistence.
     *
     */
    final public void release() {
        Logger.d("Releasing Presenter");
        PresenterBucket.release(this);
    }

    public static BasePresenter getActiveInstance(Class<? extends BasePresenter> type) {
        return PresenterBucket.getRetainedInstance(type);
    }

    protected abstract class PresenterCallback<T, RT> implements RepositoryCallback<T, RT, Exception> {
        private final PresenterAction action;

        public PresenterCallback(PresenterAction action) {
            this.action = action;
        }

        @Override
        final public void success(T t, ResponseContainer<RT> response) {
            setShouldLoadData(true);
            try {
                success(t, response, action);
            } catch (ViewDetachedException ignore){

            }
        }

        public abstract void success(T t, ResponseContainer response, PresenterAction action) throws ViewDetachedException;

        @Override
        final public void failure(ErrorContainer error) {
            setShouldLoadData(true);
            try {
                handleRestFailure(error, action);
            } catch (ViewDetachedException ignore) {

            }
        }
    }
}
