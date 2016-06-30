package io.vithor.yamvpframework.ui

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v13.app.FragmentCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.assent.Assent
import de.halfbit.tinybus.TinyBus

/**
 * Created by Vithorio Polten on 12/22/15.
 */
abstract class BaseFragment : Fragment(), FragmentCompat.OnRequestPermissionsResultCallback {
    private val mBus: TinyBus by lazy { TinyBus.from(context.applicationContext) }

    var isFirstLaunch: Boolean = false
        private set

    val isFinishing: Boolean
        get() {
            return activity?.isFinishing ?: true
        }

    private var savedInstanceStateCalled = false
        private set

    protected abstract val layoutID: Int

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Assent.setFragment(this, this)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle?) {
        savedInstanceStateCalled = true
        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Assent.handleResult(permissions, grantResults)
    }

    /**
     * Be careful, this method is sometimes called before onCreateView when inside ViewPager
     */
    @CallSuper
    open fun onFragmentVisibleInPager() {

    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(layoutID, container, false)
        //        ButterKnife.bind(this, rootView)
        return rootView
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        //        ButterKnife.unbind(this)
    }

    @CallSuper
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isFirstLaunch = view == null

        if (isVisibleToUser) {
            onFragmentVisibleInPager()
        }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        mBus.register(this)
    }

    @CallSuper
    override fun onStop() {
        mBus.unregister(this)
        super.onStop()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Assent.setFragment(this, this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()

        if (isFinishing) {
            Assent.setFragment(this, null)
        }
    }

    @CallSuper
    /**
     * @return True case action handled by fragment and false case not.
     */
    open fun onBackPressedCallback(): Boolean {
        return false
    }
}
