package io.vithor.yamvpframework.ui


import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import io.vithor.yamvpframework.core.extensions.unwrap
import org.jetbrains.anko.support.v4.withArguments

class ProgressDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = ProgressDialog(activity)

        dialog.setMessage(arguments!!.getCharSequence("message", "Loading..."))
        dialog.isIndeterminate = true
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        return dialog
    }

    companion object {

        @JvmOverloads fun showLoadingProgress(fragman: FragmentManager, message: String = "Loading...") {
            dismissLoadingProgress(fragman)
            val loading = ProgressDialogFragment().withArguments(
                    "message" to message
            )
            loading.show(fragman, "loading")
        }

        fun getLoadingProgressDialog(fragmentManager: FragmentManager): ProgressDialog? {
            val frag = fragmentManager.findFragmentByTag("loading") as? ProgressDialogFragment
            return frag?.dialog as? ProgressDialog
        }

        fun dismissLoadingProgress(fragMan: FragmentManager) {
            if (!fragMan.isDestroyed) {
                val frag = fragMan.findFragmentByTag("loading")
                frag.unwrap {
                    fragMan.beginTransaction()
                            .remove(it)
                            .commitAllowingStateLoss()
                }
            }
        }
    }
}