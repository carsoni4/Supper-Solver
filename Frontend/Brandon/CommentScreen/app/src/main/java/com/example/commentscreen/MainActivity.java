package com.example.commentscreen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private TextView msgResponse;
    private TextView tvResponse;
    private TextView comment1;
    private TextView comment2;
    private TextView comment3;
    private TextView commentUser;
    private EditText cmtEdt;
    private Button cmtSend;
    private Button deleteButton;

    private static final String URL_JSON_OBJECT = "http://coms-3090-025.class.las.iastate.edu:8080/comments/";
    private String method;
    private String url = "http://coms-3090-025.class.las.iastate.edu:8080/comments";
    private String curID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        msgResponse = findViewById(R.id.msgResponse);
        tvResponse = findViewById(R.id.tvResponse);
        cmtSend = findViewById(R.id.cmtSend);
        cmtEdt = findViewById(R.id.comment_edt);
        deleteButton = findViewById(R.id.deleteButton);

        comment1 = findViewById(R.id.comment1);
        comment2 = findViewById(R.id.comment2);
        comment3 = findViewById(R.id.comment3);
        commentUser = findViewById(R.id.commentUser);

        method = "POST";

        cmtSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (method.equals("GET")) getRequest();
                else postRequest();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteRequest(curID);
                commentUser.setText("");
                deleteButton.setVisibility(View.INVISIBLE);
            }
        });


        makeJsonObjReq("http://coms-3090-025.class.las.iastate.edu:8080/comments/1");
        makeJsonObjReq("http://coms-3090-025.class.las.iastate.edu:8080/comments/2");
        makeJsonObjReq("http://coms-3090-025.class.las.iastate.edu:8080/comments/3");

    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq(String URL_JSON_OBJECT) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Assuming the response JSON has a key "comment"
                            String comment = response.getString("comment");
                            if (URL_JSON_OBJECT.equals("http://coms-3090-025.class.las.iastate.edu:8080/comments/1")) {
                                comment1.setText(comment);
                            }
                            if (URL_JSON_OBJECT.equals("http://coms-3090-025.class.las.iastate.edu:8080/comments/2")) {
                                comment2.setText(comment);
                            }
                            if (URL_JSON_OBJECT.equals("http://coms-3090-025.class.las.iastate.edu:8080/comments/3")) {
                                comment3.setText(comment);
                            }
                        } catch (Exception e) {
                            Log.e("JSON Parsing Error", e.toString());
                            msgResponse.setText("Error: Unable to parse the response.");
                        }
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

    private void getRequest() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // String response can be converted to JSONObject via
                        // JSONObject object = new JSONObject(response);
                        tvResponse.setText("Response is: "+ response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResponse.setText("That didn't work!" + error.toString());
                    }
                }){

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

    private void postRequest() {

        // Convert input to JSONObject
        JSONObject postBody = new JSONObject();
        try{
            String commentText = cmtEdt.getText().toString();  // Get the input text from EditText
            postBody.put("comment", commentText);  // Assuming the server expects a key "comment"
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String comment = response.getString("comment");
                            commentUser.setText(comment);
                            curID = response.getString("ID");
                        } catch (Exception e) {
                            Log.e("JSON Parsing Error", e.toString());
                            tvResponse.setText("Error: Unable to parse the response.");                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResponse.setText(error.getMessage());
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
}

