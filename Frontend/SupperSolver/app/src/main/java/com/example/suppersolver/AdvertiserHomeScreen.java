package com.example.suppersolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class AdvertiserHomeScreen extends AppCompatActivity{

    private String getURL = "http://coms-3090-025.class.las.iastate.edu:8080/users/";
    private String getProfilePic = "http://coms-3090-025.class.las.iastate.edu:8080/image/user/";
    private Button postAd;
    private Button editProfile;
    private TextView profileName;
    private String userID;
    private String username;
    private Boolean isAdvertiser;
    private TextView description;
    private ImageView profileImage;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertiser_home);

        postAd = findViewById(R.id.post_advertisement);
        editProfile = findViewById(R.id.editProfile);
        profileName = findViewById(R.id.welcomeMessage);
        description = findViewById(R.id.description);
        profileImage = findViewById(R.id.profileImage);
        logout = findViewById(R.id.logout);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(AdvertiserHomeScreen.this, "cannot find user", Toast.LENGTH_SHORT).show();
        } else {
            userID = extras.getString("USERID");
            username = extras.getString("username");
            isAdvertiser = extras.getBoolean("isAdvertiser");
        }

        profileName.setText("Welcome\n" + username);

        getRequest(userID);
        requestProfilePic(userID);

        postAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvertiserHomeScreen.this, PostAdvertisement.class);
                intent.putExtra("USERID", userID);
                intent.putExtra("username", username);
                intent.putExtra("isAdvertiser", isAdvertiser);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvertiserHomeScreen.this, EditProfileScreen.class);
                intent.putExtra("USERID", userID);
                intent.putExtra("username", username);
                intent.putExtra("isAdvertiser", isAdvertiser);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdvertiserHomeScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getRequest(String id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                getURL + id,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            if(response.getString("bio").equals("null")) {
                                description.setText("No description for this user has been added.");
                            } else {
                                description.setText(response.getString("bio"));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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

    private void requestProfilePic(String rid) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                getProfilePic + rid,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String imgSource = response.getString("imgUrl");
                            makeImageRequest(imgSource);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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

    private void makeImageRequest(String imgSrc) {

        ImageRequest imageRequest = new ImageRequest(
                imgSrc,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        profileImage.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }
}
