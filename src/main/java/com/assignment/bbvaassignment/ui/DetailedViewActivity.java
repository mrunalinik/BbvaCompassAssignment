package com.assignment.bbvaassignment.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.bbvaassignment.R;
import com.assignment.bbvaassignment.models.PlacesDTO;

public class DetailedViewActivity extends BaseActivity implements View.OnClickListener {

    private TextView txtName, txtRating, txtAddr;
    private ImageView navToDestination;
    private PlacesDTO placesDTO;
    public static final String EXTRA_MODEL = "model";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(EXTRA_MODEL)) {
            placesDTO = (PlacesDTO) bundle.getSerializable(EXTRA_MODEL);
        }else {
            finish();
            return;
        }

        initializeViews();
        setData(placesDTO);
    }

    private void setData(PlacesDTO placesDTO) {
        txtName.setText(placesDTO.getName());
        txtRating.setText(placesDTO.getPlaceRating());
        txtAddr.setText(placesDTO.getFormatted_address());
    }

    private void initializeViews() {
        txtAddr = findViewById(R.id.address);
        txtName = findViewById(R.id.name);
        txtRating = findViewById(R.id.rating);
        navToDestination = findViewById(R.id.navToDestination);
        navToDestination.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navToDestination:

                String url = "https://www.google.com/maps/dir/?api=1&destination=" + placesDTO.getLatitude() + "," + placesDTO.getLongitude() + "&travelmode=driving";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
