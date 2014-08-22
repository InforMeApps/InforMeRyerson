/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Schedule;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ca.informeapps.informeryerson.Misc.FloatingActionButton;
import ca.informeapps.informeryerson.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ScheduleActivity extends FragmentActivity {

    ViewHolder dayTextHolder;
    private StickyListHeadersListView listView;
    private ScheduleDateListAdapter adapter;
    private long[] timeMills;
    private int clickPosition = 0;
    private static final int MAX_VIEWS = 6;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScheduleDetailFragment scheduleDetailFragment= new ScheduleDetailFragment();

        if(checkCalender()) {
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
                    .withDrawable(getResources().getDrawable(R.drawable.ic_action_today))
                    .withButtonColor(Color.parseColor("#e91e63"))
                    .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                    .withMargins(0, 0, 10, 10)
                    .create();


            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemSelection(0);
                    listView.smoothScrollToPositionFromTop(0, 0, 150);
                }
            });
            onItemSelection(0);
        }
        else if(!checkCalender())
        {
            setContentView(R.layout.walkthrough_ryerson_email);
            mViewPager = (ViewPager) findViewById(R.id.view_pager);
            mViewPager.setAdapter(new WalkthroughPagerAdapter());
            mViewPager.setOnPageChangeListener(new WalkthroughPageChangeListener());
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        Crashlytics.start(this);
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

            for (int x = 0; x < Dates.length; x++) {
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







    class WalkthroughPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return MAX_VIEWS;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            Log.e("walkthrough", "instantiateItem(" + position + ");");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewz = inflater.inflate(R.layout.walkthrough_single_view, null);
            ImageView imageView = (ImageView) viewz.findViewById(R.id.Walkthroug_image);
            TextView textView = (TextView)viewz.findViewById(R.id.Walkthroug_text);
            Button button=(Button)viewz.findViewById(R.id.Walkthroug_Buttn);

            switch(position) {
                case 0:
                    textView.setText("OH NOES LOOKS LIKE YOU DONT HAVE RYERSON EMAIL ON YOUR DEVICE\nFOLLOW THE TUTORIAL TO BE ENLIGHTENED");
                    imageView.setImageResource(R.drawable.error_cat);
                    break;

                case 1:
                    textView.setText("Log into your my.ryerson account and click the Apps on the top");
                    imageView.setImageResource(R.drawable.step1);
                    break;

                case 2:
                    textView.setText("Click on \"Activate Google Token\"");
                    imageView.setImageResource(R.drawable.step2);
                    break;

                case 3:
                    textView.setText("Click on Activate");
                    imageView.setImageResource(R.drawable.step3);
                    break;

                case 4:
                    textView.setText("Next on Your Device Go to Settings and Add An Existing Google Account");
                    imageView.setImageResource(R.drawable.step4);
                    break;

                case 5:
                    textView.setText("Sign in Using Ryerson Email and Use The Token As The Password\nRemember to Have Ryerson Calendar Synced!");
                    imageView.setImageResource(R.drawable.step7);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse("https://my.ryerson.ca/render.userLayoutRootNode.uP"));
                            startActivity(browserIntent);
                        }
                    });
                    break;
            }

            ((ViewPager) container).addView(viewz, 0);
            return viewz;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
        }
    }


    class WalkthroughPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // Here is where you should show change the view of page indicator
            switch(position) {

                case MAX_VIEWS - 1:
                    break;

                default:

            }

        }

    }

    private boolean checkCalender()
    {
        boolean calendarFound = false;
        ContentResolver contentResolver = this.getContentResolver();

        //Getting the ids and names of all the calendars available on the device
        final Cursor calendarNamesCursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                (new String[]{ CalendarContract.Calendars.CALENDAR_DISPLAY_NAME}),
                null, null, null);

        //finding ryerson.ca calendar
        while (calendarNamesCursor.moveToNext()) {

            final String displayName = calendarNamesCursor.getString(0);
            String output = "";

            int stringLength = displayName.length();
            if (stringLength > 9) {
                output = displayName.substring(stringLength - 10);
            }

            if (output.equals("ryerson.ca")) {
                calendarFound = true;
            }
        }
        return calendarFound;
    }
}
