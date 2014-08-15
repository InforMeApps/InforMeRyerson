package ca.informeapps.informeryerson.CampusLife.Reminders;

public class Reminder {

    int _id;
    String _title;
    String _description;
    int _day;
    int _month;
    int _year;
    int _hour;
    int _minute;

    public Reminder() {
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

    public Reminder(int id, String title, String description, int day, int month, int year, int hour, int minute) {
        _id = id;
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
}
