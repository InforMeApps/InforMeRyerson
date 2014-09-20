/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;



public class Notifications {

    private String title, description;
    private int tYear, tMonth, tDay, tHour, tMinute;

    public Notifications(String title, String description, int tMinute, int tHour, int tDay, int tMonth, int tYear) {
        this.title = title;
        this.description = description;
        this.tMinute = tMinute;
        this.tHour = tHour;
        this.tDay = tDay;
        this.tMonth = tMonth;
        this.tYear = tYear;
    }

    public void sendAlarmMangerNotification(Activity activity,Class<?> aClass) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        long seconds = Long.parseLong(sharedPreferences.getString("Reminders_default_value", "no selection"));

        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        int hours = (int) TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        int minute = (int) (TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60));


        Calendar notificationDate = Calendar.getInstance();
        notificationDate.setTimeInMillis(System.currentTimeMillis());

        notificationDate.set(tYear, tMonth, tDay-day, tHour-hours, tMinute-minute);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        Bundle args = new Bundle();
        args.putString("KEY_TITLE", title);
        args.putString("KEY_DESCRIPTION", description);
        Intent notificationIntent = new Intent(activity, aClass);//Receiver class for each notification
        notificationIntent.putExtra("TITLE_DESCRIPTION", args);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
        }
    }

}
