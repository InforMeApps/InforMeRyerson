/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Transit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import ca.informeapps.informeryerson.R;

public class TransitActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    private int[] spinnerImages = {R.drawable.ttc_logo, R.drawable.go_logo};
    public static boolean isActive=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_transit);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_transit);
        spinner.setAdapter(new TransitSpinnerAdapter());
        spinner.setOnItemSelectedListener(this);
        ImageView imageView = (ImageView)findViewById(R.id.ChangeMode);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive=false;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(isActive) {
            if (i == 0) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_transit, new TTCUpdatesFragment()).commit();
            } else if (i == 1) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame_transit, new GOUpdatesFragment()).commit();
            }
        }
        else
        {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class TransitSpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return spinnerImages.length;
        }

        @Override
        public Object getItem(int i) {
            return spinnerImages[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return getCustomView(i, view, viewGroup);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            View rootView = getLayoutInflater().inflate(R.layout.layout_spinner_transit, null);

            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview_transit_spinner);

            Picasso.with(TransitActivity.this).load(spinnerImages[position]).into(imageView);

            return rootView;
        }
    }
}
