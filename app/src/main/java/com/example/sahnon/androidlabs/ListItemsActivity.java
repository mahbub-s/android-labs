package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {

    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    private ImageButton camButton;
    private Switch switchApp;
    private CheckBox checkBoxApp;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        camButton = (ImageButton) findViewById(R.id.imageButton);
        switchApp = (Switch) findViewById(R.id.switchApp);
        checkBoxApp = (CheckBox) findViewById(R.id.checkBox2);

        Log.i(ACTIVITY_NAME, "In onCreate()");

        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        camButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent(takePictureIntent);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        switchApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    CharSequence text = "Switch is On";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                    toast.show();
                }
                else {
                    CharSequence text = "Switch is Off";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                    toast.show();
                }
            }
        });

        checkBoxApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    checkBoxDialog();
                }
            }
        });
    }

    private void checkBoxDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        builder.setMessage(R.string.dialog_message); //Add a dialog message to strings.xml
            builder.setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                // User clicked OK button
                public void onClick(DialogInterface dialog, int id) {
                    Intent resultIntent = new Intent( );
                    resultIntent.putExtra("Response", "Here is my response");
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                // User cancelled the dialog
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK==resultCode) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            camButton = (ImageButton) findViewById(R.id.imageButton);
            camButton.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent(Intent takePictureIntent) {
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
