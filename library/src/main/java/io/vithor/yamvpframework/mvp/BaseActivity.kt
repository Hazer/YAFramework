package io.vithor.yamvpframework.mvp

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.afollestad.assent.Assent
import com.afollestad.assent.AssentCallback
import com.orhanobut.logger.Logger
import io.vithor.yamvpframework.PermissionDelegate
import io.vithor.yamvpframework.extensions.find
import io.vithor.yamvpframework.mvp.presenter.BasePresenter
import io.vithor.yamvpframework.mvp.presenter.Presentable
import io.vithor.yamvpframework.mvp.presenter.sketch.Sketch
import io.vithor.yamvpframework.validation.FailedRule
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<P : BasePresenter<SK>, SK : Sketch> : AppCompatActivity(), Presentable<P, SK>, Sketch /*, PermissionDelegate*/ {

    var presenter: P? = null
        private set

    protected abstract val layoutID: Int

    var savedInstanceStateCalled = false
        private set

    private var presenterClass: Class<P>? = null
    /**
     * Instantiate a presenter instance
     *
     * @return The [BasePresenter] for this view
     */
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
    //    protected abstract fun createPresenter(): P

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
        setContentView(layoutID)
        Assent.setActivity(this, this)

        this.presenter = createPresenter()

        this.presenter!!.beforeAttachView()
        //            ButterKnife.bind(this)

        onViewSetup(savedInstanceState)

        val sketchView = this as? SK ?: throw IllegalStateException("${javaClass.simpleName} must implement ${ (presenterClass?.genericSuperclass as? ParameterizedType)?.actualTypeArguments!![0]}")
        this.presenter!!.attachView(sketchView)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        //                Assent.setActivity(this, this)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Assent.setActivity(this, this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            Assent.setActivity(this, null)
        }
    }

    @CallSuper
    override fun onDestroy() {
        //        Assent.setActivity(this, null)
        presenter?.detachView()
        if (isFinishing) {
            presenter?.onDestroy()
            presenter = null
        }
        super.onDestroy()
    }

    @CallSuper
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Assent.handleResult(permissions, grantResults)
    }
//
//    override final fun askPermission(permission: String, granted: () -> Unit, notGranted: () -> Unit) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            granted.invoke()
//        } else {
//            if (Assent.isPermissionGranted(permission)) {
//                granted()
//            } else {
//                Assent.requestPermissions(AssentCallback {
//                    if (it.allPermissionsGranted()) {
//                        granted()
//                    } else {
//                        notGranted()
//                    }
//                }, 221, permission)
//            }
//        }
//
//    }
//
//    override final fun askPermissions(vararg permissions: String, granted: () -> Unit, notGranted: () -> Unit) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            granted.invoke()
//        } else {
//            val permissionsGranted = !permissions.any { !Assent.isPermissionGranted(it) }
//            if (permissionsGranted) {
//                granted()
//            } else {
//                Assent.requestPermissions({ result ->
//                    if (result?.allPermissionsGranted() == true) {
//                        granted()
//                    } else {
//                        notGranted()
//                    }
//                }, 220, permissions)
//            }
//        }
//    }

    protected fun showErrors(failedRules: List<FailedRule>) {
        failedRules.forEach {
            it.editText.error = it.rule.getMessage(this@BaseActivity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        savedInstanceStateCalled = true
        super.onSaveInstanceState(outState)
    }

    inline final fun <reified T : PresentableFragment<*, *>> findFragment(tag: String, java: Class<T>): T {
        return supportFragmentManager.find<T>(tag = tag) ?: java.newInstance()
    }
}