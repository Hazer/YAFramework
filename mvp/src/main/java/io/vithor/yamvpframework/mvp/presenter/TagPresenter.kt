package io.vithor.yamvpframework.mvp.presenter

import android.support.annotation.CallSuper
import io.vithor.yamvpframework.core.extensions.debugLog
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch

abstract class TagPresenter<SK : Sketch>(val tag: String) : BasePresenter<SK>() {
    init {
        debugLog("Presenter Generated")
        PresenterBucket.add(tag, this)
    }

    /**
     * Called in {onDestroy()} to remove this presenter from in-memory persistence.
     */
    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        debugLog("Releasing Presenter")
        PresenterBucket.release(tag)
    }
}