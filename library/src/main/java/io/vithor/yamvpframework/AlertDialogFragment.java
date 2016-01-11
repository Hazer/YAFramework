package io.vithor.yamvpframework;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import icepick.Icepick;

@FragmentWithArgs
public class AlertDialogFragment extends DialogFragment {

    private static final String FRAG_TAG = "alert";

    @Arg
    String mMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.cannot_create_account)
                .setMessage(mMessage)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }


    public static void showAlert(FragmentManager fragman, String message) {
        dismissAlert(fragman);
        AlertDialogFragment loading = new AlertDialogFragmentBuilder(message).build();
        loading.show(fragman, FRAG_TAG);
    }

    public static AlertDialog getAlertDialog(FragmentManager fragmentManager) {
        AlertDialogFragment frag = (AlertDialogFragment) fragmentManager.findFragmentByTag(FRAG_TAG);
        if (frag == null) {
            return null;
        }
        return (AlertDialog) frag.getDialog();
    }

    public static void dismissAlert(FragmentManager fragman) {
        FragmentTransaction tr = fragman.beginTransaction();
        Fragment frag = fragman.findFragmentByTag(FRAG_TAG);
        if(frag != null){
            tr.remove(frag);
        }
        tr.commit();
    }

}