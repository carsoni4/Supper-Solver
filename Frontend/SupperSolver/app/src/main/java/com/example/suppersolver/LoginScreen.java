package com.example.suppersolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Log in with a valid username and password.
 */
public class LoginScreen extends AppCompatActivity {

    private static final String url = "http://coms-3090-025.class.las.iastate.edu:8080/login";
    private static final String URL_STRING_REQ = "http://coms-3090-025.class.las.iastate.edu:8080/userID/";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        homeButton = findViewById(R.id.home_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    // Show error message
                    return;
                }

                postRequest();

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void postRequest() {

        JSONObject postBody = new JSONObject();
        try {
            String username = usernameEditText.getText().toString();  // Get the input text from EditText
            String password = passwordEditText.getText().toString();
            postBody.put("username", username);
            postBody.put("password", password);
        } catch (Exception e) {
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
                            String adm = response.getString("isAdmin");
                            String adv = response.getString("isAdvertiser");
                            Boolean isAdmin = adm.equals("true");
                            Boolean isAdvertiser = adv.equals("true");
                            int userID = response.getInt("id");
                            String username = response.getString("username");

                            if (isAdmin) {
                                Intent intent = new Intent(LoginScreen.this, AdminHomeScreen.class);
                                intent.putExtra("username", username);
                                intent.putExtra("USERID", String.valueOf(userID));
                                intent.putExtra("isAdmin", isAdmin);
                                startActivity(intent);
                            } else if (isAdvertiser){
                                Intent intent = new Intent(LoginScreen.this, AdvertiserHomeScreen.class);
                                intent.putExtra("username", username);
                                intent.putExtra("USERID", String.valueOf(userID));
                                intent.putExtra("isAdvertiser", isAdvertiser);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(LoginScreen.this, HomeFeed.class);
                                intent.putExtra("username", username);
                                intent.putExtra("USERID", String.valueOf(userID));
                                intent.putExtra("isAdvertiser", isAdvertiser);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        //makeStringReq();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /*
    private void makeStringReq() {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL_STRING_REQ + username + "/" + password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        userID = response;
                        Intent intent = new Intent(LoginScreen.this, HomeFeed.class);
                        intent.putExtra("username", username);
                        intent.putExtra("USERID", userID);
                        if (isAdmin) intent.putExtra("isAdmin", "t");
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }*/


}
