package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User can post a recipe by providing a name, description, and image URL.
 */
public class PostAdvertisement extends AppCompatActivity {
    private static final String postUrl = "http://coms-3090-025.class.las.iastate.edu:8080/advertisements";
    private String userID, username;
    private EditText description, imageSource;
    private Button postAdvertisementBtn, backButton;
    private TextView id;
    private int recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_advertisement);

        description = findViewById(R.id.edit_description);
        postAdvertisementBtn = findViewById(R.id.post_advertisement_btn);
        imageSource = findViewById(R.id.edit_image_url);
        backButton = findViewById(R.id.back_button);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(PostAdvertisement.this, "cannot find user", Toast.LENGTH_SHORT).show();
        } else {
            userID = extras.getString("USERID");
            username = extras.getString("username");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PostAdvertisement.this, AdvertiserHomeScreen.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                intent.putExtra("isAdvertiser", true);
                startActivity(intent);
            }
        });

        postAdvertisementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgsrc = imageSource.getText().toString();
                String desc = description.getText().toString();
                if(imgsrc.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(PostAdvertisement.this, "please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    postRequest();
                }
            }
        });
    }

    private void postRequest() {

        JSONObject postBody = new JSONObject();
        try{
            postBody.put("description", description.getText().toString());
            postBody.put("advertiserID", Integer.parseInt(userID));
            postBody.put("imageURL", imageSource.getText().toString());
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postUrl,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(PostAdvertisement.this, AdvertiserHomeScreen.class);
                        intent.putExtra("username", username);
                        intent.putExtra("USERID", userID);
                        intent.putExtra("isAdvertiser", true);
                        startActivity(intent);
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

}
