/*package io.vithor.yamvpframework


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import icepick.Icepick

@FragmentWithArgs
class AlertDialogFragment:DialogFragment() {

    @Arg
    internal var mMessage:String? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState:Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    override fun onCreateDialog(savedInstanceState:Bundle):Dialog {
        return AlertDialog.Builder(getActivity()).setTitle(R.string.cannot_create_account).setMessage(mMessage).setPositiveButton("Ok", object:DialogInterface.OnClickListener() {
            override fun onClick(dialog:DialogInterface, which:Int) {
                dialog.dismiss()
            }
        }).create()
    }

    companion object {

        private val FRAG_TAG = "alert"

        fun showAlert(fragman:FragmentManager, message:String) {
            dismissAlert(fragman)
            val loading = AlertDialogFragmentBuilder(message).build()
            loading.show(fragman, FRAG_TAG)
        }

        fun getAlertDialog(fragmentManager:FragmentManager):AlertDialog? {
            val frag = fragmentManager.findFragmentByTag(FRAG_TAG) as AlertDialogFragment
            if (frag == null)
            {
                return null
            }
            return frag!!.getDialog() as AlertDialog
        }

        fun dismissAlert(fragman:FragmentManager) {
            val tr = fragman.beginTransaction()
            val frag = fragman.findFragmentByTag(FRAG_TAG)
            if (frag != null)
            {
                tr.remove(frag)
            }
            tr.commit()
        }
    }

}*/