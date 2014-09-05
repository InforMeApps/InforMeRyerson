/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.viewpagerindicator.UnderlinePageIndicator;

import ca.informeapps.informeryerson.Misc.CircleImageView2;

public class AboutUsActivity extends FragmentActivity {

    private ListView listView;
    private ViewPager viewPager;
    private String[] teamNames = {"Raj", "Patrick", "Tanmay", "Shahar"};
    private String[] teamDescription = {"CoFounder/Project Lead/Marketing", "Co-Founder/COO/CFO", "UI/UX Designer/Developer", "Developer"};
    private int[] teamImages = {R.drawable.aboutus_raj, R.drawable.aboutus_patrick, R.drawable.aboutus_tanmay, R.drawable.aboutus_shahar};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_aboutus);
        getActionBar().setHomeButtonEnabled(true);

        View headerView = getLayoutInflater().inflate(R.layout.layout_list_header_aboutus, null);
        View footerView = getLayoutInflater().inflate(R.layout.layout_list_footer_aboutus, null);

        viewPager = (ViewPager) footerView.findViewById(R.id.pager_aboutus_contributions);
        viewPager.setAdapter(new AboutUsSlideAdapter(getSupportFragmentManager()));

        UnderlinePageIndicator underlinePageIndicator = (UnderlinePageIndicator) footerView.findViewById(R.id.viewpager_indicator_aboutus);
        underlinePageIndicator.setViewPager(viewPager);

        listView = (ListView) findViewById(R.id.listview_aboutus);
        AboutUsListAdapter adapter = new AboutUsListAdapter();
        listView.addHeaderView(headerView, null, false);
        listView.addFooterView(footerView, null, false);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Crashlytics.start(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class AboutUsPagerFragment extends Fragment {

        private int position;
        private ListView listView;
        private String[] listData;
        private String title;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle b = getArguments();
            position = b.getInt("POSITION");

            switch (position) {
                case 0:
                    listData = getResources().getStringArray(R.array.Libraries);
                    title = "Libraries";
                    break;
                case 1:
                    listData = getResources().getStringArray(R.array.Iconography);
                    title = "Iconography";
                    break;
                case 2:
                    listData = getResources().getStringArray(R.array.Images);
                    title = "Images";
                    break;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_aboutus_contributions, container, false);

            listView = (ListView) rootView.findViewById(R.id.listview_aboutus_contributions);
            listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listData));

            TextView textView = (TextView) rootView.findViewById(R.id.textview_aboutus_contributions_title);
            textView.setText(title);

            return rootView;
        }
    }

    private class AboutUsListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return teamNames.length;
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

            View row = view;
            ViewHolder holder = null;

            if (row == null) {
                row = getLayoutInflater().inflate(R.layout.layout_list_aboutus, null);
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            holder.textView.setText(teamNames[i]);
            holder.imageView.setImageResource(teamImages[i]);
            holder.descTextV.setText(teamDescription[i]);

            return row;
        }


        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }
    }

    class ViewHolder {

        CircleImageView2 imageView;
        TextView textView, descTextV;

        ViewHolder(View v) {
            imageView = (CircleImageView2) v.findViewById(R.id.imageview_aboutus_list_team);
            textView = (TextView) v.findViewById(R.id.textView_aboutus_team_name);
            descTextV = (TextView) v.findViewById(R.id.textView_aboutus_team_description);
        }

    }

    private class AboutUsSlideAdapter extends FragmentStatePagerAdapter {

        public AboutUsSlideAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {

            Fragment fragment = new AboutUsPagerFragment();
            Bundle args = new Bundle();
            args.putInt("POSITION", i);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
