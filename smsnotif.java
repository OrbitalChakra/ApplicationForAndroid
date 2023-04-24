package com.example.myapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class smsnotif extends AppCompatActivity{
    public static EventDatabase db;
    Button yes;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);
        yes = findViewById(R.id.accept);
    }
    public void accept(View view){
        Intent intent = new Intent(this,  Events.class);
        startActivity(intent);
    }
}