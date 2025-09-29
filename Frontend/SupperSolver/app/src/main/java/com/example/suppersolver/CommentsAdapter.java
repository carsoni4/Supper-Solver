package com.example.suppersolver;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Custom adapter for displaying the filtered listView for search screen.
 */
public class CommentsAdapter extends BaseAdapter implements ListAdapter {
    private String getURL = "http://coms-3090-025.class.las.iastate.edu:8080/comments/getSpecific/";
    private String deleteURL = "http://coms-3090-025.class.las.iastate.edu:8080/comments/";

    private ArrayList<String> list;
    private ArrayList<String> usernameList;
    private ArrayList<String> userIDList;
    private Context context;

    public interface RequestCallback {
        void onComplete();
    }

    public CommentsAdapter(ArrayList<String> list, ArrayList<String> usernameList, ArrayList<String> userIDList, Context context) {
        this.list = new ArrayList<>(list);
        this.usernameList = new ArrayList<>(usernameList);
        this.userIDList = new ArrayList<>(userIDList);
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
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_layoutcomments, null);
        }

        // Handle TextView and display string from filtered list
        TextView tvContact = (TextView) view.findViewById(R.id.tvContact);
        TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
        tvContact.setText(usernameList.get(position));
        tvComment.setText(list.get(position));

        // Handle buttons and add onClickListeners
        Button viewbtn = (Button) view.findViewById(R.id.btn);
        Button deletebtn = (Button) view.findViewById(R.id.deletebtn);

        Activity activity = (Activity) context;
        Bundle extras = activity.getIntent().getExtras();
        String userID = extras.getString("USERID");

        if (userIDList.get(position).equals(userID)) {
            viewbtn.setVisibility(View.INVISIBLE);
            deletebtn.setVisibility(View.VISIBLE);
        } else {
            viewbtn.setVisibility(View.VISIBLE);
            deletebtn.setVisibility(View.INVISIBLE);
        }

        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Bundle extras = activity.getIntent().getExtras();
                String userID = extras.getString("USERID");
                String username = extras.getString("username");
                String userToViewID = userIDList.get(position);
                Intent intent = new Intent(activity, ProfileScreen.class);
                intent.putExtra("USERID", userID);
                intent.putExtra("creatorID", userToViewID);
                intent.putExtra("username", username);
                activity.startActivity(intent);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Bundle extras = activity.getIntent().getExtras();
                String recipeID = extras.getString("recipeID");
                String userID = extras.getString("USERID");
                String username = extras.getString("username");
                getRequestCommentID(userIDList.get(position), recipeID, list.get(position));
                Intent intent = new Intent(activity, HomeFeed.class);
                intent.putExtra("USERID", userID);
                intent.putExtra("username", username);
                activity.startActivity(intent);
            }
        });

        return view;
    }

    private void getRequestCommentID(String userID, String recipeID, String comment) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                getURL + userID + '/' + recipeID + '/' + comment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response.toString());
                        deleteRequest(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void deleteRequest(String curID) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteURL + curID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

}