/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ca.informeapps.informeryerson.CampusLife.CampusLifeFragment;

/* TOO LAZY TO DOCUMENT STUFF #YOLOSWAG */

public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    private int visibleFragment = 0;
    private ListView mDrawerList;
    private DrawerListAdapter mListAdapter;
    private String[] mDrawerItems = {"Campus Life", "Places", "Events", /*"Settings",*/ "About Us", "FeedBack"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate");
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            getActionBar().setTitle(mDrawerItems[0]);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.content_frame_main, new CampusLifeFragment())
                    .commit();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mListAdapter = new DrawerListAdapter(LayoutInflater.from(this));
        mDrawerList = (ListView) findViewById(R.id.listview_drawer);
        mDrawerList.setAdapter(mListAdapter);
        mDrawerList.setOnItemClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                getActionBar().setTitle(mDrawerItems[visibleFragment]);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                getActionBar().setTitle("InforMeRyerson");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        new Handler().postDelayed(openDrawerRunnable(), 500); //When items are added to drawer

    }

    private Runnable openDrawerRunnable() {
        return new Runnable() {

            @Override
            public void run() {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mListAdapter.setSelectedItem(visibleFragment);
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("MainActivity", "onItemClick " + i);
        if (visibleFragment != i) {
            mListAdapter.setSelectedItem(i);
            mListAdapter.notifyDataSetChanged();

            boolean isFragment = true;
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = new CampusLifeFragment();
                    setVisibleFragment(i);
                    break;
                case 1:
                    isFragment = false;
                    /*fragment = new StudentLifeFragment();
                    setVisibleFragment(i);*/
                    break;
                case 2:
                    isFragment = false;
                    /*fragment = new EventsFragment();
                    setVisibleFragment(i);*/
                    break;
               /* case 3:
                    isFragment = false;
                    startActivity(new Intent(this, PreferencesActivity.class));
                    break;*/
                case 3:
                    isFragment = false;
                    startActivity(new Intent(this, AboutUsActivity.class));
                    break;
                case 4:
                    isFragment = false;
                    String[] emailAddress = {"informeapplications@gmail.com"};
                    String deviceInfo = "\n\n\nAPI Level: " + Build.VERSION.SDK_INT + "\n" + Build.DEVICE + "\n"
                            + Build.MODEL + "\n" + Build.MANUFACTURER + "\n" + Build.PRODUCT;
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback InforMeRyerson");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, deviceInfo);
                    startActivity(emailIntent);
                    break;
            }

            if (isFragment) {
                final Fragment finalFragment = fragment;
                final int clickPosition = i;
                getActionBar().setTitle(mDrawerItems[i]);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.content_frame_main, finalFragment)
                                .commit();
                    }
                }, 250);
            }
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void setVisibleFragment(int i) {
        visibleFragment = i;
    }

    public class DrawerListAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private int mSelectedItem;
        private int[] mDrawerDrawables = {/*R.drawable.ic_settings,*/ R.drawable.ic_about, R.drawable.ic_feedback};

        public DrawerListAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        public void setSelectedItem(int i) {
            mSelectedItem = i;
        }

        @Override
        public int getCount() {
            return mDrawerItems.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position <= 2) {
                convertView = inflater.inflate(R.layout.layout_drawer_primary, null);
                TextView wip = (TextView) convertView.findViewById(R.id.textview_drawer_list_primary_WIP);
                if (position > 0) {
                    wip.setVisibility(View.VISIBLE);
                }
                TextView titlePrimary = (TextView) convertView.findViewById(R.id.textView_drawer_list_primary);
                titlePrimary.setText(mDrawerItems[position]);
                //navigation drawer selected item coloring
                //titlePrimary.setTextColor((mSelectedItem == position) ? Color.parseColor("#3f51b5") : Color.parseColor("#454545"));
                if (position == 0) {
                    titlePrimary.setTextColor(Color.parseColor("#3f51b5"));
                }

                return convertView;
            } else {
                convertView = inflater.inflate(R.layout.layout_drawer_secondary, null);
                TextView titleSecondary = (TextView) convertView.findViewById(R.id.textView_drawer_list_secondary);
                titleSecondary.setText(mDrawerItems[position]);

                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_drawer_list_secondary);
                imageView.setImageResource(mDrawerDrawables[position - 3]);
                return convertView;
            }

        }
    }
}
