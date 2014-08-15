/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Reminders;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import ca.informeapps.informeryerson.R;

public class RemindersReceiver extends BroadcastReceiver {

    private Bundle bundle;
    private String title, description;
    private int id = 1;
    private PendingIntent pendingIntent;
    private long[] pattern = {50, 50, 250, 50};

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getBundleExtra("TITLE_DESCRIPTION");
        title = bundle.getString("KEY_TITLE");
        description = bundle.getString("KEY_DESCRIPTION");

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_today)
                .setContentTitle(title)
                .setContentText(description)
                .setSound(soundUri)
                .setVibrate(pattern);

        Intent resultIntent = new Intent(context, RemindersActivity.class);

        if (Build.VERSION.SDK_INT > 15) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(RemindersActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, mBuilder.build());

    }
}
