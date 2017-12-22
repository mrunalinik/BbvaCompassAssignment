package com.assignment.bbvaassignment.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.assignment.bbvaassignment.R;
import com.assignment.bbvaassignment.listeners.DialogMangerCallback;
import com.assignment.bbvaassignment.utils.DialogManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1001;
    private static final int REQUEST_LOCATION_RATIONALE = 1002;
    private Button navToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navToMain = (Button) findViewById(R.id.navToMain);
        navToMain.setOnClickListener(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navToMain:
                if (checkForLocPermission()) {
                    navToMaps();
                } else {
                    requestForLocPermission();
                    return;
                }

            break;
        }
    }

    private void navToMaps(){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }


    private boolean checkForLocPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void requestForLocPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            DialogManager.showConfirmPopup(MainActivity.this, new DialogMangerCallback() {
                @Override
                public void onOkClick() {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_RATIONALE);
                }

                @Override
                public void onCancelClick() {

                }
            }, getString(R.string.app_name), getString(R.string.locationPermissionMsg), getString(R.string.ok), getString(R.string.cancel));



        } else {

            // No explanation needed, we can request the permission.

            DialogManager.showConfirmPopup(MainActivity.this, new DialogMangerCallback() {
                @Override
                public void onOkClick() {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);
                }

                @Override
                public void onCancelClick() {

                }
            }, getString(R.string.app_name), getString(R.string.locationPermissionMsg), getString(R.string.ok), getString(R.string.cancel));


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }
}
