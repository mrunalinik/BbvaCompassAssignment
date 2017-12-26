package com.assignment.bbvaassignment.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.assignment.bbvaassignment.R;

public class BaseActivity extends AppCompatActivity {


    protected void showDialogToOpenSetting(){
        String DENIED_TITLE;
        String NEVER_SHOW_DENIED_MESSAGE;

        DENIED_TITLE = getString(R.string.permission_denied);
        NEVER_SHOW_DENIED_MESSAGE = getString(R.string.denied_never_show_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DENIED_TITLE);
        builder.setMessage(NEVER_SHOW_DENIED_MESSAGE);
        builder.setPositiveButton(getString(R.string.open_settings), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                showInstalledAppDetails(BaseActivity.this, BaseActivity.this.getPackageName());
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        builder.create().show();
    }


    protected void showInstalledAppDetails(Context context, String packageName) {
        Intent intent2 = new Intent();
        intent2.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri2 = Uri.fromParts("package", packageName, null);
        intent2.setData(uri2);
        context.startActivity(intent2);
        setResult(RESULT_CANCELED);
        finish();
    }

}

