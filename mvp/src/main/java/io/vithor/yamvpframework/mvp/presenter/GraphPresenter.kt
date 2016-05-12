package io.vithor.yamvpframework.mvp.presenter

import java.util.ArrayList

/**
 * Created by Vithorio Polten on 1/10/16.
 */
abstract class GraphPresenter(parent: GraphPresenter?) {
    private val childPresenters = ArrayList<GraphPresenter>(4)
    private val parent: GraphPresenter? = null

    fun addChildPresenter(presenter: GraphPresenter) {
        if (!childPresenters.contains(presenter)) {
            childPresenters.add(presenter)
        }
    }

    fun removeChildPresenter(presenter: GraphPresenter) {
        childPresenters.remove(presenter)
    }

    fun notifyChildPresenters() {
        for (presenter in childPresenters) {

        }
    }
}
