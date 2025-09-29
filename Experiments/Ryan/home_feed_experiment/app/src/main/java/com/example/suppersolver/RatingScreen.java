package com.example.suppersolver;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RatingScreen extends AppCompatActivity {

    private Button one, two, three, four, five; //"stars" for leaving a rating
    private Button postBtn, putBtn, deleteBtn, getBtn; //post,put,get,delete
    private TextView msgResponse, getResponse;
    private EditText etRating, etUserId, etRecipeId;
    private String myPost;
    private String getRating, getRecipeId, getUserId;
    private static final String url = "http://coms-3090-025.class.las.iastate.edu:8080/rating";
    int rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rating_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rating), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //assign xml ID's
        one = findViewById(R.id.one);two = findViewById(R.id.two);three = findViewById(R.id.three);four = findViewById(R.id.four); five = findViewById(R.id.five);
        postBtn = findViewById(R.id.post_btn);
        putBtn = findViewById(R.id.put_btn);
        getBtn = findViewById(R.id.get_rating_btn);
        deleteBtn = findViewById(R.id.delete_rating_btn);
        etRating = findViewById(R.id.rating_id_et);
        etRecipeId = findViewById(R.id.recipe_id_et);
        etUserId = findViewById(R.id.user_id_et);
        msgResponse = findViewById(R.id.msgResponse);
        getResponse = findViewById(R.id.get_response);

        //update rating value and highlight buttons up to the one that was pressed
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 1;
                colors(rating);
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 2;
                colors(rating);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 3;
                colors(rating);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 4;
                colors(rating);
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 5;
                colors(rating);
            }
        });

        //call volley functions for post, put, get, delete
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
        putBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNum = etRating.getText().toString();
                putRequest("/" + idNum);
            }
        });
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNum = etRating.getText().toString();
                getRequest("/" + idNum);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNum = etRating.getText().toString();
                deleteReq("/" + idNum);
            }
        });

    }

    //highlight rating buttons up to the one that was clicked
    private void colors(int currentRating){
        one.setBackgroundColor(Color.parseColor("#FFD700"));
        two.setBackgroundColor(Color.parseColor("#FFD700"));
        three.setBackgroundColor(Color.parseColor("#FFD700"));
        four.setBackgroundColor(Color.parseColor("#FFD700"));
        five.setBackgroundColor(Color.parseColor("#FFD700"));
        if(currentRating < 5){
            five.setBackgroundColor(Color.parseColor("#000000"));
            if(currentRating < 4){
                four.setBackgroundColor(Color.parseColor("#000000"));
                if(currentRating < 3){
                    three.setBackgroundColor(Color.parseColor("#000000"));
                    if(currentRating < 2){
                        two.setBackgroundColor(Color.parseColor("#000000"));
                    }
                }
            }
        }
    }

    //put request
    private void putRequest(String id) {

        // Convert inputs to JSONObject
        JSONObject postBody = new JSONObject();
        try{
            postBody.put("userid", Integer.parseInt(etUserId.getText().toString()));
            postBody.put("recipeID", Integer.parseInt(etRecipeId.getText().toString()));
            postBody.put("rating", rating);
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url + id,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        msgResponse.setText("updated a rating: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msgResponse.setText(error.toString());
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

    //post request
    private void postRequest() {

        // Convert input to JSONObject
        JSONObject postBody = new JSONObject();
        try{
            // etRequest should contain a JSON object string as your POST body
            // similar to what you would have in POSTMAN-body field
            // and the fields should match with the object structure of @RequestBody on sb
            postBody.put("userid", Integer.parseInt(etUserId.getText().toString()));
            postBody.put("recipeID", Integer.parseInt(etRecipeId.getText().toString()));
            postBody.put("rating", rating);
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
                        msgResponse.setText("posted a rating: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            msgResponse.setText(error.toString());
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

    //get request
    private void getRequest(String id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                url + id,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                            getRating = response.toString().substring(10, 11);
                            getRecipeId = response.toString().substring(23, 24);
                            getUserId = response.toString().substring(41, 43);
                            getResponse.setText("User: " + getUserId + "\nRecipe: " + getRecipeId
                                    + "\nRating: " + getRating);
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

    private void deleteReq(String id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                url + id,
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