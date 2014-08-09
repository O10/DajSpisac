package com.example.olek.firsttest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by olek on 08.08.14.
 */
public class ChooseClassDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=new Dialog(getActivity(),R.style.cust_dialog);
        dialog.setContentView(R.layout.dialog_choose_class_layout);
        dialog.setTitle("Wybierz klasę");
        return dialog;

    }
}
