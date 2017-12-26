package com.assignment.bbvaassignment.utils;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.assignment.bbvaassignment.listeners.DialogMangerCallback;
import com.assignment.bbvaassignment.ui.BaseActivity;



public class DialogManager {
    public static void showConfirmPopup(BaseActivity activity, final DialogMangerCallback callback, String title, String message, String posBtn, String negBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(posBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        callback.onOkClick();
                    }
                })
                .setNegativeButton(negBtn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                callback.onCancelClick();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
