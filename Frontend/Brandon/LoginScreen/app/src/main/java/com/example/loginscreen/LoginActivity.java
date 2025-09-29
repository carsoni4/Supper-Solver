package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText editUsernameText;
    private EditText editPasswordText;
    private TextView putMessage;
    private Button edtLoginButton;
    private Button loginButton;         // define login button variable
    private Button homeButton;          // created a new home button variable
    private static final String url = "http://coms-3090-025.class.las.iastate.edu:8080/login";
    private static final String put_url = "http://coms-3090-025.class.las.iastate.edu:8080/users/";
    private EditText curID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        editUsernameText = findViewById(R.id.edt_login_username);
        editPasswordText = findViewById(R.id.edt_login_password);
        edtLoginButton = findViewById(R.id.login_edt_btn);
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        homeButton = findViewById(R.id.home_btn);  // link the new home button in the Login activity XML
        putMessage = findViewById(R.id.putMessage);
        curID = findViewById(R.id.id_edt);

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    // Show error message
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                postRequest();

            }
        });

        edtLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* grab strings from user inputs */
                String username = editUsernameText.getText().toString();
                String password = editPasswordText.getText().toString();
                String ID = curID.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    // Show error message
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                putRequest(ID);

            }

        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }

    private void postRequest() {

        // Convert input to JSONObject
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
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void putRequest(String curID) {

        // Convert input to JSONObject
        JSONObject postBody = new JSONObject();
        try{
            String edtUsernameText = editUsernameText.getText().toString();  // Get the input text from EditText
            String edtPasswordText = editPasswordText.getText().toString();
            postBody.put("username", edtUsernameText);
            postBody.put("password", edtPasswordText);
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
                            String newUsername = response.getString("username");
                            Toast.makeText(LoginActivity.this, newUsername, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("JSON Parsing Error", e.toString());
                            putMessage.setText("Error: Unable to parse the response.");                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        putMessage.setText(error.getMessage());
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