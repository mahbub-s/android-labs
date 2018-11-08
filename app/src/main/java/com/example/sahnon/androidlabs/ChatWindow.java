package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class ChatWindow extends Activity {

    private ListView msgView;
    private EditText msgBox;
    private Button sendButton;
    final private ArrayList<String> msgHistory = new ArrayList<>();
    private final static String ACTIVITY_NAME = "ChatWindow";

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        msgView = (ListView) findViewById(R.id.msgView);
        msgBox = (EditText) findViewById(R.id.msgBox);
        sendButton = (Button) findViewById(R.id.sendButton);

        final ChatAdapter messageAdapter = new ChatAdapter( this );
        msgView.setAdapter (messageAdapter);

        //Lab4
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        //Cursor c = db.query("chat", new String[]{"*"}, null,null,null,null, null);
        Cursor c = db.rawQuery("SELECT * FROM chat", null);
        int colIndex = c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        c.moveToFirst();


        while(!c.isAfterLast()){
            msgHistory.add(c.getString(colIndex));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString( c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME,"Cursor's column count=" + c.getColumnCount());
            Log.i(ACTIVITY_NAME,"Cursor's column name=" + c.getColumnName(colIndex));
            c.moveToNext();
        }



        c.close();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgHistory.add(msgBox.getText().toString());

                ContentValues cValues = new ContentValues();

                cValues.put(ChatDatabaseHelper.KEY_MESSAGE, msgBox.getText().toString());
                db.insert(ChatDatabaseHelper.TABLE_NAME, "Null replacement value", cValues);

                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount() & getView()
                msgBox.setText("");
            }
        });
    }

    public class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return msgHistory.size();
        }

        @Override
        public String getItem(int position) {
            return msgHistory.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText( getItem(position) ); // get the string at position

            return result;
        }

        @Override
        public long getItemId(int position){
            return position;
        }
    }

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//    }
}
