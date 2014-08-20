/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.informeapps.informeryerson.R;

public class ScheduleDetailFragment extends Fragment {

    private ListView listView;
    private ScheduleDetailListAdapter adapter;
    private long timeMillsStart;
    private long timeMillsEnd;
    private String[] calendarID;
    private List<Event> eventList;
    private boolean calendarFound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        timeMillsStart = args.getLong("timeMillsStart") + 1;
        timeMillsEnd = args.getLong("timeMillsEnd") - 1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myschedule_detail, container, false);

        buildCalendarCursor();

        if (calendarFound) {
            listView = (ListView) rootView.findViewById(R.id.listview_myschedule_detail);
            adapter = new ScheduleDetailListAdapter();
            listView.setAdapter(adapter);
        } else {
            TextView textView = (TextView) rootView.findViewById(R.id.textview_schedule_error);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void buildCalendarCursor() {

        ContentResolver contentResolver = getActivity().getContentResolver();

        //Getting the ids and names of all the calendars available on the device
        final Cursor calendarNamesCursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                (new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME}),
                null, null, null);

        //finding ryerson.ca calendar
        while (calendarNamesCursor.moveToNext()) {
            final String _id = calendarNamesCursor.getString(0);
            final String displayName = calendarNamesCursor.getString(1);

            int stringLength = displayName.length();
            String output = displayName.substring(stringLength - 10);

            if (output.equals("ryerson.ca")) {
                calendarID = new String[]{_id};
                calendarFound = true;
            }
        }

        if (calendarFound) {
            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

            ContentUris.appendId(builder, timeMillsStart);
            ContentUris.appendId(builder, timeMillsEnd);


            //getting the begin time and the description of the calendars for the specific date
            Cursor eventsCursor = contentResolver.query(builder.build(), new String[]{CalendarContract.Instances.BEGIN,
                            CalendarContract.Instances.DESCRIPTION},
                    CalendarContract.Instances.CALENDAR_ID + " = ?", calendarID, null);

            eventList = new ArrayList<Event>();


            //adding the receive data to the list for storage
            while (eventsCursor.moveToNext()) {

                Event event = new Event();

                String[] description = eventsCursor.getString(1).split(System.getProperty("line.separator"));

                event.setTime(description[3]);
                event.setCourse(description[0]);
                event.setCourseName(description[1]);
                event.setLocation(description[4]);
                if (description.length == 7) {
                    event.setInstructor(description[6]);
                } else {
                    event.setInstructor(description[5]);
                }
                event.setLecture(description[2]);
                event.setBegin(eventsCursor.getLong(0));

                eventList.add(event);

            }

            //sort the list
            sortEventList();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ryerson Calendar not found");
            builder.setMessage("Please click the Instructions button to set up the Ryerson calendar on your phone");
            builder.setPositiveButton("Instructions", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String url = "http://www.ryerson.ca/google/usingapps/viamobile/android/androidnativeappsnew.html";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(url));
                    startActivity(browserIntent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }

    private void sortEventList() {

        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event event2) {
                return (int) (event.getBegin() - event2.getBegin());
            }
        });

    }

    public class ScheduleDetailListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return eventList.size();
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

            view = getActivity().getLayoutInflater().inflate(R.layout.layout_list_schedule_detail, null);

            Event event = eventList.get(i);

            TextView time = (TextView) view.findViewById(R.id.textview_schedule_list_detail_time);
            TextView course = (TextView) view.findViewById(R.id.textview_schedule_list_detail_course);
            TextView courseName = (TextView) view.findViewById(R.id.textview_schedule_list_detail_coursename);
            TextView location = (TextView) view.findViewById(R.id.textview_schedule_list_detail_location);
            TextView instructor = (TextView) view.findViewById(R.id.textview_schedule_list_detail_instructor);
            TextView lecture = (TextView) view.findViewById(R.id.textview_schedule_list_detail_lecture);

            time.setText(event.getTime());
            course.setText(event.getCourse());
            courseName.setText(event.getCourseName());
            location.setText(event.getLocation());
            instructor.setText("Instructor: " + event.getInstructor());
            lecture.setText(event.getLecture());

            return view;
        }
    }

}
