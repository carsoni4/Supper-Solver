package com.example.suppersolver;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GroupScreen extends AppCompatActivity {
    private String deleteURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/";
    private String putURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/removeUser/";
    private String getInGroupURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/userInGroup/";
    private String getGroupIDURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/userGroupID/";

    private String userID;
    private String groupID;
    private String curUsername;
    private boolean inGroup;

    private Button backBtn;
    private Button createGroup;
    private Button addToGroup;
    private Button groupMessage;
    private Button groupInventory;
    private Button leaveGroup;
    private Button deleteGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.groupScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.back_button);
        createGroup = findViewById(R.id.createGroup);
        addToGroup = findViewById(R.id.addToGroup_btn);
        groupMessage = findViewById(R.id.groupMessage);
        groupInventory = findViewById(R.id.groupInventory);
        leaveGroup = findViewById(R.id.leaveGroup_btn);
        deleteGroup = findViewById(R.id.deleteGroup_btn);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        } else {
            curUsername = extras.getString("username");
            userID = extras.getString("USERID");
        }

        getInGroupRequest();

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GroupScreen.this, ProfileScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GroupScreen.this, CreateGroupScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        addToGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getGroupIDRequest(false);
            }
        });

        groupMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GroupScreen.this, GroupMessageScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        leaveGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                putRequest(putURL, userID);
            }
        });

        deleteGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getGroupIDRequest(true);
            }
        });
    }

    private void deleteRequest(String url, String curID) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url + curID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response (delete)", response.toString());
                        Intent intent = new Intent(GroupScreen.this, ProfileScreen.class);
                        intent.putExtra("username", curUsername);
                        intent.putExtra("USERID", userID);
                        startActivity(intent);
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void putRequest(String url, String curID) {
        // Convert input to JSONObject
        JSONObject postBody = new JSONObject();
        try{
            postBody.put("userID", userID);// Assuming the server expects a key "comment"
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url + curID,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response", response.toString());
                            Intent intent = new Intent(GroupScreen.this, ProfileScreen.class);
                            intent.putExtra("username", curUsername);
                            intent.putExtra("USERID", userID);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e("JSON Parsing Error", e.toString());
                }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    private void getInGroupRequest() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                getInGroupURL + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the response as a boolean
                        boolean isUserInGroup = Boolean.parseBoolean(response); // Convert response to boolean

                        // Handle the boolean value
                        if (isUserInGroup) {
                            Log.d("Volley Response", "User is in the group.");
                            inGroup = true;
                        } else {
                            Log.d("Volley Response", "User is not in the group.");
                            inGroup = false;
                        }

                        if (inGroup) {
                            createGroup.setVisibility(View.INVISIBLE);
                            addToGroup.setVisibility(View.VISIBLE);
                            groupMessage.setVisibility(View.VISIBLE);
                            groupInventory.setVisibility(View.VISIBLE);
                            leaveGroup.setVisibility(View.VISIBLE);
                            deleteGroup.setVisibility(View.VISIBLE);
                        } else {
                            createGroup.setVisibility(View.VISIBLE);
                            addToGroup.setVisibility(View.INVISIBLE);
                            groupMessage.setVisibility(View.INVISIBLE);
                            groupInventory.setVisibility(View.INVISIBLE);
                            leaveGroup.setVisibility(View.INVISIBLE);
                            deleteGroup.setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error (inGroupCheck)", error.toString());
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getGroupIDRequest(boolean delete) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                getGroupIDURL + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response (groupid)", response);
                        groupID = response;
                        if (!delete) {
                            Intent intent = new Intent(GroupScreen.this, AddToGroupScreen.class);
                            intent.putExtra("username", curUsername);
                            intent.putExtra("USERID", userID);
                            intent.putExtra("GROUPID", groupID);
                            startActivity(intent);
                        } else {
                            deleteRequest(deleteURL, groupID);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error (groupid)", error.toString());
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}

