package io.vithor.yamvpframework.mvp.presenter

import android.content.Context
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import io.vithor.yamvpframework.core.extensions.debugLog
import io.vithor.yamvpframework.mvp.ErrorContainer
import io.vithor.yamvpframework.mvp.RepositoryCallback
import io.vithor.yamvpframework.mvp.ResponseContainer
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import kotlin.reflect.KClass

/**
 * Created by Vithorio Polten on 1/8/16.
 */
abstract class BasePresenter<SK : Sketch> : Presenter {
    companion object {
        fun <P : BasePresenter<SK>, SK : Sketch> getActiveInstance(type: KClass<P>?): P? {
            @Suppress("UNCHECKED_CAST")
            return PresenterBucket.Singletons.getRetainedInstance(type as KClass<out BasePresenter<out Sketch>>) as? P
        }

        fun <P : BasePresenter<SK>, SK : Sketch> getActiveInstance(tag: String): P? {
            @Suppress("UNCHECKED_CAST")
            return PresenterBucket.getRetainedInstance(tag) as? P
        }
    }

    protected val actionQueue by lazy { Executors.newSingleThreadScheduledExecutor() }

    private var viewWeak: WeakReference<SK>? = null

    var shouldLoadData = true
        protected set

    var parent: BasePresenter<*>? = null

    protected val view: SK?
        get() = viewWeak?.get()

    val context: Context?
        get() {
            return when (view) {
                is Context -> view as? Context
                is Fragment -> (view as? Fragment)?.activity
                else -> null
            }
        }

    /**
     * Checks if a viewWeak is attached to this presenter. You should always call this method before
     * calling [.getView] to get the viewWeak instance.
     */
    protected val isViewAttached: Boolean = viewWeak?.get() != null

    protected abstract fun onViewAttached()

    final fun attachView(view: SK) {
        this.viewWeak = WeakReference(view)
        debugLog("View Attached")
        onViewAttached()
    }

    @CallSuper
    open fun onRestFailure(error: ErrorContainer<Throwable>, action: PresenterAction) {

    }

    /**
     * Will be called if the viewWeak has been destroyed. Typically this method will be invoked from
     * `Activity.detachView()` or `Fragment.onDestroyView()`
     */
    @CallSuper
    fun detachView() {
        viewWeak?.clear()
        viewWeak = null
    }

    /**
     * Called in {onDestroy()} to remove this presenter from in-memory persistence.
     *
     */
    @CallSuper
    open fun onDestroy() {
        parent = null
    }

    open fun beforeAttachView() {

    }

    abstract class Callback<T, RT>(private val presenter: BasePresenter<*>, private val action: PresenterAction) : RepositoryCallback<T, RT, Throwable> {

        override fun success(model: T, response: ResponseContainer<RT>) {
            presenter.shouldLoadData = true
            success(model, response, action)
        }

        abstract fun success(model: T, response: ResponseContainer<RT>, action: PresenterAction)

        override fun failure(error: ErrorContainer<Throwable>) {
            presenter.shouldLoadData = true
            presenter.onRestFailure(error, action)
        }
    }

    abstract class ViewRunnable(view: Any) : java.lang.Runnable {
        protected val weakView: WeakReference<Any>

        init {
            this.weakView = WeakReference(view)
        }

        override fun run() {
            if (weakView.get() == null) throw ViewDetachedException()
        }
    }

    class ViewRunnableWrapper(view: Any, val runnable: Runnable) : ViewRunnable(view) {
        override fun run() {
            super.run()
            runnable.run()
        }
    }
}

