/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Shahar on 2014-08-23.
 */
public class WalkthrougAsync extends AsyncTask<Void, Void, Void> {

    private Document doc;
    public Element one;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public Element getOne() {
        return one;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Elements results = doc.select("div#googletoken");
        one =results.get(0);
        Log.d("",""+one);


    }

    @Override
    protected Void doInBackground(Void... voids) {
        Connection.Response res=null;
        try {
            res = Jsoup.connect("https://cas.ryerson.ca/login?service=https%3A%2F%2Fmy.ryerson.ca%2FLogin")
                    .data("username", "siamin", "password", "pass")
                    .method(Connection.Method.POST)
                    .execute();
            Log.d("", "here");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> loginCookies = res.cookies();
        try {
            doc = Jsoup.connect("https://my.ryerson.ca/render.userLayoutRootNode.uP?uP_fname=ryersonApplications&appId=googleToken&uP_sparam=activeTab&activeTab=2")
                    .cookies(loginCookies)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
