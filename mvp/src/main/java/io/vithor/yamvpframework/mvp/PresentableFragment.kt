package io.vithor.yamvpframework.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import io.vithor.yamvpframework.core.extensions.whenNull
import io.vithor.yamvpframework.mvp.presenter.BasePresenter
import io.vithor.yamvpframework.mvp.presenter.Presentable
import io.vithor.yamvpframework.mvp.presenter.TagPresenter
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import io.vithor.yamvpframework.ui.BaseFragment
import java.lang.reflect.ParameterizedType

/**
 * Created by Vithorio Polten on 3/1/16.
 */
abstract class PresentableFragment<P : TagPresenter<SK>, SK : Sketch> : BaseFragment(), Presentable<P, SK> {

    var presenter: P? = null
        private set

    private var presenterClass: Class<P>? = null
        get() {
            @Suppress("UNCHECKED_CAST")
            return field ?: Class.forName(presenterClassString) as Class<P>?
        }

    private val presenterClassString: String by lazy { createPresenterClassString() }

    private fun createPresenterClassString(): String {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type.toString().substring(6)
    }

    open protected val presenterTag: String by lazy { presenterClassString }

    override final fun createPresenter(): P {
        var inst: P? = BasePresenter.getActiveInstance<P, SK>(tag = presenterTag)
        inst = inst ?: presenterClass?.getConstructor(String::class.java)?.newInstance(presenterTag)
        return inst ?: throw IllegalStateException("Class ${presenterClassString} must have a public no-args constructor.")
    }

    override fun activePresenter(): P? {
        return presenter
    }

    override final fun createPresenter(presentable: Presentable<*, *>?): P {
        val presenter = super.createPresenter(presentable)
        presenter.parent = presentable?.activePresenter()
        return presenter
    }

    @CallSuper
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (presenter == null) {
            presenter = createPresenter()
        }
        @Suppress("UNCHECKED_CAST")
        val sketchView = this as? SK ?: throw IllegalStateException("${javaClass.simpleName} must implement ${ (presenterClass?.genericSuperclass as? ParameterizedType)?.actualTypeArguments!![0]}")
        presenter?.attachView(sketchView)
    }

    @CallSuper
    override fun onDestroyView() {
        presenter?.detachView()
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
    }
}