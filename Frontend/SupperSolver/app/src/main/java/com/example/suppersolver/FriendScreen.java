package com.example.suppersolver;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;

import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * Variable length list showing all of the user's friends
 */
public class FriendScreen extends AppCompatActivity {

    private String getURL = "http://coms-3090-025.class.las.iastate.edu:8080/users/getFriends/";

    private String userID;
    private String curUsername;
    private ArrayList<String> friendsArray;

    private Button backBtn;
    private ListView friendsList;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.friendScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Toast.makeText(FriendScreen.this, "Failed to get extras from intent", Toast.LENGTH_LONG).show();
        } else {
            curUsername = extras.getString("username");
            userID = extras.getString("USERID");
        }

        backBtn = findViewById(R.id.back_button);
        friendsList = findViewById(R.id.friendsList);
        friendsArray = new ArrayList<>();

        getRequest();

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FriendScreen.this, ProfileScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });
    }

    private void getRequest() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURL + userID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curObj = response.getJSONObject(i);
                                String curUser = curObj.getString("username");
                                friendsArray.add(curUser);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Log.d("Array", String.valueOf(friendsArray));
                        friendsList.setAdapter(new FriendsListAdapter(friendsArray, FriendScreen.this));
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
