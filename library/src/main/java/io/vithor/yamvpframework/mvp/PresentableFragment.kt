package io.vithor.yamvpframework.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import io.vithor.yamvpframework.BaseFragment
import io.vithor.yamvpframework.mvp.presenter.BasePresenter
import io.vithor.yamvpframework.mvp.presenter.Presentable
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import java.lang.reflect.ParameterizedType

/**
 * Created by Hazer on 3/1/16.
 */
abstract class PresentableFragment<P : BasePresenter<SK>, SK : Sketch> : BaseFragment(), Presentable<P, SK> {

    var presenter: P? = null
        private set

    private var presenterClass: Class<P>? = null

    override final fun createPresenter(): P {
        if (presenterClass == null) {
            val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
            val name = type.toString().substring(6)
            presenterClass = Class.forName(name) as? Class<P> ?: throw IllegalStateException("Class ${name} does not extends from BasePresenter or the Sketch implemented its not the same from BaseActivity.")
        }
        var inst = BasePresenter.getActiveInstance(presenterClass?.kotlin)
        inst = inst ?: presenterClass?.newInstance()
        return inst ?: throw IllegalStateException("Class ${presenterClass?.simpleName} must have a public no-args constructor.")
    }

    @CallSuper
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (presenter == null) {
            presenter = createPresenter()
        }
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