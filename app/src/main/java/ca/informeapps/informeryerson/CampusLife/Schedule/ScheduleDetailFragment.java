/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
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

    Context con;
    private Calendar[] allDate;
    private int clickPosition;
    private Cursor mCursor = null;

    public ScheduleDetailFragment(Calendar[] date, Context con) {
        allDate = date;
        this.con = con;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        clickPosition = args.getInt("pos");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myschedule_detail, container, false);


        mCursor = con.getContentResolver().query(CalendarContract.Events.CONTENT_URI, new String[]{"_id", "title", "description", "dtstart", "dtend", "eventLocation"},
                null, null, null);

        mCursor.moveToFirst();

        String[] CalNames = new String[mCursor.getCount()];
        int[] CalIds = new int[mCursor.getCount()];
        Date[] sDate = new Date[mCursor.getCount()];
        Date[] eDate = new Date[mCursor.getCount()];
        long[] slong = new long[mCursor.getCount()];
        long[] elong = new long[mCursor.getCount()];
        int index1 = 0;
        int index2 = 0;
        String s = "";

        for (int i = 0; i < CalNames.length; i++) {

            CalIds[i] = mCursor.getInt(0);
            CalNames[i] = "" + mCursor.getString(1) + "\n" + mCursor.getString(2) + "\n" + mCursor.getString(3) + "\n" + mCursor.getString(4) + "\n" + mCursor.getString(5);
            sDate[i] = new Date(mCursor.getLong(3));
            slong[i] = sDate[i].getTime();
            eDate[i] = new Date(mCursor.getLong(4));
            elong[i] = eDate[i].getTime();
            mCursor.moveToNext();

            Date startTime = new Date();
            startTime = allDate[clickPosition].getTime();
            long lngStartTime = startTime.getTime();
            Calendar c = Calendar.getInstance();
            c.set(allDate[clickPosition].get(Calendar.YEAR), allDate[clickPosition].get(Calendar.MONTH), allDate[clickPosition].get(Calendar.DAY_OF_MONTH), 23, 59);
            Date endTime = c.getTime();
            long lngEndTime = endTime.getTime();

            if (slong[i] <= lngStartTime) {
                index1 = i + 1;

            }
            if (elong[i] >= lngEndTime) {
                index2 = i;
            }

        }
        mCursor.close();
        for (int y = index1; y < index2; y++) {
            s = s + CalNames[y];
        }

        TextView t = (TextView) rootView.findViewById(R.id.textView);
        t.setText(s + "");


        return rootView;
    }


}
