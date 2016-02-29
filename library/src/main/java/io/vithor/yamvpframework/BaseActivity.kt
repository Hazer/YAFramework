package io.vithor.yamvpframework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.afollestad.assent.Assent
import com.afollestad.assent.AssentCallback
import io.vithor.yamvpframework.presenter.BasePresenter
import io.vithor.yamvpframework.presenter.sketch.Sketch
import io.vithor.yamvpframework.validation.FailedRule
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

//inline fun <reified PR: BasePresenter<*>> createPresenter(): PR {
//    return BasePresenter.getActiveInstance(PR::class.java) ?: PR::class.java.newInstance()
//}

abstract class BaseActivity<P : BasePresenter<SK>, SK : Sketch> : AppCompatActivity(), Sketch, PermissionDelegate {

    var presenter: P? = null
        private set

    protected abstract val layoutID: Int

    protected open var useButterknife: Boolean = true

    protected val safeContext: Context?
        get() {
            return if (isFinishing == false) this else null
        }

    private var presenterClass: Class<P>? = null

    /**
     * Instantiate a presenter instance
     *
     * @return The [BasePresenter] for this view
     */
    fun createPresenter(): P {
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
        //        Akatsuki.restore(this, savedInstanceState);
        //        Icepick.restoreInstanceState(this, savedInstanceState);

        this.presenter = createPresenter()

        if (useButterknife) {
            ButterKnife.bind(this)
        }
        this.presenter!!.beforeAttachView()

        onViewSetup(savedInstanceState)

        this.presenter!!.attachView(this as SK)
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
        presenter?.detachView()
        if (isFinishing) {
            presenter?.onDestroy()
        }
        super.onDestroy()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //        Icepick.saveInstanceState(this, outState);
        //        Akatsuki.save(this, outState);
    }

    @CallSuper
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Assent.handleResult(permissions, grantResults)
    }

    override final fun askPermission(permission: String, granted: () -> Unit, notGranted: () -> Unit) {
        if (Assent.isPermissionGranted(permission)) {
            granted()
        } else {
            Assent.requestPermissions(AssentCallback {
                if (it.allPermissionsGranted()) {
                    granted()
                } else {
                    notGranted()
                }
            }, permission.hashCode(), permission)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    protected fun showErrors(failedRules: List<FailedRule>) {
        failedRules.forEach {
            it.editText.error = it.rule.getMessage(this@BaseActivity)
        }
    }
}

