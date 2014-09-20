/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Reminders;

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
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ca.informeapps.informeryerson.Notifications;
import ca.informeapps.informeryerson.R;

public class ReminderAddNewFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button date, time, cancel, add;
    private EditText title, description;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int tYear, tMonth, tDay, tHour, tMinute;
    private ReminderDatabaseHandler databaseHandler;
    private ImageView done;
    private boolean pressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reminders_add, container, false);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
        initialize();

        done.setVisibility(View.GONE);
        return rootView;
    }

    private void initialize() {

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        date = (Button) rootView.findViewById(R.id.button_newreminder_date);
        time = (Button) rootView.findViewById(R.id.button_newreminder_time);
        cancel = (Button) rootView.findViewById(R.id.button_newreminder_cancel);
        add = (Button) rootView.findViewById(R.id.button_newreminder_add);
        title = (EditText) rootView.findViewById(R.id.edittext_newreminder_title);
        description = (EditText) rootView.findViewById(R.id.edittext_newreminder_description);
        done = (ImageView) rootView.findViewById(R.id.imageview_newreminder_done);

        date.setOnClickListener(this);
        time.setOnClickListener(this);
        cancel.setOnClickListener(this);
        add.setOnClickListener(this);

        databaseHandler = new ReminderDatabaseHandler(getActivity());
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_newreminder_date:
                datePickerPressed();
                break;
            case R.id.button_newreminder_time:
                timePickerPressed();
                break;
            case R.id.button_newreminder_cancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.button_newreminder_add:
                if (title.getText().toString().length() > 0 || description.getText().toString().length() > 0)
                    addButtonPressed(true);
                else {
                    Toast.makeText(getActivity(), "Enter a description or title", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!pressed)
        {
            if (title.getText().toString().length() > 0 || description.getText().toString().length() > 0)
                addButtonPressed(false);
            else
                Toast.makeText(getActivity(),"Empty reminder not created",Toast.LENGTH_SHORT).show();

        }

    }


    private void addButtonPressed(boolean popBack) {
            pressed = true;
            add.setClickable(false);
            if (!date.getText().toString().equals("Select Date") || !time.getText().toString().equals("Select Time")) {
                databaseHandler.addReminder(new Reminder(title.getText().toString(), description.getText().toString(), tDay, tMonth, tYear, tHour, tMinute));
                Notifications notifications = new Notifications(title.getText().toString(),description.getText().toString(),tMinute, tHour, tDay, tMonth, tYear);
                notifications.sendAlarmMangerNotification(getActivity(),RemindersReceiver.class);
            }
            else
            {
                databaseHandler.addReminder(new Reminder(title.getText().toString(), description.getText().toString(), tDay, 10000, tYear, tHour, tMinute));

            }

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_rotate);
            animation.setInterpolator(getActivity(), android.R.anim.overshoot_interpolator);
            done.setAnimation(animation);
            done.setVisibility(View.VISIBLE);

            if (popBack)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }, 700);


    }


    private void timePickerPressed() {
        RadialTimePickerDialog radialTimePickerDialog = RadialTimePickerDialog
                .newInstance(new RadialTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {
                        Date date1 = new Date(2014, 3, 5, i, i2);
                        DateFormat format = new SimpleDateFormat("hh:mm aa");
                        String timeFormatted = format.format(date1);
                        time.setText(timeFormatted);
                        tHour = i;
                        tMinute = i2;
                    }
                }, mHour, mMinute, false);
        radialTimePickerDialog.show(getActivity().getSupportFragmentManager(), "TIME_PICKER");
    }

    private void datePickerPressed() {
        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(new CalendarDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int i, int i2, int i3) {
                        Date date1 = new Date(i, i2, i3);
                        DateFormat format = new SimpleDateFormat("MMM dd");
                        String dateFormatted = format.format(date1);
                        date.setText(dateFormatted);
                        tDay = i3;
                        tMonth = i2;
                        tYear = i;
                    }
                }, mYear, mMonth, mDay);
        calendarDatePickerDialog.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");

    }
}
