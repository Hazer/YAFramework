package io.vithor.yamvpframework.viewutils

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import io.vithor.yamvpframework.extensions.unwrap
import org.jetbrains.anko.support.v4.withArguments

class AlertDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.cannot_create_account)
                .setMessage(arguments?.getCharSequence("message") ?: "")
                .setPositiveButton("Ok", { dialog, which ->
                    dialog.dismiss()
                }).create()
    }

    companion object {
        private val FRAG_TAG = "alert"

        fun showAlert(fragMan: FragmentManager, message: String) {
            dismissAlert(fragMan)
            val loading = AlertDialogFragment()
            loading.withArguments(
                    "message" to message
            )
            loading.show(fragMan, FRAG_TAG)
        }

        fun getAlertDialog(fragmentManager: FragmentManager): AlertDialog? {
            val frag = fragmentManager.findFragmentByTag(FRAG_TAG) as? AlertDialogFragment
            return frag?.dialog as? AlertDialog
        }

        fun dismissAlert(fragMan: FragmentManager) {
            val tr = fragMan.beginTransaction()
            val frag = fragMan.findFragmentByTag(FRAG_TAG)
            frag.unwrap {
                tr.remove(it)
            }
            tr.commitAllowingStateLoss()
        }
    }
}