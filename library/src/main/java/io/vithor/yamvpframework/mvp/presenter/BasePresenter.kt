package io.vithor.yamvpframework.mvp.presenter

import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import android.support.v4.app.Fragment

import com.orhanobut.logger.Logger

import java.lang.ref.WeakReference

import io.vithor.yamvpframework.ErrorContainer
import io.vithor.yamvpframework.RepositoryCallback
import io.vithor.yamvpframework.ResponseContainer
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import kotlin.reflect.KClass

/**
 * Created by Vithorio Polten on 1/8/16.
 */
abstract class BasePresenter<SK : Sketch> : Presenter {
    private var viewWeak: WeakReference<SK>? = null
    internal var shouldLoadData = true
    protected val mLock = Object()

    protected val view: SK?
        @Throws(ViewDetachedException::class)
        get() {
            if (!isViewAttached) {
//                throw ViewDetachedException()
                return null
            }
            return viewWeak?.get()
        }

    val context: Context?
        get() {
            try {
                if (view is Context) {
                    return view as Context

                } else if (view is Fragment) {
                    return (view as Fragment).activity

                }
            } catch (e: ViewDetachedException) {
                Logger.e(e, "Context null")
            }

            return null
        }

    /**
     * Checks if a viewWeak is attached to this presenter. You should always call this method before
     * calling [.getView] to get the viewWeak instance.
     */
    protected val isViewAttached: Boolean
        get() = viewWeak != null && viewWeak?.get() != null

    init {
        Logger.d("Presenter Generated")
        PresenterBucket.add(this)
    }

    protected abstract fun onViewAttached()

    @CallSuper
    fun attachView(view: SK) {
        Logger.d("View Attached")
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
        Logger.d("Releasing Presenter")
        PresenterBucket.release(this.javaClass.kotlin)
    }

    open fun beforeAttachView() { }

    companion object {
        fun <P : BasePresenter<SK>, SK : Sketch> getActiveInstance(type: KClass<P>?): P? {
            return PresenterBucket.getRetainedInstance(type as KClass<out BasePresenter<out Sketch>>) as? P
        }
    }
}

abstract class PresenterCallback<T, RT>(private val presenter: BasePresenter<*>, private val action: PresenterAction) : RepositoryCallback<T, RT, Throwable> {

    override fun success(t: T, response: ResponseContainer<RT>) {
        presenter.shouldLoadData = true
        try {
            success(t, response, action)
        } catch (ignore: ViewDetachedException) { }
    }

    @Throws(ViewDetachedException::class)
    abstract fun success(t: T, response: ResponseContainer<RT>, action: PresenterAction)

    override fun failure(error: ErrorContainer<Throwable>) {
        presenter.shouldLoadData = true
        try {
            presenter.handleRestFailure(error, action)
        } catch (ignore: ViewDetachedException) { }
    }
}
