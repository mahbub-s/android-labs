package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(ACTIVITY_NAME, "In onCreate()");

        final Button refButton = (Button) findViewById(R.id.button);

        final Button startButton = (Button) findViewById(R.id.startChat);

        final Button weatherButton = (Button) findViewById(R.id.startWeatherForecast);

        final Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
        final Intent chatIntent = new Intent(StartActivity.this, ChatWindow.class);
        final Intent weatherIntent = new Intent(StartActivity.this, WeatherForecast.class);

        refButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(intent, 50);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.i(ACTIVITY_NAME, "@string/startButtonMsg");
                startActivity(chatIntent);
            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.i(ACTIVITY_NAME, "@string/startButtonMsg");
                startActivity(weatherIntent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if(requestCode == 50) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }
        if(responseCode == Activity.RESULT_OK) {
            String messagePassed = data.getStringExtra("Response");
            CharSequence text = "ListItemsActivity passed: " + messagePassed;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(StartActivity.this, text, duration);
            toast.show();
        }
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
