/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Directory;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.text.WordUtils;

import ca.informeapps.informeryerson.R;

public class DirectoryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View rootView;
    private EditText searchText;
    private Button buttonText;
    private SearchByKeyword searchByKeyword;
    private SearchByDepartment searchByDepartment;
    private DirectorySpinnerItems directorySpinnerItems;
    private String[] spinnerItems, spinnerItemValues;
    private ListView departmentList;
    private DepartmentListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("Directory");
        rootView = inflater.inflate(R.layout.fragment_directory, container, false);

        initialize();

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    buttonText.performClick();
                    hideKeyboard();
                    handled = true;
                }
                return handled;
            }
        });

        adapter = new DepartmentListAdapter();
        departmentList.setAdapter(adapter);

        return rootView;
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchText.setText("");
    }

    public void initialize() {
        directorySpinnerItems = new DirectorySpinnerItems();
        spinnerItems = directorySpinnerItems.getSpinnerItems();
        spinnerItemValues = directorySpinnerItems.getSpinnerItemValues();

        searchText = (EditText) rootView.findViewById(R.id.edittext_directory_search);
        buttonText = (Button) rootView.findViewById(R.id.button_directory_searchtext);
        departmentList = (ListView) rootView.findViewById(R.id.listview_directory_departmentlist);
        departmentList.setOnItemClickListener(this);
        buttonText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String inputText = searchText.getText().toString();
        if (inputText.length() >= 3) {
            if (isNetworkAvailable()) {
                searchByKeyword = new SearchByKeyword(inputText, getActivity());
                searchByKeyword.execute();
            } else {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            }

            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } else {
            Toast.makeText(getActivity(), "Search must contain at least 3 characters", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (isNetworkAvailable()) {
            searchByDepartment = new SearchByDepartment(spinnerItemValues[i], getActivity());
            searchByDepartment.execute();
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class DepartmentListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return spinnerItems.length;
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

            view = getActivity().getLayoutInflater().inflate(R.layout.layout_list_directory_departmentlist, null);

            TextView textView = (TextView) view.findViewById(R.id.textview_directory_list_department_name);
            String s = spinnerItems[i];
            textView.setText(WordUtils.capitalizeFully(s));

            return view;
        }
    }
}
