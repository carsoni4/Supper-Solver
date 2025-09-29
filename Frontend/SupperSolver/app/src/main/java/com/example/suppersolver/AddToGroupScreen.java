package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddToGroupScreen extends AppCompatActivity {
    private String getURLFriends = "http://coms-3090-025.class.las.iastate.edu:8080/users/getFriends/";
    private String getURLGroupList = "http://coms-3090-025.class.las.iastate.edu:8080/group/getAllUsers/";
    private String putURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/addUser/";

    private String curUsername;
    private String userID;
    private String groupID;
    private ArrayList<String> groupArray;
    private ArrayList<String> adapterArray;
    private ArrayList<String> groupList;

    private ListView friendGroupList;

    private Button backBtn;
    private Button addToGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtogroup_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addToGroupScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        groupArray = new ArrayList<>();
        adapterArray = new ArrayList<>();
        groupList = new ArrayList<>();
        backBtn = findViewById(R.id.back_button);
        addToGroupBtn = findViewById(R.id.addToGroupBtn);
        friendGroupList = findViewById(R.id.friendGroupList);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        } else {
            curUsername = extras.getString("username");
            userID = extras.getString("USERID");
            groupID = extras.getString("GROUPID");
        }

        getRequestFriends();

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddToGroupScreen.this, GroupScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        addToGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                putRequest(groupList, groupID);
                Intent intent = new Intent(AddToGroupScreen.this, GroupScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });
    }

    private void getRequestFriends() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURLFriends + userID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        ArrayList<String> friendsArray = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curObj = response.getJSONObject(i);
                                String curUser = curObj.getString("username");
                                friendsArray.add(curUser);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        getRequestGroupList(friendsArray);
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

    private void getRequestGroupList(ArrayList<String> friendsArray) {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURLGroupList + groupID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curObj = response.getJSONObject(i);
                                String curUser = curObj.getString("username");
                                groupArray.add(curUser);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        for (String friend : friendsArray) {
                            if (!groupArray.contains(friend)) {
                                adapterArray.add(friend);
                            }
                        }

                        friendGroupList.setAdapter(new AddToGroupAdapter(adapterArray, AddToGroupScreen.this, new CreateGroupAdapter.OnGroupListChangeListener() {
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
                        }));
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

    private void putRequest(ArrayList<String> usersToAdd, String groupID) {

        JSONArray usersList = new JSONArray(usersToAdd);
        JSONObject postBody = new JSONObject();
        try{
            postBody.put("array", usersList);
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.d("Add to group test", postBody.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                putURL + groupID,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response", response.toString());
                        } catch (Exception e) {
                            Log.e("JSON Parsing Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
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