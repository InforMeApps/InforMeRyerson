/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ca.informeapps.informeryerson.R;

/**
 * Created by Shahar on 2014-08-08.
 */
public class ScheduleListDateAdapter extends ArrayAdapter<String> {


    private final Activity context;
    private final String[] Day;
    private final String[] Month;
    TextView MonthText, DayText;

    public ScheduleListDateAdapter(Activity context, String[] Day, String[] Month) {
        super(context, R.layout.layout_list_myschedule, Day);
        this.context = context;
        this.Day = Day;
        this.Month = Month;
    }


    @Override
    public int getCount() {

        return 180;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_list_myschedule, null, true);

        MonthText = (TextView) rowView.findViewById(R.id.TextView_MonthName_MMM);
        DayText = (TextView) rowView.findViewById(R.id.TextView_DateNumber);

        MonthText.setText(Month[i] + "");
        DayText.setText(Day[i]);


        return rowView;
    }


}
