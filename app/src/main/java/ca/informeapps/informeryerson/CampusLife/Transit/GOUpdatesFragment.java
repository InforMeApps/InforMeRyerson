/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Transit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ca.informeapps.informeryerson.R;

/**
 * Created by Tanmay on 2014-08-27.
 */
public class GOUpdatesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ServiceAlertListAdapter adapter;
    private ListView listView;
    private List<String> alertList, lastUpdatedList;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_transit_detail, container, false);

        alertList = new LinkedList<String>();
        lastUpdatedList = new LinkedList<String>();

        listView = (ListView) rootView.findViewById(R.id.listview_transit_servicealert_ttc);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh_container_servicealert_ttc);

        listView.setOnScrollListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);

        if (isNetworkAvailable()) {
            new GOServiceAlerts().execute();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection :(", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable()) {
            alertList.clear();
            lastUpdatedList.clear();
            new GOServiceAlerts().execute();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection :(", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        int topRowVerticalPosition =
                (listView == null || listView.getChildCount() == 0) ?
                        0 : listView.getChildAt(0).getTop();
        swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
    }

    public class ServiceAlertListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return alertList.size();
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

            ViewHolder holder;

            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.layout_list_servicealerts, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.alertTextV.setText(alertList.get(i));
            holder.updateTextV.setText(lastUpdatedList.get(i));

            return view;
        }
    }

    public class ViewHolder {

        private TextView alertTextV;
        private TextView updateTextV;

        public ViewHolder(View v) {
            alertTextV = (TextView) v.findViewById(R.id.textview_servicealert_list_alert);
            updateTextV = (TextView) v.findViewById(R.id.textview_servicealert_list_update);
        }
    }

    public class GOServiceAlerts extends AsyncTask<Void, Void, Void> {

        private final String url = "http://www.gotransit.com/publicroot/en/updates/servicestatus.aspx";
        private Document doc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Elements updatesArea = doc.select("div[id=divupdates_train]");
            Elements onlyAlert = updatesArea.select("div[class=publishedContent]");
            Elements timeArea = doc.select("div[class=RadAjaxPanel]");
            Elements time = timeArea.select("span[id=serviceUpdateTimeStampPage");

            for (int i = 0; i < onlyAlert.size(); i++) {
                alertList.add(onlyAlert.get(i).text());
                lastUpdatedList.add("Last Updated: " + time.get(0).text());
            }

            TextView textView = (TextView) rootView.findViewById(R.id.textview_transit_noerror);

            if (alertList.size() != 0) {
                adapter = new ServiceAlertListAdapter();
                listView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

        }
    }
}
