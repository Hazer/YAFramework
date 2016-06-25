package io.vithor.yamvpframework.mvp.presenter

import android.support.annotation.CallSuper
import io.vithor.yamvpframework.core.extensions.debugLog
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch

abstract class SingletonPresenter<SK : Sketch> : BasePresenter<SK>() {
    init {
        debugLog("Presenter Generated")
        PresenterBucket.Singletons.add(this)
    }

    /**
     * Called in {onDestroy()} to remove this presenter from in-memory persistence.
     */
    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        debugLog("Releasing Presenter")
        PresenterBucket.Singletons.release(this.javaClass.kotlin)
    }
}