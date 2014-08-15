/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Directory;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ca.informeapps.informeryerson.R;

public class SearchByDepartment extends AsyncTask<Void, Void, Void> {

    private String text;
    private Document doc;
    private Activity activity;
    private ProgressDialog progressDialog;
    private List<String> nameList, titleList, locationList, extensionList, emailList;
    private List<Integer> numResults;

    public SearchByDepartment(String searchText, Activity activity) {
        text = searchText;
        this.activity = activity;

        nameList = new LinkedList<String>();
        titleList = new LinkedList<String>();
        locationList = new LinkedList<String>();
        extensionList = new LinkedList<String>();
        emailList = new LinkedList<String>();
        numResults = new LinkedList<Integer>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        nameList.clear();
        titleList.clear();
        locationList.clear();
        extensionList.clear();
        emailList.clear();
        numResults.clear();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            doc = Jsoup.connect("https://m.ryerson.ca/core_apps/directory/index.cfm")
                    .data("department", text)
                    .data("subDept", "Find")
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Elements results = doc.select("div[class=staffMember]");
        int x = results.size();
        for (int i = 0; i < results.size(); i++) {
            Element oneResult = results.get(i);
            int j = oneResult.children().size();
            numResults.add(j);
            if (j == 5) {
                nameList.add(oneResult.child(0).text());
                titleList.add(oneResult.child(1).text());
                locationList.add(oneResult.child(2).text());
                extensionList.add(oneResult.child(3).text());
                emailList.add(oneResult.child(4).text());
            } else if (j == 4) {
                nameList.add(oneResult.child(0).text());
                titleList.add("");
                locationList.add(oneResult.child(1).text());
                extensionList.add(oneResult.child(2).text());
                emailList.add(oneResult.child(3).text());
            } else if (j == 3) {
                nameList.add(oneResult.child(0).text());
                titleList.add("");
                locationList.add("");
                extensionList.add(oneResult.child(1).text());
                emailList.add(oneResult.child(2).text());
            } else if (j == 2) {
                nameList.add(oneResult.child(0).text());
                titleList.add("");
                locationList.add("");
                extensionList.add("");
                emailList.add(oneResult.child(1).text());
            } else if (j == 1) {
                nameList.add(oneResult.child(0).text());
                titleList.add("");
                locationList.add("");
                extensionList.add("");
                emailList.add("");
            }
        }
        progressDialog.dismiss();

        String[] namesArray = nameList.toArray(new String[0]);
        String[] titlesArray = titleList.toArray(new String[0]);
        String[] locationsArray = locationList.toArray(new String[0]);
        String[] extensionsArray = extensionList.toArray(new String[0]);
        String[] emailsArray = emailList.toArray(new String[0]);
        int[] numArray = ArrayUtils.toPrimitive(numResults.toArray(new Integer[numResults.size()]));

        Fragment resultFragment = new DirectoryResultsFragment();
        Bundle args = new Bundle();
        args.putStringArray("ARRAY_NAME", namesArray);
        args.putStringArray("ARRAY_TITLE", titlesArray);
        args.putStringArray("ARRAY_LOCATION", locationsArray);
        args.putStringArray("ARRAY_EXTENSION", extensionsArray);
        args.putStringArray("ARRAY_EMAIL", emailsArray);
        args.putIntArray("ARRAY_NUM", numArray);
        resultFragment.setArguments(args);

        activity.getFragmentManager().beginTransaction()
                .replace(R.id.content_frame_directory, resultFragment)
                .addToBackStack(null)
                .commit();
    }
}
