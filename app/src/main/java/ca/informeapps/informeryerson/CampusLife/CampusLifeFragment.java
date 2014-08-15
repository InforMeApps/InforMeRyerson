/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ca.informeapps.informeryerson.CampusLife.Bookstore.BookstoreActivity;
import ca.informeapps.informeryerson.CampusLife.CampusMap.CampusMapActivity;
import ca.informeapps.informeryerson.CampusLife.Directory.DirectoryActivity;
import ca.informeapps.informeryerson.CampusLife.Reminders.RemindersActivity;
import ca.informeapps.informeryerson.CampusLife.Schedule.ScheduleActivity;
import ca.informeapps.informeryerson.MainActivity;
import ca.informeapps.informeryerson.R;

public class CampusLifeFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View rootView;
    private ListView mListView;
    private CampusLifeListAdapter adapter;
    private String[] listTitles = {"My Schedule", "Reminders", "Directory", "Bookstore"};
    private int[] listImages = {R.drawable.campuslife_icons_schedule, R.drawable.campuslife_icons_reminders,
            R.drawable.campuslife_icons_directory, R.drawable.campuslife_icons_bookstore};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("CampusLifeFragment", "onCreateView");
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

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        MainActivity mainActivity = (MainActivity) getActivity();
        Fragment fragment = null;
        boolean isFragment = true;

        switch (i) {
            case 0:
                isFragment = false;
                startActivity(new Intent(getActivity(), CampusMapActivity.class));
                break;
            case 1:
                isFragment = false;
                startActivity(new Intent(getActivity(), ScheduleActivity.class));
                break;
            case 2:
                isFragment = false;
                startActivity(new Intent(getActivity(), RemindersActivity.class));
                break;
            case 3:
                isFragment = false;
                startActivity(new Intent(getActivity(), DirectoryActivity.class));
                break;
            case 4:
                isFragment = false;
                startActivity(new Intent(getActivity(), BookstoreActivity.class));
                break;
        }

        if (isFragment && fragment != null) {
            mainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit, R.anim.slide_left_exit, R.anim.slide_left_enter)
                    .replace(R.id.content_frame_main, fragment)
                    .addToBackStack(null)
                    .commit();
        }
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
