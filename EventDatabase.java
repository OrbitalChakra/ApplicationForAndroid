package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EventDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "events.db";
    public static final int VERSION = 2;

    public EventDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
        private static final class EventTable {
            private static final String TABLE = "event";
            private static final String COL_ID = "_id";
            private static final String COL_TITLE = "title";
            private static final String COL_USER = "user";
            private static final String COL_DATE = "date";
        }
        private static final class UsersTable{
        private static final String TABLE = "users";
        private static final String COL_ID = "_id";
        private static final String COL_USER = "username";
        private static final String COL_PASSWORD = "password";
        }

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL("create table " + EventTable.TABLE + " (" +
                EventTable.COL_ID + " integer primary key autoincrement, " +
                EventTable.COL_TITLE + " text, " +
                EventTable.COL_USER + " text, " +
                EventTable.COL_DATE + " date )" );
        db.execSQL("create table " + UsersTable.TABLE + "(" +
                UsersTable.COL_ID + " integer primary key autoincrement, " +
                UsersTable.COL_USER + " text, " +
                UsersTable.COL_PASSWORD + " text )" );
    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion,
                           int newVersion){
        db.execSQL("drop table if exists " + EventTable.TABLE);
        db.execSQL("drop table if exists " + UsersTable.TABLE);
        onCreate(db);
    }

    public long addUser (String username, String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UsersTable.COL_USER, username);
        values.put(UsersTable.COL_PASSWORD, password);

        long userId = db.insert(UsersTable.TABLE, null,  values);
        return userId;
    }

    public String getPassword(String user){
        String password = new String();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + UsersTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {user});
        if (cursor.moveToFirst()){
            do{
                long id = cursor.getLong(0);
                String username = cursor.getString(1);
                password = cursor.getString(2);
            }while(cursor.moveToNext());
        }
        cursor.close();
        Log.d(TAG, password);
        return password;
    }

    public long addEvent(String title, String date, String user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.COL_TITLE, title);
        values.put(EventTable.COL_DATE, date);
        values.put(EventTable.COL_USER, user);

        long movieId = db.insert(EventTable.TABLE, null, values);
        return movieId;
    }

    public List<TodoEvent> getEventsInRange(String user) {
        SQLiteDatabase db = getReadableDatabase();
        //implements days from date for notification
        List<TodoEvent> eventsArray = new ArrayList<TodoEvent>();
        int i = 0;
        String today = getToday();
        String endDate = getEndDate(365);
        String sql = "select * from " + EventTable.TABLE + " where user = ? and date >= ? and date <= ?";
        Cursor cursor = db.rawQuery(sql, new String[] { user, today, endDate});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String date = cursor.getString(3);
                TodoEvent newEvent = new TodoEvent();
                newEvent.setEvent(title);
                newEvent.setDay(date);
                eventsArray.add(newEvent);
                i++;
            } while (cursor.moveToNext());

        }
        cursor.close();
        return eventsArray;
    }

    public TodoEvent getEventsToday(String user) {
        SQLiteDatabase db = getReadableDatabase();
        //implements days from date for notification
        TodoEvent event = new TodoEvent();
        String today = getToday();
        String sql = "select * from " + EventTable.TABLE + " where user = ? and date = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{user, today});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String date = cursor.getString(3);
                TodoEvent newEvent = new TodoEvent();
                event.setEvent(title);
                event.setDay(date);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return event;
    }
    public String getEndDate(int days){
        DateFormat dateFormat = new SimpleDateFormat("'yyyy-MM-dd' 'HH:mm:ss'");

        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        Date date1 = cal.getTime();
        String enddate = dateFormat.format(date1);
        Log.d(TAG, enddate);
        return enddate;
    }
    public String getToday(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date date = new Date();
        String today = dateFormat.format(date);
        Log.d(TAG, today);
        return today;
    }
}
