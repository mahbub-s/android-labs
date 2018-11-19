package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MessageFragment extends Fragment {
    ChatWindow parent = null;
    public boolean tabletMode;

    Fragment fragment;
    FragmentTransaction ft;

    public MessageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String message = getArguments().getString("message");
        final long id = getArguments().getLong("id");

        fragment = (Fragment) getFragmentManager().findFragmentById(R.id.msgFragment);
        ft = getFragmentManager().beginTransaction();

        // Inflate the layout for this fragment
        View view = (View) inflater.inflate(R.layout.fragment_layout, container, false);
        TextView msg = (TextView) view.findViewById(R.id.showMsg);
        TextView msgId = (TextView) view.findViewById(R.id.msgID);
        msg.setText(message);
        msgId.setText(Long.toString(id));

        Button delete = (Button) view.findViewById(R.id.deleteMsg);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (tabletMode) {
                    parent.deleteMessage(id);

                    if(fragment != null) {
                        ft.remove(fragment).commit();
                        fragment = null;
                    }
                }
                else {
                    Intent resultIntent = new Intent( );
                    resultIntent.putExtra("id", id);
                    getActivity().setResult(50, resultIntent);
                    getActivity().finish();
                }
            }});

        return view;
    }

    @Override
    public void onAttach(Activity context){
        super.onAttach(context);

        if(tabletMode)
            parent = (ChatWindow) context;
    }
}
