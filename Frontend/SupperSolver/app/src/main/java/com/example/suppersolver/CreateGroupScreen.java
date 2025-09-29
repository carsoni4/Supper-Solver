package com.example.suppersolver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateGroupScreen extends AppCompatActivity {

    private String getURL = "http://coms-3090-025.class.las.iastate.edu:8080/users/getFriends/";
    private String postURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/";

    private String userID;
    private String curUsername;
    private ArrayList<String> friendsArray;
    private ArrayList<String> groupList;

    private ListView friendGroupList;
    private Button backBtn;
    private Button createGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroup_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createGroupScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(CreateGroupScreen.this, "ERROR getting extras from bundle", Toast.LENGTH_LONG);
        } else {
            curUsername = extras.getString("username");
            userID = extras.getString("USERID");
        }

        friendGroupList = findViewById(R.id.friendGroupList);
        backBtn = findViewById(R.id.back_button);
        createGroupBtn = findViewById(R.id.createGroupBtn);
        friendsArray = new ArrayList<>();
        groupList = new ArrayList<>();

        getRequest();

        createGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (groupList.size() < 2) {
                    Toast.makeText(CreateGroupScreen.this, "Group size is too small, please add more friends to the group.", Toast.LENGTH_LONG).show();
                } else {
                    postRequest(userID, groupList);
                    Intent intent = new Intent(CreateGroupScreen.this, GroupScreen.class);
                    intent.putExtra("username", curUsername);
                    intent.putExtra("USERID", userID);
                    startActivity(intent);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupScreen.this, GroupScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

    }

    private void getRequest() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURL + userID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curObj = response.getJSONObject(i);
                                String curUser = curObj.getString("username");
                                friendsArray.add(curUser);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        CreateGroupAdapter adapter = new CreateGroupAdapter(friendsArray, CreateGroupScreen.this, new CreateGroupAdapter.OnGroupListChangeListener() {
                            @Override
                            public void onGroupListChanged(ArrayList<String> updatedGroupList) {
                                groupList = updatedGroupList;
                                Log.d("CreateGroupTest (in main act)", groupList.toString());
                                TextView groupListView = findViewById(R.id.groupListView);
                                StringBuilder groupListText = new StringBuilder("Group List: ");
                                for (int i = 0; i < updatedGroupList.size(); i++) {
                                    groupListText.append(updatedGroupList.get(i));
                                    if (i != updatedGroupList.size() - 1) {
                                        groupListText.append(", ");
                                    }
                                }
                                groupListView.setText(groupListText.toString());
                            }

                        });
                        friendGroupList.setAdapter(adapter);
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    private void postRequest(String currentUserID, ArrayList<String> usersToAdd) {

        // Convert input to JSONObject
        JSONArray userslist = new JSONArray(usersToAdd);
        JSONObject postBody = new JSONObject();
        try{
            postBody.put("array", userslist);
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postURL + currentUserID,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response (Create Group)", response.toString());
                        } catch (Exception e) {
                            Log.e("JSON Parsing Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error (Create Group)", error.toString());
                    }
                }
        ){
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}
