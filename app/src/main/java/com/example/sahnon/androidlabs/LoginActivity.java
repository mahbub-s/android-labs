package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    protected static final String PREF_FILE_KEY = "com.example.sahnon.androidlabs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i(ACTIVITY_NAME, "In onCreate()");

        Context context = LoginActivity.this;
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        final Button loginButton = findViewById(R.id.loginButton);
        final EditText emailInputApp = findViewById(R.id.emailInput);
        emailInputApp.setText(sharedPref.getString("DefaultEmail", "email@domain.com"));

        final Intent intent = new Intent(LoginActivity.this, StartActivity.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String emailInput = emailInputApp.getText().toString();
                editor.putString("DefaultEmail", emailInput);
                editor.commit();

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(ACTIVITY_NAME, "In onResume()");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(ACTIVITY_NAME, "In onDestroy()");

    }
}
