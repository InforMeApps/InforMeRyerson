package ca.informeapps.informeryerson.CampusLife.Reminders;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ca.informeapps.informeryerson.Misc.FloatingActionButton;
import ca.informeapps.informeryerson.Misc.SwipeDismissListViewTouchListener;
import ca.informeapps.informeryerson.R;

public class RemindersFragments extends Fragment implements AdapterView.OnItemClickListener {

    //initializing variabls
    private static FloatingActionButton floatingActionButton;
    private View rootView;
    private ListView listView;
    private ReminderDatabaseHandler databaseHandler;
    private ReminderListAdapter adapter;
    private List<Reminder> reminders;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_reminders, container, false);//setting layout
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);//for going back

        //handles database
        databaseHandler = new ReminderDatabaseHandler(getActivity());
        reminders = databaseHandler.getAllReminders();


        listView = (ListView) rootView.findViewById(R.id.listview_remiders);
        adapter = new ReminderListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        if (adapter.getCount() == 0) {
            listView.setVisibility(View.GONE);
        }
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    databaseHandler.deleteReminder(reminders.get(position));
                                    reminders.remove(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        //checks for scrolling vs swiping
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());

        initializeFloatingActionButton();

        return rootView;
    }

    private void initializeFloatingActionButton() {
        floatingActionButton = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_add))
                .withButtonColor(Color.parseColor("#e91e63"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();

        Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_enter);
        slideUp.setInterpolator(getActivity(), android.R.anim.overshoot_interpolator);
        floatingActionButton.setAnimation(slideUp);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                //vibrator.vibrate((long) 5);
                Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_exit);
                slideDown.setInterpolator(getActivity(), android.R.anim.anticipate_interpolator);
                floatingActionButton.setAnimation(slideDown);
                floatingActionButton.setVisibility(View.GONE);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_up_enter, R.anim.alpha_out, R.anim.alpha_in, R.anim.slide_down_exit)
                        .replace(R.id.content_frame_reminders, new ReminderAddNewFragment(), "NEW_REMINDER_FRAGMENT")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_exit);
        slideDown.setInterpolator(getActivity(), android.R.anim.anticipate_interpolator);
        floatingActionButton.setAnimation(slideDown);
        floatingActionButton.setVisibility(View.GONE);

        Fragment fragment = new RemindersDetailFragment(reminders, databaseHandler);
        Bundle args = new Bundle();
        args.putInt("KEY_CLICK_POSITION", i);
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_up_enter, R.anim.alpha_out, R.anim.alpha_in, R.anim.slide_down_exit)
                .replace(R.id.content_frame_reminders, fragment)
                .addToBackStack(null)
                .commit();
    }

    public String getTime(int mHour, int mMinutes) {
        String hour = null, minutes = null;
        if (mHour <= 12) {
            hour = mHour + "";
            minutes = mMinutes + "";
            if (mHour == 0) {
                mHour = 12;
                hour = mHour + "";
            }
            if (mMinutes < 10) {
                minutes = "0" + mMinutes;
            }

            return hour + ":" + minutes + "AM";
        } else {
            hour = mHour + "";
            minutes = mMinutes + "";
            mHour = mHour - 12;
            if (mHour < 10) {
                hour = "0" + mHour;
            }
            if (mMinutes < 10) {
                minutes = "0" + mMinutes;
            }

            return hour + ":" + minutes + "PM";
        }
    }

    public String getMonth(int inputMonth) {
        String month = null;
        if (inputMonth == 0)
            month = "JAN";
        else if (inputMonth == 1)
            month = "FEB";
        else if (inputMonth == 2)
            month = "MAR";
        else if (inputMonth == 3)
            month = "APR";
        else if (inputMonth == 4)
            month = "MAY";
        else if (inputMonth == 5)
            month = "JUN";
        else if (inputMonth == 6)
            month = "JUL";
        else if (inputMonth == 7)
            month = "AUG";
        else if (inputMonth == 8)
            month = "SEP";
        else if (inputMonth == 9)
            month = "OCT";
        else if (inputMonth == 10)
            month = "NOV";
        else if (inputMonth == 11)
            month = "DEC";

        return month;
    }

    private class ReminderListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public ReminderListAdapter() {
            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            Log.d("GETCOUNT", "" + databaseHandler.getRemindersCount());
            return databaseHandler.getRemindersCount();
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

            view = inflater.inflate(R.layout.layout_list_reminders, null);

            TextView day = (TextView) view.findViewById(R.id.textview_reminder_list_day);
            TextView month = (TextView) view.findViewById(R.id.textview_reminder_list_month);
            TextView time = (TextView) view.findViewById(R.id.textview_reminder_list_time);
            TextView title = (TextView) view.findViewById(R.id.textview_reminder_list_title);
            TextView description = (TextView) view.findViewById(R.id.textview_reminder_list_description);

            Reminder reminder = reminders.get(i);

            int mDay = reminder.get_day();
            int mMonth = reminder.get_month();
            int mHour = reminder.get_hour();
            int mMinutes = reminder.get_minute();
            String mTitle = reminder.get_title();
            String mDescription = reminder.get_description();
            String mTime = getTime(mHour, mMinutes);
            String monthText = getMonth(mMonth);

            Log.d("TIME", mHour + "");

            day.setText(mDay + "");
            month.setText(monthText);
            time.setText(mTime);
            title.setText(mTitle);
            description.setText(mDescription);

            return view;
        }
    }
}