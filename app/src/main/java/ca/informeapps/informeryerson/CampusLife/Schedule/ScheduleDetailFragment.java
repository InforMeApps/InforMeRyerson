/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import ca.informeapps.informeryerson.R;

/**
 * Created by Tanmay on 2014-08-07.
 */
public class ScheduleDetailFragment extends Fragment {

    private Calendar[] allDate;
    private int clickPosition;
    private Cursor mCursor = null;
    private long timeMillsStart;
    private long timeMillsEnd;
    private String[] calendarID;
    private String textOutput = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("DETAIL FRAGMENT", "ONCREATE");

        Bundle args = getArguments();
        timeMillsStart = args.getLong("timeMillsStart") + 1;
        timeMillsEnd = args.getLong("timeMillsEnd") - 1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myschedule_detail, container, false);

        Log.d("DETAIL FRAGMENT", "ONCREATEVIEW");

        ContentResolver contentResolver = getActivity().getContentResolver();

        final Cursor calendarNamesCursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                (new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME}),
                null, null, null);

        while (calendarNamesCursor.moveToNext()) {
            final String _id = calendarNamesCursor.getString(0);
            final String displayName = calendarNamesCursor.getString(1);

            int stringLength = displayName.length();
            String output = displayName.substring(stringLength - 10);

            if (output.equals("ryerson.ca")) {
                calendarID = new String[]{_id};
            }
        }

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        ContentUris.appendId(builder, timeMillsStart);
        ContentUris.appendId(builder, timeMillsEnd);

        Cursor eventsCursor = contentResolver.query(builder.build(), new String[]{CalendarContract.Instances.TITLE,
                        CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.DESCRIPTION},
                CalendarContract.Instances.CALENDAR_ID + " = ?", calendarID, null);

        while (eventsCursor.moveToNext()) {
            final String title = eventsCursor.getString(0);
            final Date begin = new Date(eventsCursor.getLong(1));
            final Date end = new Date(eventsCursor.getLong(2));
            final String description = eventsCursor.getString(3);

            textOutput = textOutput + "Title: " + title + "\nDescription: " + description + "\nBegin: " + begin + "\nEnd: " + end + "\n";
            Log.d("OUTPUT", "Title: " + title + "\nDescription: " + description + "\nBegin: " + begin + "\nEnd: " + end + "\n");
        }

        TextView t = (TextView) rootView.findViewById(R.id.hello);
        t.setText(textOutput);


        return rootView;
    }


}
