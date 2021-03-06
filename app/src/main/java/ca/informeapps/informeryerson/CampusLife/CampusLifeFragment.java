/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import ca.informeapps.informeryerson.CampusLife.Bookstore.BookstoreActivity;
import ca.informeapps.informeryerson.CampusLife.CampusMap.CampusMapActivity;
import ca.informeapps.informeryerson.CampusLife.Directory.DirectoryActivity;
import ca.informeapps.informeryerson.CampusLife.Reminders.RemindersActivity;
import ca.informeapps.informeryerson.CampusLife.Schedule.ScheduleActivity;
import ca.informeapps.informeryerson.CampusLife.Transit.TransitActivity;
import ca.informeapps.informeryerson.MainActivity;
import ca.informeapps.informeryerson.Misc.AnalyticsSampleApp;
import ca.informeapps.informeryerson.R;

public class CampusLifeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private Tracker t;
    private View rootView;
    private ListView mListView;
    private CampusLifeListAdapter adapter;
    private String[] listTitles = {"My Schedule", "Reminders", "Directory", "Bookstore", "Transit Info."};
    private int[] listImages = {R.drawable.campuslife_icons_schedule, R.drawable.campuslife_icons_reminders,
            R.drawable.campuslife_icons_directory, R.drawable.campuslife_icons_bookstore, R.drawable.campuslife_icons_transit};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().getActionBar().setTitle("Campus Life");
        rootView = inflater.inflate(R.layout.fragment_campuslife, container, false);
        View header = inflater.inflate(R.layout.layout_campuslife_list_header, mListView, false);



        mListView = (ListView) rootView.findViewById(R.id.listview_campuslife);
        adapter = new CampusLifeListAdapter(getActivity().getLayoutInflater());

        ImageView headerImage = (ImageView) header.findViewById(R.id.imageview_campuslife_list_header);
        Picasso.with(getActivity()).load(R.drawable.campuslife_header).into(headerImage);

        mListView.addHeaderView(header);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);

        t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);
        t.setScreenName("Campus Life");
        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        //ios recruiting things
        final LinearLayout recruitLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout_campuslife_recruit);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(!prefs.getBoolean("IOSClose", false)) {
            ImageButton closeRecruit = (ImageButton) rootView.findViewById(R.id.imagebutton_campuslife_recruit);
            closeRecruit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_exit);
                    recruitLayout.setAnimation(slideDown);
                    recruitLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("IOSClose", true).commit();
                }
            });
            TextView moreInfo = (TextView) rootView.findViewById(R.id.textview_campuslife_recruit);
            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "informeapplications@gmail.com", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "iOS Developer");
                    intent.putExtra(Intent.EXTRA_TEXT,"(Attach a resume or put some facts about yourself here! Don't forget to put your contact information)");
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });

        }
        else {
            recruitLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        switch (i) {
            case 0:
                startActivity(new Intent(getActivity(), CampusMapActivity.class));
                t.send(new HitBuilders.EventBuilder().setCategory("Campus Life")
                        .setAction("Maps").setLabel("IM LOST SHIIT!!").build());
                break;
            case 1:
                startActivity(new Intent(getActivity(), ScheduleActivity.class));
                t.send(new HitBuilders.EventBuilder().setCategory("Campus Life")
                        .setAction("MySchedule").setLabel("IM CHECKING CLASSES TO SKIP YOLO").build());
                break;
            case 2:
                startActivity(new Intent(getActivity(), RemindersActivity.class));
                t.send(new HitBuilders.EventBuilder().setCategory("Campus Life")
                        .setAction("Reminders").setLabel("FORGETFUL MIND").build());
                break;
            case 3:
                startActivity(new Intent(getActivity(), DirectoryActivity.class));
                t.send(new HitBuilders.EventBuilder().setCategory("Campus Life")
                        .setAction("Directories").setLabel("STALKING SOME PEOPLE").build());
                break;
            case 4:
                startActivity(new Intent(getActivity(), BookstoreActivity.class));
                t.send(new HitBuilders.EventBuilder().setCategory("Campus Life")
                        .setAction("Bookstore").setLabel("NEEDA SAVE THAT MONEY #CASHMONEY").build());
                break;
            case 5:
                startActivity(new Intent(getActivity(), TransitActivity.class));
                t.send(new HitBuilders.EventBuilder().setCategory("Campus Life")
                        .setAction("TTC Info").setLabel("Cant be late").build());
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.getBoolean("pref_key_google_analytics", true)) {
            GoogleAnalytics.getInstance(getActivity().getApplicationContext()).dispatchLocalHits();
        }
    }


    private void setupEvent(View v, int ClickId, final int categoryId, final int actionId, final int labelId) {
        final Button pageviewButton = (Button) v.findViewById(ClickId);
        pageviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracker t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);
                t.send(new HitBuilders.EventBuilder().setCategory(getString(categoryId))
                        .setAction(getString(actionId)).setLabel(getString(labelId)).build());
            }
        });
    }

    private class CampusLifeListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public CampusLifeListAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return listTitles.length;
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
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = inflater.inflate(R.layout.layout_list_campuslife, null);

            TextView title = (TextView) view.findViewById(R.id.textview_campuslife_list);
            title.setText(listTitles[i]);

            ImageView image = (ImageView) view.findViewById(R.id.imageview_campuslife_list);
            Picasso.with(getActivity()).load(listImages[i]).into(image);

            return view;
        }
    }
}
