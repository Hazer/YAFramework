package io.vithor.yamvpframework.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.view.View;

import java.lang.ref.WeakReference;

import io.vithor.yamvpframework.BaseFragment;
import io.vithor.yamvpframework.presenter.sketch.Sketch;

/**
 * Created by Hazer on 1/10/16.
 */
public abstract class PresentableFragment<P extends BasePresenter> extends BaseFragment implements Sketch {
    /**
     * The presenter for this view. Will be instantiated with {@link #createPresenter()}
     */
    private WeakReference<P> presenter;

    /**
     * Creates a new presenter instance. This method will be called after from
     * {@link #onViewCreated(View, Bundle)} if needed.
     */
    @MainThread
    protected abstract P createPresenter();

    @MainThread
    final protected P getPresenter() {
        return presenter.get();
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the presenter if needed
        if (presenter == null) {
            presenter = new WeakReference<>(createPresenter());

            if (presenter.get() == null) {
                throw new NullPointerException("Presenter is null! Do you return null in createPresenter()?");
            }
        }
        getPresenter().attachView(this);
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        getPresenter().detachView();
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().release();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
