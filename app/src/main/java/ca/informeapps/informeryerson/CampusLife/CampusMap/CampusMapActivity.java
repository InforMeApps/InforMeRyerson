/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.CampusMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import ca.informeapps.informeryerson.R;

public class CampusMapActivity extends Activity {

    LinearLayout linearLayout;
    boolean noConnection = false;
    private String mapsUrl = "https://m.ryerson.ca/core_apps/map/beta/";
    private WebView webView;
    private Menu optionsMenu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campusmap);

        linearLayout = (LinearLayout) findViewById(R.id.layout_campus_map_NoConnection);


        webView = (WebView) findViewById(R.id.webview_campusmap);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setGeolocationEnabled(true);//gets current location


        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        final Activity activity = this;
        webView.getSettings().setGeolocationDatabasePath(activity.getFilesDir().getPath());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override//passes current location to webview
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            public void onProgressChanged(WebView view, int progress) {
                if (progress != 100) {
                    setRefreshActionButtonState(true);
                } else {
                    setRefreshActionButtonState(false);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(mapsUrl)) {
                    view.loadUrl(url);
                }

                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                linearLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.INVISIBLE);
                noConnection = true;
            }
        });

        webView.loadUrl(mapsUrl);


        linearLayout.setVisibility(View.INVISIBLE);
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.campusmap_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Crashlytics.start(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_campusmap_refresh:
                webView.reload();
                linearLayout.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.VISIBLE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.action_campusmap_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.refresh_header_layout);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


}