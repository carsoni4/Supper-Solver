package com.example.suppersolver;

import java.util.ArrayList;

import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;
import android.os.Bundle;

/**
 * Custom adapter for displaying a ListView for the friends screen.
 */
public class FriendsListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;



    public FriendsListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_layout, null);
        }

        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.tvContact);
        tvContact.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.btn);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Activity activity = (Activity) context;
                Bundle extras = activity.getIntent().getExtras();
                String userID = extras.getString("USERID");
                String username = extras.getString("username");
                Intent intent = new Intent( activity, SendMessageScreen.class);
                intent.putExtra("USERID", userID);
                intent.putExtra("username", username);
                intent.putExtra("openUSER", list.get(position));
                activity.startActivity(intent);
            }
        });

        return view;
    }
}