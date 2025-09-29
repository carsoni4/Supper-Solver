package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Sign up as a new user. Input name, username, and password.
 */
public class SignupScreen extends AppCompatActivity {
    private Button signupBtn;
    private EditText editName;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPass;
    private CheckBox checkboxUser, checkboxAdmin, checkboxAdvertiser;
    private String userID;
    private String url = "http://coms-3090-025.class.las.iastate.edu:8080/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        signupBtn = findViewById(R.id.signup_btn);
        editName = findViewById(R.id.name);
        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        editConfirmPass = findViewById(R.id.confirm_password);
        checkboxUser = findViewById(R.id.checkbox_user);
        checkboxAdmin = findViewById(R.id.checkbox_admin);
        checkboxAdvertiser = findViewById(R.id.checkbox_advertiser);

        checkboxUser.setChecked(true);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String confirm = editConfirmPass.getText().toString();
                if(name.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()){
                    Toast.makeText(SignupScreen.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals(confirm)) {
                    postRequest();
                }
                else{
                    Toast.makeText(SignupScreen.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (isChecked) {
                // Deselect other checkboxes
                if (buttonView != checkboxUser) checkboxUser.setChecked(false);
                if (buttonView != checkboxAdmin) checkboxAdmin.setChecked(false);
                if (buttonView != checkboxAdvertiser) checkboxAdvertiser.setChecked(false);
            }
        };

        // Assign the same listener to all checkboxes
        checkboxUser.setOnCheckedChangeListener(listener);
        checkboxAdmin.setOnCheckedChangeListener(listener);
        checkboxAdvertiser.setOnCheckedChangeListener(listener);
    }

    private void postRequest() {
        JSONObject postBody = new JSONObject(); //create post body object
        try{
            postBody.put("name", editName.getText().toString());
            postBody.put("username", editUsername.getText().toString());
            postBody.put("password", editPassword.getText().toString());
            if (checkboxAdmin.isChecked()) {
                postBody.put("isAdmin", true);
            }
            else if (checkboxAdvertiser.isChecked()) {
                postBody.put("isAdvertiser", true);
            }
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
                            Toast.makeText(SignupScreen.this, "Signed Up", Toast.LENGTH_SHORT).show();
                            userID = response.getString("id");
                            Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
                            intent.putExtra("USERID", userID);
                            startActivity(intent);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupScreen.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    /*private void deleteReq(String id) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                url + id,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
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
    }*/
}
