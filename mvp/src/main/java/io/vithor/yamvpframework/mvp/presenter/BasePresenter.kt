package io.vithor.yamvpframework.mvp.presenter

import android.content.Context
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import io.vithor.yamvpframework.mvp.ErrorContainer
import io.vithor.yamvpframework.mvp.RepositoryCallback
import io.vithor.yamvpframework.mvp.ResponseContainer
import io.vithor.yamvpframework.core.debugLog
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

/**
 * Created by Vithorio Polten on 1/8/16.
 */
abstract class BasePresenter<SK : Sketch> : Presenter {
    private var viewWeak: WeakReference<SK>? = null
    internal var shouldLoadData = true

    var parent: BasePresenter<*>? = null

    protected val view: SK?
        get() = viewWeak?.get()

    val context: Context?
        get() {
            if (view is Context) {
                return view as? Context
            } else if (view is Fragment) {
                return (view as? Fragment)?.activity
            }
            return null
        }

    /**
     * Checks if a viewWeak is attached to this presenter. You should always call this method before
     * calling [.getView] to get the viewWeak instance.
     */
    protected val isViewAttached: Boolean = viewWeak?.get() != null

    protected abstract fun onViewAttached()

    @CallSuper
    fun attachView(view: SK) {
        debugLog("View Attached")
        this.viewWeak = WeakReference(view)
        onViewAttached()
    }

    @Throws(ViewDetachedException::class)
    abstract fun handleRestFailure(error: ErrorContainer<Throwable>, action: PresenterAction)

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

     */
    @CallSuper
    open fun onDestroy() {
        parent = null
    }

    open fun beforeAttachView() {
    }

    companion object {
        fun <P : BasePresenter<SK>, SK : Sketch> getActiveInstance(type: KClass<P>?): P? {
            return PresenterBucket.Singletons.getRetainedInstance(type as KClass<out BasePresenter<out Sketch>>) as? P
        }

        fun <P : BasePresenter<SK>, SK : Sketch> getActiveInstance(tag: String): P? {
            return PresenterBucket.getRetainedInstance(tag) as? P
        }
    }
}

abstract class PresenterCallback<T, RT>(private val presenter: BasePresenter<*>, private val action: PresenterAction) : RepositoryCallback<T, RT, Throwable> {

    override fun success(t: T, response: ResponseContainer<RT>) {
        presenter.shouldLoadData = true
//        try {
            success(t, response, action)
//        } catch (ignore: ViewDetachedException) {
//        }
    }

//    @Throws(ViewDetachedException::class)
    abstract fun success(t: T, response: ResponseContainer<RT>, action: PresenterAction)

    override fun failure(error: ErrorContainer<Throwable>) {
        presenter.shouldLoadData = true
        try {
            presenter.handleRestFailure(error, action)
        } catch (ignore: ViewDetachedException) {
        }
    }
}


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
