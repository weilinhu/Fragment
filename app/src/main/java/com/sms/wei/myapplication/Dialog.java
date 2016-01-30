package com.sms.wei.myapplication;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wei on 2016/1/30.
 */
public class Dialog extends DialogFragment {
    private String[] mEvaluteVals = new String[] { "GOOD", "BAD", "NORMAL" };
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("选择文本").setItems(mEvaluteVals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              setRult(mEvaluteVals[which]);
            }
        });

        return dialog.create();
    }

    private void setRult(String mEvaluteVal) {
        if (getTargetFragment()==null){
            return;
        }
        Intent i = new Intent();
        i.putExtra("123",mEvaluteVal);
        getTargetFragment().onActivityResult(1,2,i);
    }
}
