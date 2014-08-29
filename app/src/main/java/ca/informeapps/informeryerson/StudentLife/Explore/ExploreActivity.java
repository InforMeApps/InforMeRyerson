/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.StudentLife.Explore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.IOError;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ca.informeapps.informeryerson.Misc.ExpandAnimation;
import ca.informeapps.informeryerson.R;

/**
 * Created by Shahar on 2014-07-31.
 */
public class ExploreActivity extends Activity {

    private boolean isRotated = false;
    private String Title;
    private ListView ExploreViewListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        ExploreViewListItems = (ListView) findViewById(R.id.ListExploreView);
        ArrayAdapter<String> ExploreListAdapter = new ListAdapter(this, R.layout.layout_list_explore);

        Title = getIntent().getStringExtra("Name");
        setTitle(Title);


        if (Title.equals("Coffee Deals")) {
            ExploreListAdapter.add("Second Cup");
            ExploreListAdapter.add("Second Cup");
            ExploreListAdapter.add("Second Cup");
            ExploreListAdapter.add("Tim Hortons");//sending store name

        } else if (Title.equals("Shopping Deals")) {
            ExploreListAdapter.add("Shoppers Drug Mart");
        } else if (Title.equals("Eating Deals")) {

        } else if (Title.equals("Drink Deals")) {

        }



        ExploreViewListItems.setAdapter(ExploreListAdapter);




        ExploreViewListItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                View ExploreExpand = view.findViewById(R.id.EpandedInfo);
                ExpandAnimation expandAni = new ExpandAnimation(ExploreExpand, 250);
                ExploreExpand.startAnimation(expandAni);

                ExploreViewListItems.smoothScrollToPositionFromTop(position, 0, 150);

                //Arrow Rotation
                ImageView icon = (ImageView) view.findViewById(R.id.imageview_explore_downicon);
                if (!isRotated) {
                    RotateAnimation rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(250);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setFillEnabled(true);
                    icon.startAnimation(rotateAnimation);
                    isRotated = true;
                } else {
                    RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(250);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setFillEnabled(true);
                    icon.startAnimation(rotateAnimation);
                    isRotated = false;
                }

            }

        });
    }

    public int ResourceID(String StoreName) {
        int ResID;
        String n = StoreName.toLowerCase();
        String name = n.replaceAll("\\W", "");
        ResID = getResources().getIdentifier(name, "drawable", getPackageName());
        return ResID;
    }




    class ListAdapter extends ArrayAdapter<String> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_list_explore, null);
            }

            TextView StoreName = (TextView) convertView.findViewById(R.id.StoreName);
            ImageView StoreLogo = (ImageView) convertView.findViewById(R.id.StoreLogo);
            StoreName.setText(getItem(position));//set the text
            //StoreLogo.setImageResource(ResourceID(getItem(position)));//set the image resource
            StoreLogo.setImageResource(R.drawable.campuslife_header);

            View toolbar = convertView.findViewById(R.id.EpandedInfo);
            ((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -50;
            toolbar.setVisibility(View.GONE);







            //Favourites button click listener
            final ImageButton fav = (ImageButton) convertView.findViewById(R.id.imagebutton_explore_list_favourite);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    ScaleAnimation scaleDown =
                            new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleDown.setFillAfter(true);
                    scaleDown.setFillEnabled(true);
                    scaleDown.setDuration(150);
                    fav.startAnimation(scaleDown);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ScaleAnimation scaleUp =
                                    new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            scaleUp.setFillAfter(true);
                            scaleUp.setFillEnabled(true);
                            scaleUp.setDuration(150l);
                            fav.startAnimation(scaleUp);
                            fav.setImageResource(R.drawable.ic_favourite_activated);
                        }
                    }, 150);
                }
            });

            return convertView;
        }
    }
}
