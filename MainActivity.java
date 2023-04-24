package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView badLogin;
    Button login;
    public static EventDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = new EventDatabase(this);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        badLogin = findViewById(R.id.notifyIfIncorrectLogin);
    }
    public void login(View view){
        String password1 = db.getPassword(username.getText().toString());

        SharedPreferences sharedPref = getSharedPreferences(
                "myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username.getText().toString());
        editor.apply();
        Log.d(TAG, password.getText().toString());
        if (Objects.equals(password1, password.getText().toString())) {
            Intent intent = new Intent(this,  Events.class);
            startActivity(intent);
        }
        else{
            badLogin.setVisibility(View.VISIBLE);
        }
    }
    public void signup(View view){
        long id = db.addUser(username.getText().toString(), password.getText().toString());
    }


}