package io.vithor.yamvpframework.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.afollestad.assent.Assent

abstract class BaseActivity : AppCompatActivity() /*, PermissionDelegate*/ {

    protected abstract val layoutID: Int

    var savedInstanceStateCalled = false
        private set

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutID)
        Assent.setActivity(this, this)
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
        super.onDestroy()
        //        Assent.setActivity(this, null)
    }

    @CallSuper
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Assent.handleResult(permissions, grantResults)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle?) {
        savedInstanceStateCalled = true
        super.onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        savedInstanceStateCalled = true
        super.onSaveInstanceState(outState, outPersistentState)
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceStateCalled = false
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        savedInstanceStateCalled = false
    }

    /*
    inline final fun <reified T : PresentableFragment<*, *>> findFragment(tag: String, java: Class<T>): T {
        return supportFragmentManager.find<T>(tag = tag) ?: java.newInstance()
    }*/
}