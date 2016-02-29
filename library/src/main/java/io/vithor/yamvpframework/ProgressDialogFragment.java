package io.vithor.yamvpframework;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import icepick.Icepick;
import okio.BufferedSink;

@FragmentWithArgs
public class ProgressDialogFragment extends DialogFragment {

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
        ProgressDialog dialog = new ProgressDialog(getActivity());

        dialog.setMessage(mMessage);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        return dialog;
    }

    public static void showLoadingProgress(FragmentManager fragman, String message) {
        dismissLoadingProgress(fragman);
        ProgressDialogFragment loading = new ProgressDialogFragmentBuilder(message).build();
        loading.show(fragman, "loading");
    }

    public static void showLoadingProgress(FragmentManager fragman) {
        showLoadingProgress(fragman, "Loading...");
    }

    public static ProgressDialog getLoadingProgressDialog(FragmentManager fragmentManager) {
        ProgressDialogFragment frag = (ProgressDialogFragment) fragmentManager.findFragmentByTag("loading");
        if (frag == null) {
            return null;
        }
        return (ProgressDialog) frag.getDialog();
    }

    public static void dismissLoadingProgress(FragmentManager fragman) {
        FragmentTransaction tr = fragman.beginTransaction();
        Fragment frag = fragman.findFragmentByTag("loading");
        if(frag != null){
            tr.remove(frag);
        }
        tr.commitAllowingStateLoss();
    }
}