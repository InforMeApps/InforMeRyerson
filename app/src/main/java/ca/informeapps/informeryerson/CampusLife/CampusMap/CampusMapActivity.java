package ca.informeapps.informeryerson.CampusLife.CampusMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ca.informeapps.informeryerson.R;

public class CampusMapActivity extends Activity {

    private String mapsUrl = "https://m.ryerson.ca/core_apps/map/beta/";
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campusmap);

        webView = (WebView) findViewById(R.id.webview_campusmap);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_campusmap);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        final Activity activity = this;

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress != 100) {
                    progressBar.setProgress(progress);
                } else {
                    progressBar.setVisibility(View.GONE);
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
            }
        });

        webView.loadUrl(mapsUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.campusmap_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_campusmap_refresh:
                webView.reload();
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
