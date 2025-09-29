package com.example.suppersolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * A screen for users to post and view comments for a particular recipe
 */
public class CommentScreen extends AppCompatActivity {

    private String getURLComments = "http://coms-3090-025.class.las.iastate.edu:8080/comments/strings/";
    private String getURLUsernames = "http://coms-3090-025.class.las.iastate.edu:8080/comments/usernames/";
    private String getURLUserIDs = "http://coms-3090-025.class.las.iastate.edu:8080/comments/userIDs/";
    private String postURL = "http://coms-3090-025.class.las.iastate.edu:8080/comments";

    private String curUsername;
    private String userID;
    private String recipeID;
    private ArrayList<String> commentsArray;
    private ArrayList<String> usersArray;
    private ArrayList<String> userIDsArray;

    private EditText commentEdt;
    private Button backBtn;
    private Button commentSend;
    private ListView commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.comment_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Toast.makeText(CommentScreen.this, "Failed to get extras from intent", Toast.LENGTH_LONG).show();
        } else {
            curUsername = extras.getString("username");
            userID = extras.getString("USERID");
            recipeID = extras.getString("recipeID");
        }

        commentEdt = findViewById(R.id.comment_edt);
        backBtn = findViewById(R.id.back_button);
        commentSend = findViewById(R.id.cmtSend);
        commentsList = findViewById(R.id.commentsList);
        commentsArray = new ArrayList<>();
        usersArray = new ArrayList<>();
        userIDsArray = new ArrayList<>();

        getRequestComments();

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CommentScreen.this, HomeFeed.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest(commentEdt.getText().toString());
                Intent intent = new Intent(CommentScreen.this, CommentScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                intent.putExtra("recipeID", recipeID);
                startActivity(intent);
            }
        });

    }

    private void getRequestComments() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURLComments + recipeID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                String curObj = response.getString(i);
                                commentsArray.add(curObj);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Log.d("Array", String.valueOf(commentsArray));
                        getRequestUsernames(commentsArray);
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

    private void getRequestUsernames(ArrayList<String> commentsArray) {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURLUsernames + recipeID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                String curObj = response.getString(i);
                                usersArray.add(curObj);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Log.d("Array", String.valueOf(usersArray));
                        getRequestUserIDs(commentsArray, usersArray);
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

    private void getRequestUserIDs(ArrayList<String> commentsArray, ArrayList<String> usersArray) {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURLUserIDs + recipeID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                String curObj = response.getString(i);
                                userIDsArray.add(curObj);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Log.d("Array", String.valueOf(userIDsArray));
                        CommentsAdapter ca = new CommentsAdapter(commentsArray, usersArray, userIDsArray, CommentScreen.this);
                        commentsList.setAdapter(ca);
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

    private void postRequest(String commentText) {

        // Convert input to JSONObject
        JSONObject postBody = new JSONObject();
        try{
            postBody.put("userID", userID);
            postBody.put("recipeID", recipeID);
            postBody.put("comment", commentText);// Assuming the server expects a key "comment"
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.d("Volley Test", postBody.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postURL,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("JSON Response", response.toString());
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

    /*
    private void deleteRequest(String curID) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                URL_JSON_OBJECT + curID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        msgResponse.setText(response.toString());
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
    */
}

