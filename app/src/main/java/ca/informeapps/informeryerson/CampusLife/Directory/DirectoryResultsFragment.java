/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Directory;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import ca.informeapps.informeryerson.R;

public class DirectoryResultsFragment extends Fragment {

    private View rootView;
    private ListView listView;
    private LinearLayout errorView;
    private DirectoryResultsListAdapter adapter;
    private String[] names, titles, locations, extensions, emails;
    private String searchText;
    private int[] nums;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        searchText = WordUtils.capitalizeFully(args.getString("SEARCH_REQUEST"));
        names = args.getStringArray("ARRAY_NAME");
        titles = args.getStringArray("ARRAY_TITLE");
        locations = args.getStringArray("ARRAY_LOCATION");
        extensions = args.getStringArray("ARRAY_EXTENSION");
        emails = args.getStringArray("ARRAY_EMAIL");
        nums = args.getIntArray("ARRAY_NUM");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Search: " + searchText);
        rootView = inflater.inflate(R.layout.fragment_directory_results, container, false);
        errorView = (LinearLayout) rootView.findViewById(R.id.layout_directory_results_error);
        listView = (ListView) rootView.findViewById(R.id.listview_directory_results);

        if (names.length != 0) {
            adapter = new DirectoryResultsListAdapter();
            listView.setAdapter(adapter);
        } else {
            listView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public class DirectoryResultsListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public DirectoryResultsListAdapter() {
            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return names.length;
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

            View rootview = inflater.inflate(R.layout.layout_list_directory_results, null);

            LinearLayout five = (LinearLayout) rootview.findViewById(R.id.layout_directory_list_5);
            LinearLayout four = (LinearLayout) rootview.findViewById(R.id.layout_directory_list_4);
            LinearLayout three = (LinearLayout) rootview.findViewById(R.id.layout_directory_list_3);
            LinearLayout two = (LinearLayout) rootview.findViewById(R.id.layout_directory_list_2);

            if (nums[i] == 5) {

                five.setVisibility(View.VISIBLE);
                TextView name = (TextView) rootview.findViewById(R.id.textview_directory_list_name_5);
                TextView title = (TextView) rootview.findViewById(R.id.textview_directory_list_title_5);
                TextView location = (TextView) rootview.findViewById(R.id.textview_directory_list_location_5);
                TextView extension = (TextView) rootview.findViewById(R.id.textview_directory_list_extension_5);
                TextView email = (TextView) rootview.findViewById(R.id.textview_directory_list_email_5);

                name.setText(names[i]);
                title.setText(titles[i]);
                location.setText(locations[i]);
                extension.setText(extensions[i]);
                email.setText(emails[i]);

                return rootview;

            } else if (nums[i] == 4) {

                four.setVisibility(View.VISIBLE);
                TextView name = (TextView) rootview.findViewById(R.id.textview_directory_list_name_4);
                TextView location = (TextView) rootview.findViewById(R.id.textview_directory_list_location_4);
                TextView extension = (TextView) rootview.findViewById(R.id.textview_directory_list_extension_4);
                TextView email = (TextView) rootview.findViewById(R.id.textview_directory_list_email_4);

                name.setText(names[i]);
                location.setText(locations[i]);
                extension.setText(extensions[i]);
                email.setText(emails[i]);

                return rootview;

            } else if (nums[i] == 3) {

                three.setVisibility(View.VISIBLE);
                TextView name = (TextView) rootview.findViewById(R.id.textview_directory_list_name_3);
                TextView extension = (TextView) rootview.findViewById(R.id.textview_directory_list_extension_3);
                TextView email = (TextView) rootview.findViewById(R.id.textview_directory_list_email_3);

                name.setText(names[i]);
                extension.setText(extensions[i]);
                email.setText(emails[i]);

                return rootview;

            } else {

                two.setVisibility(View.VISIBLE);
                TextView name = (TextView) rootview.findViewById(R.id.textview_directory_list_name_2);
                TextView email = (TextView) rootview.findViewById(R.id.textview_directory_list_email_2);

                name.setText(names[i]);
                email.setText(emails[i]);

                return rootview;

            }
        }
    }
}
