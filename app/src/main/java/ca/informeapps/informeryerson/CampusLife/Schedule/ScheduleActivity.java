/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.informeapps.informeryerson.R;


/**
 * Created by Tanmay on 2014-08-07.
 */
public class ScheduleActivity extends FragmentActivity {

    public Calendar[] Day;
    public String[] Days;
    public String[] MonthDays;
    private ListView listView;
    private String[] calendarID;
    private long[] timeMills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myschedule);


        Day = new Calendar[180];
        Days = new String[180];
        MonthDays = new String[180];
        timeMills = new long[180];


        for (int z = 0; z < 180; z++) {
            Day[z] = shiftedCalender(Calendar.getInstance(), z);
            Days[z] = shiftDate(Calendar.getInstance(), z);
            MonthDays[z] = whatMonth(Day[z]);
            timeMills[z] = shiftedCalender(Calendar.getInstance(), z).getTimeInMillis();
        }


        /*
        //
        // GETTING STUFF FROM CALENDAR START
        //
        ContentResolver contentResolver = getContentResolver();
        final Cursor cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                (new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME}), null, null, null);

        while (cursor.moveToNext()) {
            final String _id = cursor.getString(0);
            final String displayName = cursor.getString(1);


            int stringLength = displayName.length();
            String output = displayName.substring(stringLength - 10);
            //Log.d("Cursor", output);

            if (output.equals("ryerson.ca")) {
                //Log.d("Cursor", "true");
                calendarID = new String[]{_id};
            }
        }

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2014, Calendar.SEPTEMBER, 2, 8, 0);
        long startMills = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(2014, Calendar.SEPTEMBER, 2, 20, 0);
        long endMills = endTime.getTimeInMillis();

        ContentUris.appendId(builder, startMills);
        ContentUris.appendId(builder, endMills);

        Cursor eventCursor = contentResolver.query(builder.build(), new String[]{CalendarContract.Instances.TITLE,
                        CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.DESCRIPTION},
                CalendarContract.Instances.CALENDAR_ID + " = ?", calendarID, null);

        while (eventCursor.moveToNext()) {
            final String title = eventCursor.getString(0);
            final Date begin = new Date(eventCursor.getLong(1));
            final Date end = new Date(eventCursor.getLong(2));
            final String description = eventCursor.getString(3);

            Log.d("Cursor", "Title: " + title + "\tDescription: " + description + "\tBegin: " + begin + "\tEnd: " + end);
        }

        //
        // GETTING STUFF FROM CALENDAR END
        //
        */


        final ScheduleListDateAdapter adapter = new ScheduleListDateAdapter(this, Days, MonthDays);
        listView = (ListView) findViewById(R.id.listview_myschedule_date);


        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelection(i);
            }

        });

        onItemSelection(0);

    }


    public Calendar shiftedCalender(Calendar c, int Shift) {
        Calendar calendar = c;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        calendar.add(Calendar.DAY_OF_YEAR, Shift);
        return calendar;
    }

    public String shiftDate(Calendar c, int Shift) {
        Calendar calendar = c;
        calendar.add(Calendar.DAY_OF_MONTH, Shift);
        return (new SimpleDateFormat("EEE").format(calendar.getTime()));
    }

    public String whatMonth(Calendar c) {
        return (new SimpleDateFormat("MMM, d").format(c.getTime()));


    }


    public void onItemSelection(int Pos) {
        Bundle bundle = new Bundle();
        bundle.putLong("timeMillsStart", timeMills[Pos] + 1);
        bundle.putLong("timeMillsEnd", timeMills[Pos + 1] - 1);

        Log.d("Stuff", timeMills[Pos] + " " + timeMills[Pos + 1]);
        ScheduleDetailFragment fragInfo = new ScheduleDetailFragment();
        fragInfo.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.content_frame_myschedule, fragInfo).commit();

    }
}
