/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Reminders;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ca.informeapps.informeryerson.R;

public class RemindersDetailFragment extends Fragment {

    private View rootView;
    private int clickPosition;
    private TextView title, time, description;
    private List<Reminder> reminders;
    private ReminderDatabaseHandler databaseHandler;
    private ImageView delete;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        clickPosition = args.getInt("KEY_CLICK_POSITION");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reminders_detail, container, false);

        databaseHandler = new ReminderDatabaseHandler(getActivity());
        reminders = databaseHandler.getAllReminders();

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setHomeButtonEnabled(true);
        setHasOptionsMenu(true);

        title = (TextView) rootView.findViewById(R.id.textview_reminder_detail_title);
        time = (TextView) rootView.findViewById(R.id.textview_reminder_detail_time);
        description = (TextView) rootView.findViewById(R.id.textview_reminder_detail_description);
        delete = (ImageView) rootView.findViewById(R.id.imageview_newreminder_delete);
        delete.setVisibility(View.GONE);

        Reminder reminder = reminders.get(clickPosition);

        Date date = new Date(reminder.get_year(), reminder.get_month(), reminder.get_day(), reminder.get_hour(), reminder.get_minute());

        title.setText(reminder.get_title());
        description.setText(reminder.get_description());

        if(reminder.get_month()!=10000)
        time.setText(reminder.get_day() + " " + new SimpleDateFormat("MMMM").format(date) + " at " + new SimpleDateFormat("hh:mm aa").format(date));
        else
        time.setText("No reminder date set");

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.reminders_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reminders_delete:
                deleteReminderPressed();
                break;
            //case R.id.action_reminders_edit:
               // editReminderPressed();
               // break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteReminderPressed() {
        Reminder r = reminders.get(clickPosition);
        databaseHandler.deleteReminder(r);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_rotate);
        animation.setInterpolator(getActivity(), android.R.anim.overshoot_interpolator);
        delete.setAnimation(animation);
        delete.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }, 700);
    }

    private void editReminderPressed() {
        Fragment fragment = new ReminderEditFragment();
        Bundle args = new Bundle();
        args.putInt("KEY_CLICK_POSITION", clickPosition);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_up_enter, R.anim.alpha_out, R.anim.alpha_in, R.anim.slide_down_exit)
                .replace(R.id.content_frame_reminders, fragment)
                .addToBackStack(null)
                .commit();
    }
}
