package com.assignment.bbvaassignment.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.assignment.bbvaassignment.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class Utility {

    private static final String SHARED_PREFERENCE = "assignment_preference";
    private static ProgressDialog dialog;

    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }


    public static String convertStreamToString(InputStream inputStream)
            throws IOException, Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }



    public static void showProgressDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage(context.getResources().getString(R.string.progress_pleaseWait));
        dialog.show();
    }

    public static void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static boolean checkPlayServices(Context context) {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED == result) {
            Utility.showToast(context, "Update google play services for better performance");
        } else if (ConnectionResult.SERVICE_MISSING == result) {
            Utility.showToast(context, "google play services missing install/update for better performance");
        } else if (ConnectionResult.SERVICE_DISABLED == result) {
            Utility.showToast(context, "google play services disabled enable for better performance");
        } else if (ConnectionResult.SERVICE_INVALID == result) {
            Utility.showToast(context, "google play services invalid install/update for better performance");
        }

    /*if (GooglePlayServicesUtil.isUserRecoverableError(result)) {

         * GooglePlayServicesUtil.getErrorDialog(resultCode, this,
         *
         * PLAY_SERVICES_RESOLUTION_REQUEST).show();

    } else {
        Log.i("Tag", "This device is not supported.");
        Utility.showAlert(context, "", "This device is not supported better change device for better performance");

    }*/

        return false;
    }

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setLocationPermission(boolean value, Context context) {
        SharedPreferences sfref = context.getSharedPreferences(SHARED_PREFERENCE, 0);
        SharedPreferences.Editor editor = sfref.edit();
        editor.putBoolean("LocationPermission", value);
        editor.apply();
    }

    public static boolean getLocationPermission(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCE, 0);
        return pref.getBoolean("LocationPermission", true);
    }

}
