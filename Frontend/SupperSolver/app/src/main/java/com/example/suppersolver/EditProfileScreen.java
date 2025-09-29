package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *  Inputs for user to edit profile name, username, password, and bio, as well as upload a profile picture
 */
public class EditProfileScreen extends AppCompatActivity {

    private String creatorID;
    private String userID;
    private static final String put_url = "http://coms-3090-025.class.las.iastate.edu:8080/users/";
    private static final String imageUrl = "http://coms-3090-025.class.las.iastate.edu:8080/image/link";

    private EditText editName;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editDescription;
    private EditText imgUrl;
    private Button uploadImgBtn;
    private Button editButton;
    private Button deleteBtn;
    private Button backBtn;
    private Boolean isAdvertiser = false;
    private String curUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editprofile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editName = findViewById(R.id.edt_login_name);
        editUsername = findViewById(R.id.edt_login_username);
        editPassword = findViewById(R.id.edt_login_password);
        editDescription = findViewById(R.id.edt_description);
        editButton = findViewById(R.id.login_edt_btn);
        imgUrl = findViewById(R.id.edt_img_url);
        uploadImgBtn = findViewById(R.id.profile_pic_btn);
        deleteBtn = findViewById(R.id.delete_profile_btn);
        backBtn = findViewById(R.id.editprofile_back);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Toast.makeText(EditProfileScreen.this, "Failed to get extras from intent", Toast.LENGTH_SHORT).show();
        } else {
            curUsername = extras.getString("username");
            creatorID = extras.getString("creatorID");
            userID = extras.getString("USERID");
            if(extras.containsKey("isAdvertiser")){
                isAdvertiser = extras.getBoolean("isAdvertiser");
            }
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = editName.getText().toString();
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String description = editDescription.getText().toString();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    // Show error message
                    Toast.makeText(EditProfileScreen.this, userID, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (creatorID!=null) {
                    putRequest(creatorID);
                } else {
                    putRequest(userID);
                }

                if(isAdvertiser){
                    Intent intent = new Intent(EditProfileScreen.this, AdvertiserHomeScreen.class);
                    intent.putExtra("username", username);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("isAdvertiser", isAdvertiser);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(EditProfileScreen.this, ProfileScreen.class);
                    intent.putExtra("username", username);
                    intent.putExtra("description", description);
                    intent.putExtra("USERID", userID);
                    startActivity(intent);
                }

            }
        });

        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!imgUrl.getText().toString().isEmpty()) {
                    postImage();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteReq(userID);
                Intent intent = new Intent(EditProfileScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isAdvertiser){
                    Intent intent = new Intent(EditProfileScreen.this, AdvertiserHomeScreen.class);
                    intent.putExtra("username", curUsername);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("isAdvertiser", isAdvertiser);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(EditProfileScreen.this, ProfileScreen.class);
                    intent.putExtra("username", curUsername);
                    intent.putExtra("USERID", userID);
                    startActivity(intent);
                }
            }
        });
    }

    private void putRequest(String curID) {

        JSONObject postBody = new JSONObject();
        try{
            String edtNameText = editName.getText().toString();
            String edtUsernameText = editUsername.getText().toString();
            String edtPasswordText = editPassword.getText().toString();
            String edtDescriptionText = editDescription.getText().toString();

            postBody.put("name", edtNameText);
            postBody.put("username", edtUsernameText);
            postBody.put("password", edtPasswordText);
            postBody.put("bio", edtDescriptionText);
            if(isAdvertiser){
                postBody.put("isAdvertiser", true);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                put_url + curID,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String newDescription = response.getString("bio");
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

    private void postImage() {

        JSONObject postBody = new JSONObject();
        try{
            postBody.put("imgUrl", imgUrl.getText().toString());
            postBody.put("recipeID", 0);
            postBody.put("userID", userID);

        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                imageUrl,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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

    private void deleteReq(String id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                put_url + id,
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }
}
