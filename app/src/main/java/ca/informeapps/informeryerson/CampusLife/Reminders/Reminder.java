/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.CampusLife.Reminders;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        @Override
        public Reminder createFromParcel(Parcel parcel) {
            return new Reminder(parcel);
        }

        @Override
        public Reminder[] newArray(int i) {
            return new Reminder[i];
        }
    };
    private int _id;
    private String _title;
    private String _description;
    private int _day;
    private int _month;
    private int _year;
    private int _hour;
    private int _minute;

    public Reminder() {
    }

    public Reminder(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        _title = data[0];
        _description = data[1];

        int[] moreData = new int[6];
        in.readIntArray(moreData);
        _id = moreData[0];
        _year = moreData[1];
        _month = moreData[2];
        _day = moreData[3];
        _hour = moreData[4];
        _minute = moreData[5];
    }

    public Reminder(String title, String description, int day, int month, int year, int hour, int minute) {
        _title = title;
        _description = description;
        _day = day;
        _month = month;
        _year = year;
        _hour = hour;
        _minute = minute;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public int get_day() {
        return _day;
    }

    public void set_day(int _day) {
        this._day = _day;
    }

    public int get_month() {
        return _month;
    }

    public void set_month(int _month) {
        this._month = _month;
    }

    public int get_year() {
        return _year;
    }

    public void set_year(int _year) {
        this._year = _year;
    }

    public int get_hour() {
        return _hour;
    }

    public void set_hour(int _hour) {
        this._hour = _hour;
    }

    public int get_minute() {
        return _minute;
    }

    public void set_minute(int _minute) {
        this._minute = _minute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this._title, this._description});
        parcel.writeIntArray(new int[]{this._id, this._year, this._month, this._day, this._hour, this._minute});
    }
}
