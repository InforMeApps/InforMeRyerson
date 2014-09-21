/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.Events;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import ca.informeapps.informeryerson.Misc.ServiceHandler;
import ca.informeapps.informeryerson.R;

public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    private static final String TAG_EVENTS = "events";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_ORGANIZER = "organizer";
    private static final String TAG_TIME = "time";
    private static final String TAG_FROM = "from";
    private static final String TAG_TO = "to";
    private static final String TAG_DATE = "date";
    private static String url = "https://dl.dropboxusercontent.com/u/69305400/json_test/";

    private JSONArray events = null;
    private ArrayList<HashMap<String, String>> eventList;
    private ProgressDialog progressDialog;
    private View rootView;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView errorText;
    private boolean dataFound = true;
    private eventsListAdapter adapter;
    private GetContacts getContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_events, container, false);

        eventList = new ArrayList<HashMap<String, String>>();
        listView = (ListView) rootView.findViewById(R.id.listview_events);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_container_events);
        errorText = (TextView) rootView.findViewById(R.id.textview_nodatafound);
        errorText.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        listView.setOnScrollListener(this);
        listView.setTextFilterEnabled(true);
        getContacts = new GetContacts();

        getContacts.execute();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRefresh() {

        new GetContacts().execute();
        if (dataFound) {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ?
                0 : listView.getChildAt(0).getTop();
        swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            if (jsonStr != null) {
                try {

                    eventList.clear();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    events = jsonObject.getJSONArray(TAG_EVENTS);

                    for (int i = 0; i < events.length(); i++) {

                        JSONObject c = events.getJSONObject(i);
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String description = c.getString(TAG_DESCRIPTION);
                        String organizer = c.getString(TAG_ORGANIZER);
                        String date = c.getString(TAG_DATE);

                        JSONObject time = c.getJSONObject(TAG_TIME);
                        String time_from = time.getString(TAG_FROM);
                        String time_to = time.getString(TAG_TO);

                        HashMap<String, String> event = new HashMap<String, String>();

                        event.put(TAG_ID, id);
                        event.put(TAG_NAME, name);
                        event.put(TAG_DESCRIPTION, description);
                        event.put(TAG_DATE, date);
                        event.put("IMAGE", Integer.toString(R.drawable.campuslife_icons_bookstore));

                        eventList.add(event);
                    }
                    dataFound = true;
                    Collections.sort(eventList, new DateComparator());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Could not get data from url");
                dataFound = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if (dataFound) {
                adapter = new eventsListAdapter(eventList);
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
                errorText.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
            }
        }
    }

    public class DateComparator implements Comparator<HashMap<String, String>> {

        @Override
        public int compare(HashMap<String, String> stringStringHashMap, HashMap<String, String> stringStringHashMap2) {
            return stringStringHashMap.get(TAG_NAME).compareTo(stringStringHashMap2.get(TAG_NAME));
        }
    }

    public class eventsListAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ArrayList<HashMap<String, String>> list;

        public eventsListAdapter(ArrayList<HashMap<String, String>> inputList) {
            inflater = LayoutInflater.from(getActivity());
            list = inputList;
        }

        public void setArrayList(ArrayList<HashMap<String, String>> inputList) {
            list = inputList;
        }

        @Override
        public int getCount() {
            return events.length();
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

            View listLayoutView = inflater.inflate(R.layout.layout_list_events_small, null);
            String title = list.get(i).get(TAG_NAME);
            String description = list.get(i).get(TAG_DESCRIPTION);
            String date = list.get(i).get(TAG_DATE);
            Date d = new Date(Long.parseLong(date));
            CharSequence dateFormatted = d.toString().subSequence(0, 10);

            TextView name = (TextView) listLayoutView.findViewById(R.id.textview_events_list_small_title);
            TextView desc = (TextView) listLayoutView.findViewById(R.id.textview_events_list_small_description);
            TextView orga;
            if (i % 2 == 0) {
                orga = (TextView) listLayoutView.findViewById(R.id.textview_events_list_small_date_left);
                orga.setVisibility(View.VISIBLE);
            } else {
                orga = (TextView) listLayoutView.findViewById(R.id.textview_events_list_small_date_right);
                orga.setVisibility(View.VISIBLE);
            }

            name.setText(title);
            desc.setText(description);
            //orga.setText(dateFormatted);

            return listLayoutView;
        }
    }
}
