package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class ChatWindow extends Activity {

    private boolean frameExists;
    private View frame;

    Cursor c;

    private ListView msgView;
    private EditText msgBox;
    private Button sendButton;
    final private ArrayList<String> msgHistory = new ArrayList<>();
    private final static String ACTIVITY_NAME = "ChatWindow";

    private SQLiteDatabase db;

    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        msgView = (ListView) findViewById(R.id.msgView);
        msgBox = (EditText) findViewById(R.id.msgBox);
        sendButton = (Button) findViewById(R.id.sendButton);

        messageAdapter = new ChatAdapter( this );
        msgView.setAdapter (messageAdapter);

        //lab7
        frame = (View) findViewById(R.id.frame);
        if(frame == null){
            frameExists = false;
        } else{
            frameExists = true;
        }

        msgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
                //bundle strings
                Bundle passInfo = new Bundle();
                passInfo.putString("message", messageAdapter.getItem(position));
                passInfo.putLong("id", messageAdapter.getItemId(position));

                if(frameExists){
                    MessageFragment fragment = new MessageFragment();
                    fragment.tabletMode = true;
                    fragment.setArguments(passInfo);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, fragment).addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(passInfo);
                    startActivityForResult(intent, 50);
                } //for phone
            }
        });

        //Lab4
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        //Cursor c = db.query("chat", new String[]{"*"}, null,null,null,null, null);
        c = db.rawQuery("SELECT * FROM chat", null);
        int colIndex = c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        c.moveToFirst();


        while(!c.isAfterLast()){
            msgHistory.add(c.getString(colIndex));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString( c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME,"Cursor's column count=" + c.getColumnCount());
            Log.i(ACTIVITY_NAME,"Cursor's column name=" + c.getColumnName(colIndex));
            c.moveToNext();
        }

        //c.close();

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
            c.moveToPosition(position);
            long id = 0;
            if(c.getCount() > 0)
                id = c.getLong(0);
            return id;
        }
    }

    public void deleteMessage(long id){
        msgHistory.clear();

        db.delete("chat", "_id =" + id, null);

        //Cursor c = db.query("chat", new String[]{"*"}, null,null,null,null, null);
        c = db.rawQuery("SELECT * FROM chat", null);
        int colIndex = c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        c.moveToFirst();

        while(!c.isAfterLast()){
            msgHistory.add(c.getString(colIndex));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString( c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME,"Cursor's column count=" + c.getColumnCount());
            Log.i(ACTIVITY_NAME,"Cursor's column name=" + c.getColumnName(colIndex));
            c.moveToNext();
        }

        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if(requestCode == 50) {
            deleteMessage(data.getLongExtra("id", 0));
        }
    }

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//    }
}
