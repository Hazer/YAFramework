package io.vithor.yamvpframework.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import io.vithor.yamvpframework.mvp.presenter.BasePresenter
import io.vithor.yamvpframework.mvp.presenter.Presentable
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import io.vithor.yamvpframework.ui.BaseActivity
import java.lang.reflect.ParameterizedType

/**
 * Created by Vithorio Polten on 6/24/16.
 */
abstract class PresentableActivity<P : BasePresenter<SK>, SK : Sketch> : BaseActivity(), Presentable<P, SK>, Sketch {
    var presenter: P? = null
        private set

    private val presenterClass: Class<P> by lazy {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        val name = type.toString().substring(6)
        @Suppress("UNCHECKED_CAST")
        return@lazy Class.forName(name) as? Class<P> ?: throw IllegalStateException("Class ${name} does not extends from BasePresenter or the Sketch implemented its not the same from BaseActivity.")
    }

    /**
     * Instantiate a presenter instance
     *
     * @return The [BasePresenter] for this view
     */
    override final fun createPresenter(): P {
        if (presenter != null) return presenter!!

        var inst = BasePresenter.getActiveInstance(presenterClass.kotlin)
        inst = inst ?: presenterClass.getConstructor(String::class.java)?.newInstance(presenterClass.kotlin.toString())
        return inst ?: throw IllegalStateException("Class ${presenterClass.simpleName} must have a public no-args constructor.")
    }

    override fun activePresenter(): P? {
        return presenter
    }

    /**
     * Called during onCreate step, after view and presenter created, but before presenter.attachView(this)
     * Be careful, this is not called after onCreate, this is called DURING onCreate.
     * @param [savedInstanceState]
     */
    protected abstract fun onViewSetup(savedInstanceState: Bundle?)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.presenter = createPresenter()

        this.presenter!!.beforeAttachView()
        //            ButterKnife.bind(this)

        onViewSetup(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val sketchView = this as? SK ?: throw IllegalStateException("${javaClass.simpleName} must implement ${ (presenterClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments!![0]}")
        this.presenter!!.attachView(sketchView)
    }

    @CallSuper
    override fun onDestroy() {
        presenter?.detachView()
        if (isFinishing) {
            presenter?.onDestroy()
            presenter = null
        }
        super.onDestroy()
    }
}