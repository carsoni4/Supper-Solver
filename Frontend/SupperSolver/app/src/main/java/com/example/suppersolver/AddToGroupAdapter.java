package com.example.suppersolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;
import android.util.Log;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Custom adapter for displaying a ListView for the friends screen.
 */
public class AddToGroupAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> groupList = new ArrayList<>();
    private Context context;

    public interface OnGroupListChangeListener {
        void onGroupListChanged(ArrayList<String> updatedGroupList);
    }

    private CreateGroupAdapter.OnGroupListChangeListener listener;

    public AddToGroupAdapter(ArrayList<String> list, Context context, CreateGroupAdapter.OnGroupListChangeListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    // Call this when the group list is updated
    private void notifyGroupListChanged() {
        if (listener != null) {
            listener.onGroupListChanged(groupList);
        }
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
            view = inflater.inflate(R.layout.custom_layoutaddtogroup, null);
        }

        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.tvContact);
        tvContact.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button addbtn= (Button)view.findViewById(R.id.btn);
        Button removebtn = (Button)view.findViewById(R.id.removebtn);

        TextView groupListView = (TextView)view.findViewById(R.id.groupListView);

        addbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userToAdd = list.get(position);

                if (!groupList.contains(userToAdd)) {
                    groupList.add(userToAdd);
                    notifyGroupListChanged();
                } else {
                    Toast.makeText(context, "You have already added this friend to the group!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        removebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userToRemove = list.get(position);

                if (!groupList.contains(userToRemove)) {
                    Toast.makeText(context, "You have not added this friend to the group yet!", Toast.LENGTH_SHORT).show();
                } else {
                    groupList.remove(userToRemove);
                    notifyGroupListChanged();
                }
            }
        });

        return view;
    }
}