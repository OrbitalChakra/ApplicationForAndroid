package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Events extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView tvDate;
    Button dateButton;
    String username;
    public TextView todos;
    public EventDatabase db = new EventDatabase(this);
    public EditText newTextEvent; //use this variable to call createNewEvent after date is added
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        dateButton = findViewById(R.id.addDate);
        newTextEvent = findViewById(R.id.itemList2);
        todos = findViewById(R.id.listItems);
        SharedPreferences sharedPref = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        username = sharedPref.getString("username", "User1");
        Log.d(TAG, username);
        dateButton.setOnClickListener(v -> {
            // Please note that use your package name here
            com.example.myapplication.DatePicker mDatePickerDialogFragment;
            mDatePickerDialogFragment = new com.example.myapplication.DatePicker();
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
        });


    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());

        db.addEvent(newTextEvent.getText().toString(), selectedDate, username);
        updateUi();
    }
    public void updateUi(){
        List<TodoEvent> events = new ArrayList<TodoEvent>();
        List<String> e = new ArrayList<String>();
        events = db.getEventsInRange(username);
        for (int i = 0; i < events.size(); i++){
            String newEvents = events.get(i).eventToDo + "\t | \t" + events.get(i).whatday + "\n";
            e.add(newEvents);
        }
        String listString = String.join(", ", e);
        Log.d(TAG,listString);
        todos.setText(listString);

    }
    // The id of the group.

}

