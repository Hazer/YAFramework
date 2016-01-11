package io.vithor.yamvpframework;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.vithor.yamvpframework.presenter.BasePresenter;
import io.vithor.yamvpframework.presenter.sketch.Sketch;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements Sketch {

    private P presenter;

    protected abstract @LayoutRes int getLayoutID();

    protected abstract void onViewCreated(Bundle savedInstanceState);

    /**
     * Instantiate a presenter instance
     *
     * @return The {@link BasePresenter} for this view
     */
    protected abstract P createPresenter();

    @CallSuper
    @Override protected void onDestroy() {
        if (presenter != null) {
            presenter.detachView();

            if (isFinishing()) {
                presenter.release();
            }
        }
        super.onDestroy();

    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
//        Akatsuki.restore(this, savedInstanceState);
//        Icepick.restoreInstanceState(this, savedInstanceState);

        this.presenter = createPresenter();

        ButterKnife.bind(this);

        onViewCreated(savedInstanceState);

        this.presenter.attachView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this, outState);
//        Akatsuki.save(this, outState);
    }
}

