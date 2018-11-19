package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle passInfo = getIntent().getExtras();

        MessageFragment fragment = new MessageFragment();
        fragment.tabletMode = false;
        fragment.setArguments(passInfo);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentDestination, fragment).addToBackStack(null).commit();
    }
}
