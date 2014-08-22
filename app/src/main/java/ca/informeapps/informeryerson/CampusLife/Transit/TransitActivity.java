/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Transit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ca.informeapps.informeryerson.R;

public class TransitActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<String> alertList, lastUpdatedList;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ServiceAlertListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);

        alertList = new LinkedList<String>();
        lastUpdatedList = new LinkedList<String>();

        listView = (ListView) findViewById(R.id.listview_transit_servicealert_ttc);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_container_servicealert_ttc);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);

        if (isNetworkAvailable()) {
            new TTCServiceAlerts().execute();
        } else {
            Toast.makeText(this, "No Internet Connection :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Crashlytics.start(this);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable()) {
            alertList.clear();
            lastUpdatedList.clear();
            new TTCServiceAlerts().execute();
        } else {
            Toast.makeText(this, "No Internet Connection :(", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
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

            view = getLayoutInflater().inflate(R.layout.layout_list_servicealerts, null);

            TextView alertTextV = (TextView) view.findViewById(R.id.textview_servicealert_ttc_list_alert);
            TextView updateTextV = (TextView) view.findViewById(R.id.textview_servicealert_ttc_list_update);

            alertTextV.setText(alertList.get(i));
            updateTextV.setText(lastUpdatedList.get(i));

            return view;
        }
    }

    public class TTCServiceAlerts extends AsyncTask<Void, Void, Void> {

        private final String url = "https://m.ttc.ca/mobile/index.jsp";
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

            Elements serviceAlerts = doc.select("div[class=ttc-service-alert");

            for (int i = 0; i < serviceAlerts.size(); i++) {
                Element alert = serviceAlerts.get(i);
                alertList.add(alert.child(1).text());
                lastUpdatedList.add(alert.child(2).text());
            }

            adapter = new ServiceAlertListAdapter();
            listView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
