package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import static android.os.SystemClock.sleep;
import android.util.Base64;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import androidx.core.app.NotificationCompat;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Profile screen for a user. The display is different for the user's own profile versus other profiles.
 */
public class ProfileScreen extends AppCompatActivity implements WebSocketListener {

    private String serverURL = "ws://coms-3090-025.class.las.iastate.edu:8080/chat/";
    private String postURL = "http://coms-3090-025.class.las.iastate.edu:8080/users/addFriend/";
    private String getURL = "http://coms-3090-025.class.las.iastate.edu:8080/users/";
    private String getFriendURL = "http://coms-3090-025.class.las.iastate.edu:8080/users/getFriends/";
    private String getProfilePic = "http://coms-3090-025.class.las.iastate.edu:8080/image/user/";
    private String[] friendsList;
    private boolean alreadyFriends;

    private static final String URL_IMAGE = "";
    private String curUsername;
    private String userID;
    private String posterID;
    private String getCreatorUser;
    private boolean isAdmin;


    private TextView welcomeMessage;
    private TextView description;

    private Button viewFriends;
    private Button viewGroup;
    private Button editProfile;
    private Button addFriend;
    private Button backButton;
    private Button logout;

    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        description = findViewById(R.id.description);
        welcomeMessage = findViewById(R.id.welcomeMessage);
        viewFriends = findViewById(R.id.viewFriends);
        viewGroup = findViewById(R.id.viewGroup);
        editProfile = findViewById(R.id.editProfile);
        profileImage = findViewById(R.id.profileImage);
        addFriend = findViewById(R.id.addFriend);
        backButton = findViewById(R.id.back_button);
        logout = findViewById(R.id.logout);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            welcomeMessage.setText("Error getting username");
        } else {
            isAdmin = extras.getBoolean("isAdmin");
            curUsername = extras.getString("username");
            userID = extras.getString("USERID");
            posterID = extras.getString("creatorID");
            if (posterID != null) {
                getRequest(posterID);
                if(!userID.equals(posterID)){
                    getRequestF(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Friend Status", String.valueOf(alreadyFriends));
                            // This code only runs after getRequestF is done
                            if (!alreadyFriends) {
                                viewFriends.setVisibility(View.INVISIBLE);
                                viewGroup.setVisibility(View.INVISIBLE);
                                editProfile.setVisibility(View.INVISIBLE);
                                logout.setVisibility(View.INVISIBLE);
                                addFriend.setVisibility(View.VISIBLE);
                            } else if (alreadyFriends) {
                                viewFriends.setVisibility(View.INVISIBLE);
                                viewGroup.setVisibility(View.INVISIBLE);
                                editProfile.setVisibility(View.INVISIBLE);
                                logout.setVisibility(View.INVISIBLE);
                                addFriend.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                } else {
                    requestProfilePic(userID);
                    getRequest(userID);
                    viewFriends.setVisibility(View.VISIBLE);
                    viewGroup.setVisibility(View.VISIBLE);
                    editProfile.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.VISIBLE);
                    addFriend.setVisibility(View.INVISIBLE);
                }
            } else {
                requestProfilePic(userID);
                getRequest(userID);
                viewFriends.setVisibility(View.VISIBLE);
                viewGroup.setVisibility(View.VISIBLE);
                editProfile.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
                addFriend.setVisibility(View.INVISIBLE);
            }
        }

        WebSocketManager.getInstance().connectWebSocket(serverURL + userID);
        WebSocketManager.getInstance().setWebSocketListener(ProfileScreen.this);

        Log.d("Friend Status", String.valueOf(alreadyFriends));

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                if (isAdmin) {
                    Intent intent = new Intent(ProfileScreen.this, AdminHomeScreen.class);
                    intent.putExtra("username", curUsername);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("isAdmin", isAdmin);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProfileScreen.this, HomeFeed.class);
                    intent.putExtra("username", curUsername);
                    intent.putExtra("USERID", userID);
                    startActivity(intent);
                }
            }
        });

        viewFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, FriendScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        viewGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent intent = new Intent(ProfileScreen.this, GroupScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, EditProfileScreen.class);
                intent.putExtra("username", curUsername);
                if (posterID != null) {
                    intent.putExtra("creatorID", posterID);
                } else {
                    intent.putExtra("USERID", userID);
                }
                startActivity(intent);
            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postRequest();
                WebSocketManager.getInstance().sendMessage("[FRIEND] " + getCreatorUser);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent intent = new Intent(ProfileScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onWebSocketMessage(String message) {
        if (message.startsWith("[FRIENDNOTIFICATION]")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ProfileScreen.this, "FRIEND REQUEST RECEIVED", Toast.LENGTH_LONG).show();
                }
            });
        } else if (message.startsWith("[NOTIFICATION]")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ProfileScreen.this, "New Message From" + message.substring(14), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("Websocket Test", "Websocket Closed");
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("Websocket Test", "Websocket Opened");
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("Websocket Test", String.valueOf(ex));
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
                            getCreatorUser = response.getString("username");
                            welcomeMessage.setText(getCreatorUser);
                            if(response.getString("bio").equals("null")) {
                                description.setText("No description for this user has been added.");
                            } else {
                                description.setText(response.getString("bio"));
                            }
                            requestProfilePic(posterID);
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

    private void getRequestF(final Runnable onFriendsChecked) {
        JsonArrayRequest jsonARequest = new JsonArrayRequest(
                Request.Method.GET,
                getFriendURL + userID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response F", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curFriend = response.getJSONObject(i);
                                if (curFriend.getString("id").equals(posterID)) {
                                    alreadyFriends = true;
                                    break;
                                } else {
                                    alreadyFriends = false;
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        onFriendsChecked.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        onFriendsChecked.run();
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonARequest);
    }

    private void postRequest() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postURL + userID + "/" + posterID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response P", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error P", error.getMessage());
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

}
