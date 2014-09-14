/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "remindersManager";
    private static final String TABLE_REMINDERS = "reminders";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DAY = "day";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTES = "minutes";
    private static final String KEY_MILLS = "milliseconds";

    public ReminderDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DAY + " INTEGER,"
                + KEY_MONTH + " INTEGER,"
                + KEY_YEAR + " INTEGER,"
                + KEY_HOUR + " INTEGER,"
                + KEY_MINUTES + " INTEGER,"
                + KEY_MILLS + " INTEGER" + ")";

        sqLiteDatabase.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        onCreate(sqLiteDatabase);
    }

    public void addReminder(Reminder reminder) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, reminder.get_title());
        values.put(KEY_DESCRIPTION, reminder.get_description());
        values.put(KEY_DAY, reminder.get_day());
        values.put(KEY_MONTH, reminder.get_month());
        values.put(KEY_YEAR, reminder.get_year());
        values.put(KEY_HOUR, reminder.get_hour());
        values.put(KEY_MINUTES, reminder.get_minute());

        Calendar dateTime = Calendar.getInstance();
        dateTime.set(reminder.get_year(), reminder.get_month(), reminder.get_day(), reminder.get_hour(), reminder.get_minute());
        long timeMills = dateTime.getTimeInMillis();

        values.put(KEY_MILLS, timeMills);

        database.insert(TABLE_REMINDERS, null, values);
        database.close();
    }

    public int editReminder(Reminder reminder)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, reminder.get_title());
        contentValues.put(KEY_DESCRIPTION, reminder.get_description());
        contentValues.put(KEY_DAY, reminder.get_day());
        contentValues.put(KEY_MONTH, reminder.get_month());
        contentValues.put(KEY_YEAR, reminder.get_year());
        contentValues.put(KEY_HOUR, reminder.get_hour());
        contentValues.put(KEY_MINUTES, reminder.get_minute());

        return database.update(TABLE_REMINDERS, contentValues, KEY_ID + " = ?",new String[] { String.valueOf(reminder.get_id()) });

    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = new ArrayList<Reminder>();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS + " ORDER BY " + KEY_MILLS + " ASC";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.set_id(Integer.parseInt(cursor.getString(0)));
                reminder.set_title(cursor.getString(1));
                reminder.set_description(cursor.getString(2));
                reminder.set_day(cursor.getInt(3));
                reminder.set_month(cursor.getInt(4));
                reminder.set_year(cursor.getInt(5));
                reminder.set_hour(cursor.getInt(6));
                reminder.set_minute(cursor.getInt(7));

                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }

        return reminderList;
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_REMINDERS, KEY_ID + "=?", new String[]{String.valueOf(reminder.get_id())});
        database.close();
    }

    public int getRemindersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }
}
