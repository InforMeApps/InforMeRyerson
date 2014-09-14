/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Reminders;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.informeapps.informeryerson.R;

/**
 * Created by Shahar on 2014-09-13.
 */
public class ReminderEditFragment extends Fragment  {

    private List<Reminder> reminders;
    private int clickPosition;
    private int tHour,tMinute,tDay,tMonth,tYear;
    EditText title, des;
    int a = 0,b=0,c=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        clickPosition = args.getInt("KEY_CLICK_POSITION");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
        final View rootView= inflater.inflate(R.layout.fragment_reminders_add, container, false);

        final ReminderDatabaseHandler databaseHandler = new ReminderDatabaseHandler(getActivity());
        reminders = databaseHandler.getAllReminders();
        final Reminder reminder = reminders.get(clickPosition);

        title = (EditText)rootView.findViewById(R.id.edittext_newreminder_title);
        des = (EditText)rootView.findViewById(R.id.edittext_newreminder_description);
        Button done =(Button)rootView.findViewById(R.id.button_newreminder_add);
        Button cancel =(Button)rootView.findViewById(R.id.button_newreminder_cancel);
        final Button date = (Button) rootView.findViewById(R.id.button_newreminder_date);
        final Button time = (Button) rootView.findViewById(R.id.button_newreminder_time);
        if(reminder.get_month()!=10000)
        {
            time.setText(new SimpleDateFormat("h:mm:aa").format(new Date(0,reminder.get_month(),reminder.get_day(),reminder.get_hour(),reminder.get_minute())));
            date.setText(new SimpleDateFormat("MMM dd").format(new Date(0,reminder.get_month(),reminder.get_day())));
            a=reminder.get_year(); b=reminder.get_month(); c=reminder.get_day();


        }
        else
        {
            time.setText("Select Time");
            date.setText("Select Date");
            Calendar d = Calendar.getInstance();
            a=d.get(d.YEAR);b=d.get(d.MONTH);c=d.get(d.DAY_OF_MONTH);

        }


        title.setText(reminder.get_title());
        des.setText(reminder.get_description());
        done.setText("Done");


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!date.getText().toString().equals("Select Date") || !time.getText().toString().equals("Select Time")) {
                    reminder.set_month(tMonth);
                    processNotification();
                }
                else
                {
                    reminder.set_month(10000);
                }

                reminder.set_title(title.getText().toString());
                reminder.set_description(des.getText().toString());
                reminder.set_day(tDay);
                reminder.set_hour(tHour);
                reminder.set_year(tYear);
                reminder.set_minute(tMinute);
                databaseHandler.editReminder(reminder);
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
                ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview_newreminder_done);

                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_rotate);
                animation.setInterpolator(getActivity(), android.R.anim.overshoot_interpolator);
                imageView.setAnimation(animation);
                imageView.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }, 700);

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadialTimePickerDialog radialTimePickerDialog = RadialTimePickerDialog
                        .newInstance(new RadialTimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {
                                Date date1 = new Date(1, 1, 1, i, i2);
                                time.setText(new SimpleDateFormat("hh:mm aa").format(date1));
                                tHour = i;
                                tMinute = i2;
                            }
                        }, reminder.get_hour(),reminder.get_minute(), false);
                radialTimePickerDialog.show(getActivity().getSupportFragmentManager(), "TIME_PICKER");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                        .newInstance(new CalendarDatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int i, int i2, int i3) {
                                date.setText(new SimpleDateFormat("MMM dd").format(new Date(i, i2, i3)));
                                tDay = i3;
                                tMonth = i2;
                                tYear = i;
                            }
                        },a,b,c );
                calendarDatePickerDialog.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        return rootView;

    }

    private void processNotification() {
        Calendar notificationDate = Calendar.getInstance();
        notificationDate.setTimeInMillis(System.currentTimeMillis());
        notificationDate.set(tYear, tMonth, tDay, tHour, tMinute);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(getActivity(), RemindersReceiver.class);
        Bundle args = new Bundle();
        args.putString("KEY_TITLE", title.getText().toString());
        args.putString("KEY_DESCRIPTION", des.getText().toString());
        notificationIntent.putExtra("TITLE_DESCRIPTION", args);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
        }
    }


}
