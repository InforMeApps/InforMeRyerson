/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Shahar on 2014-08-26.
 */



public class NewSchedule extends Activity {
    private DateAdapter adapter;
    private MonthAdapter madapter;

    HorizontalCalendarView horizontalCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newschedule);
        adapter = new DateAdapter(this);
        madapter = new MonthAdapter(this);
        horizontalCalendarView=(HorizontalCalendarView)findViewById(R.id.ss);
        horizontalCalendarView.setAdapter(adapter);
        horizontalCalendarView=(HorizontalCalendarView)findViewById(R.id.Month);
        horizontalCalendarView.setAdapter(madapter);


    }



   class DateAdapter extends BaseAdapter {

       private LayoutInflater inflater;
       String[] Dates;

       public DateAdapter(Context context) {
           inflater = LayoutInflater.from(context);
           Dates = new String[181];
           for(int x=0; x<181; x++)
           {
               Dates[x]= x+" ";
           }

       }


       @Override
       public int getCount() {
           return Dates.length;
       }

       @Override
       public Object getItem(int position) {
           return Dates[position];
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {

           convertView = inflater.inflate(R.layout.layout_list_myschedule, parent, false);
           TextView t = (TextView) convertView.findViewById(R.id.TextView_Day_EEE);
           t.setText(Dates[position]);

           return convertView;


       }

   }

    class MonthAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        String[] Dates;

        public MonthAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            Dates = new String[181];


        }


        @Override
        public int getCount() {
            return Dates.length;
        }

        @Override
        public Object getItem(int position) {
            return Dates[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.layout_list_myschedule, parent, false);
            TextView t = (TextView) convertView.findViewById(R.id.TextView_Day_EEE);
            t.setText(horizontalCalendarView.getFirstVisiblePosition()+"");

            return convertView;


        }

    }



}
