package ca.informeapps.informeryerson.CampusLife.Directory;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ca.informeapps.informeryerson.R;

public class DirectoryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private View rootView;
    private Spinner spinner;
    private EditText searchText;
    private Button buttonText;
    private SearchByKeyword searchByKeyword;
    private SearchByDepartment searchByDepartment;
    private DirectorySpinnerItems directorySpinnerItems;
    private String[] spinnerItems, spinnerItemValues;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return rootView;
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        spinner.setSelection(0);
        searchText.setText("");
    }

    public void initialize() {
        directorySpinnerItems = new DirectorySpinnerItems();
        spinnerItems = directorySpinnerItems.getSpinnerItems();
        spinnerItemValues = directorySpinnerItems.getSpinnerItemValues();

        spinner = (Spinner) rootView.findViewById(R.id.spinner_directory);
        searchText = (EditText) rootView.findViewById(R.id.edittext_directory_search);
        buttonText = (Button) rootView.findViewById(R.id.button_directory_searchtext);
        buttonText.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            if (isNetworkAvailable()) {
                searchByDepartment = new SearchByDepartment(spinnerItemValues[i], getActivity());
                searchByDepartment.execute();
            } else {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
