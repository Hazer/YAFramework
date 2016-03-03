package io.vithor.yamvpframework.mvp.presenter

import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch

interface Presentable<P : BasePresenter<SK>, SK : Sketch> {
    /**
     * Instantiate a presenter instance
     *
     * @return The [BasePresenter] for this view
     */
    fun createPresenter(): P
}