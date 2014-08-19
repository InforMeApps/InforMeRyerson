/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.informeapps.informeryerson.R;

/**
 * Created by Tanmay on 2014-08-18.
 */
public class ScheduleActivity extends FragmentActivity {

    private ViewPager viewPager;
    private String[] Months;
    private String[][] Dates;
    private long[] timeMills;
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myschedule);

        fillDatesArray();

        timeMills = new long[140];
        for (int x = 0; x < 140; x++) {
            timeMills[x] = shiftCalender(Calendar.getInstance(), offset).getTimeInMillis();
            offset++;
        }

        viewPager = (ViewPager) findViewById(R.id.pager_schedule_dates);
        viewPager.setAdapter(new ScheduleSlideAdapter(getSupportFragmentManager()));
    }

    private void changeMonthText(int i) {
        TextView monthText = (TextView) findViewById(R.id.textview_schedule_month);
        monthText.setText(Months[i]);
    }

    private void fillDatesArray() {
        Dates = new String[7][20];
        Months = new String[20];

        boolean foundSunday = false;
        Calendar sunday = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        sunday.set(Calendar.HOUR_OF_DAY, 0);
        sunday.set(Calendar.MINUTE, 0);
        sunday.set(Calendar.SECOND, 0);

        final long DAY_IN_MILLS = 1000 * 60 * 60 * 24;

        int i = (int) -((today.getTimeInMillis() - sunday.getTimeInMillis()) / DAY_IN_MILLS);

        offset = i;

        for (int j = 0; j < 20; j++) {

            Months[j] = shiftedCalender(Calendar.getInstance(), i).split(" ")[0];

            for (int z = 0; z < 7; z++) {
                Dates[z][j] = shiftedCalender(Calendar.getInstance(), i).split(" ")[1];
                i++;
            }
        }
    }

    public Calendar shiftCalender(Calendar c, int Shift) {
        Calendar calendar = c;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        calendar.add(Calendar.DAY_OF_YEAR, Shift);
        return calendar;
    }

    public String shiftedCalender(Calendar c, int Shift) {
        Calendar calendar = c;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        calendar.add(Calendar.DAY_OF_MONTH, Shift);
        return (new SimpleDateFormat("MMMM dd").format(calendar.getTime()));
    }

    public String[] Day(String s) {
        String p[];
        p = s.split(" ");
        return p;
    }

    public static class ScheduleWeekViewFragment extends Fragment implements View.OnClickListener {

        private int pagerPosition;
        private long[] timeMills;
        private String[][] dates;
        private TextView sundayTextV, mondayTextV, tuesdayTextV, wednesdayTextV, thursdayTextV, fridayTextV, saturdayTextV;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle b = getArguments();
            timeMills = b.getLongArray("KEY_MILLSARRAY");
            pagerPosition = b.getInt("KEY_POSITION");
            dates = (String[][]) b.getSerializable("KEY_ARRAY");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule_viewpager, container, false);

            initialize(rootView);
            setTextDates();

            return rootView;
        }

        public void initialize(View rootView) {

            sundayTextV = (TextView) rootView.findViewById(R.id.textview_schedule_viewpager_sunday);
            mondayTextV = (TextView) rootView.findViewById(R.id.textview_schedule_viewpager_monday);
            tuesdayTextV = (TextView) rootView.findViewById(R.id.textview_schedule_viewpager_tuesday);
            wednesdayTextV = (TextView) rootView.findViewById(R.id.textview_schedule_viewpager_wednesday);
            thursdayTextV = (TextView) rootView.findViewById(R.id.textview_schedule_viewpager_thursday);
            fridayTextV = (TextView) rootView.findViewById(R.id.textview_schedule_viewpager_friday);
            saturdayTextV = (TextView) rootView.findViewById(R.id.textview_schedule_viewpager_saturday);

            sundayTextV.setOnClickListener(this);
            mondayTextV.setOnClickListener(this);
            tuesdayTextV.setOnClickListener(this);
            wednesdayTextV.setOnClickListener(this);
            thursdayTextV.setOnClickListener(this);
            fridayTextV.setOnClickListener(this);
            saturdayTextV.setOnClickListener(this);

        }

        private void setTextDates() {
            for (int i = 0; i < 7; i++) {
                String text = dates[i][pagerPosition];
                switch (i) {
                    case 0:
                        sundayTextV.setText(text);
                        break;
                    case 1:
                        mondayTextV.setText(text);
                        break;
                    case 2:
                        tuesdayTextV.setText(text);
                        break;
                    case 3:
                        wednesdayTextV.setText(text);
                        break;
                    case 4:
                        thursdayTextV.setText(text);
                        break;
                    case 5:
                        fridayTextV.setText(text);
                        break;
                    case 6:
                        saturdayTextV.setText(text);
                        break;
                }
            }
        }

        @Override
        public void onClick(View view) {

            Log.d("CLICk", "CLICK");
            int clickPos = 0;

            switch (view.getId()) {
                case R.id.textview_schedule_viewpager_sunday:
                    clickPos = 0;
                    break;
                case R.id.textview_schedule_viewpager_monday:
                    clickPos = 1;
                    break;
                case R.id.textview_schedule_viewpager_tuesday:
                    clickPos = 2;
                    break;
                case R.id.textview_schedule_viewpager_wednesday:
                    clickPos = 3;
                    break;
                case R.id.textview_schedule_viewpager_thursday:
                    clickPos = 4;
                    break;
                case R.id.textview_schedule_viewpager_friday:
                    clickPos = 5;
                    break;
                case R.id.textview_schedule_viewpager_saturday:
                    clickPos = 6;
                    break;
            }

            int arrayPosition = (pagerPosition * 7) + clickPos;
            Bundle b = new Bundle();
            b.putLong("timeMillsStart", timeMills[arrayPosition]);
            b.putLong("timeMillsEnd", timeMills[arrayPosition + 1]);


            Log.d("CLICk", "CLICK2" + arrayPosition);
            ScheduleDetailFragment fragment = new ScheduleDetailFragment();
            fragment.setArguments(b);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_myschedule, fragment).commit();
        }
    }

    private class ScheduleSlideAdapter extends FragmentStatePagerAdapter {

        public ScheduleSlideAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int i) {

            changeMonthText(i);

            Fragment fragment = new ScheduleWeekViewFragment();
            Bundle args = new Bundle();
            args.putInt("KEY_POSITION", i);
            args.putLongArray("KEY_MILLSARRAY", timeMills);
            args.putSerializable("KEY_ARRAY", Dates);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 20;
        }
    }
}
