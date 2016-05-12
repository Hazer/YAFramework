package io.vithor.yamvpframework

//import butterknife.ButterKnife
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.halfbit.tinybus.TinyBus

/**
 * Created by Vithorio Polten on 12/22/15.
 */
abstract class BaseFragment : Fragment() {
    private val mBus: TinyBus by lazy { TinyBus.from(context.applicationContext) }

    var isFirstLaunch: Boolean = false
        private set

    protected abstract val layoutID: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        Assent.setFragment(this, this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        //        Icepick.saveInstanceState(this, outState)
        //        Akatsuki.save(this, outState);
        //        Icepick.saveInstanceState<BaseFragment>(this, outState)
    }

    //    }

    //    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    //        Assent.handleResult(permissions, grantResults)

    /**
     * Be careful, this method is sometimes called before onCreateView when inside ViewPager
     */
    @CallSuper
    open fun onFragmentVisible() {

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
        if (view == null) {
            this.isFirstLaunch = true
        } else {
            this.isFirstLaunch = false
        }
        //        Logger.d("First launch: ${isFirstLaunch}")
        if (isVisibleToUser) {
            onFragmentVisible()
        }
    }

    override fun onStart() {
        super.onStart()
        mBus.register(this)
    }

    override fun onStop() {
        mBus.unregister(this)
        super.onStop()
    }
//
//    final fun askPermission(permission: String, granted: () -> Unit, notGranted: () -> Unit) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            granted.invoke()
//        } else {
//            //            if (activity is PermissionDelegate) {
//            //                activity.askPermission(permission, granted, notGranted)
//            if (Assent.isPermissionGranted(permission)) {
//                granted()
//            } else {
//                Assent.requestPermissions(AssentCallback {
//                    if (it.allPermissionsGranted()) {
//                        granted()
//                    } else {
//                        notGranted()
//                    }
//                }, 233, permission)
//            }
//            //            } else {
//            //                Logger.e("Permission error")
//            //            }
//        }
//    }
//
//    final fun askPermissions(vararg permissions: String, granted: () -> Unit, notGranted: () -> Unit) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            granted.invoke()
//        } else {
//            //            if (activity is PermissionDelegate) {
//            //                activity.askPermissions(permissions, granted, notGranted)
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
//                }, 234, permissions)
//            }
//            //            } else {
//            //                Logger.e("Permission error")
//            //            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        //        Assent.setFragment(this, this)
    }

    override fun onPause() {
        super.onPause()
        //        if (activity != null && activity.isFinishing)
        //            Assent.setFragment(this, null)
    }

    @CallSuper
    open fun onBackPressedCallback() {

    }
}
