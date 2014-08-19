/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.informeapps.informeryerson.Misc.FloatingActionButton;
import ca.informeapps.informeryerson.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * Created by Tanmay on 2014-08-07.
 */
public class ScheduleActivity extends FragmentActivity {

    private StickyListHeadersListView listView;
    private ScheduleDateListAdapter adapter;
    private long[] timeMills;
    private int clickPosition = 0;
    ViewHolder dayTextHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myschedule);

        timeMills = new long[180];

        for (int z = 0; z < 180; z++) {

            timeMills[z] = shiftedCalender(Calendar.getInstance(), z).getTimeInMillis();
        }


        adapter = new ScheduleDateListAdapter(this);
        listView = (StickyListHeadersListView) findViewById(R.id.listview_myschedule_date);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelection(i);
                clickPosition = i;
                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton floatingActionButton;


        floatingActionButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_add))
                .withButtonColor(Color.parseColor("#e91e63"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 0, 0)
                .withButtonSize(65)
                .create();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemSelection(0);
                listView.smoothScrollToPositionFromTop(0,0,150);
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

    public void onItemSelection(int Pos) {
        Bundle bundle = new Bundle();
        bundle.putLong("timeMillsStart", timeMills[Pos] + 1);
        bundle.putLong("timeMillsEnd", timeMills[Pos + 1] - 1);

        Log.d("Stuff", timeMills[Pos] + " " + timeMills[Pos + 1]);
        ScheduleDetailFragment fragInfo = new ScheduleDetailFragment();
        fragInfo.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.content_frame_myschedule, fragInfo).commit();

    }

    class ScheduleDateListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private String[] Dates;
        private String[] monthHeader;
        private LayoutInflater inflater;

        public ScheduleDateListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            Dates = new String[180];
            monthHeader = new String[180];

            for (int x = 0; x < 180; x++) {
                Dates[x] = Day(shiftedCalender(Calendar.getInstance(), x))[1] + ", " + Day(shiftedCalender(Calendar.getInstance(), x))[2];
                monthHeader[x] = (shiftedCalender(Calendar.getInstance(), x));
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

            if (convertView == null) {
                dayTextHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.layout_list_myschedule, parent, false);
                dayTextHolder.text = (TextView) convertView.findViewById(R.id.TextView_Day_EEE);
                convertView.setTag(dayTextHolder);
            } else {
                dayTextHolder = (ViewHolder) convertView.getTag();
            }

            String dayText = Dates[position].substring(0, 3).toUpperCase();
            String dateText = Dates[position].substring(Dates[position].length() - 2);
            if (position == clickPosition) {
                dayTextHolder.text.setTypeface(null, Typeface.BOLD);
                if (Build.VERSION.SDK_INT >= 16) {
                    dayTextHolder.text.setHasTransientState(true);
                }
            }
            dayTextHolder.text.setText(dayText + "\n" + dateText);


            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = inflater.inflate(R.layout.layout_list_schedule_datepicker_stickyheader, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.textview_schedule_list_header);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            //set header text as first char in name
            String headerText = "" + Day(monthHeader[position])[0];
            holder.text.setText(headerText);
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            //return the first character of the country as ID because this is what headers are based upon
            return monthHeader[position].subSequence(0, 1).charAt(0);
        }

        public String shiftedCalender(Calendar c, int Shift) {
            Calendar calendar = c;
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            calendar.add(Calendar.DAY_OF_MONTH, Shift);
            return (new SimpleDateFormat("MMMM EEEE dd").format(calendar.getTime()));
        }

        public String[] Day(String s) {
            String p[];
            p = s.split(" ");
            return p;
        }
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }

}
