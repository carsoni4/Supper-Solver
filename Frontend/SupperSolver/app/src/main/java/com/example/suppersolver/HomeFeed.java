package com.example.suppersolver;

import com.android.volley.toolbox.ImageRequest;
import com.example.suppersolver.R;
import static android.os.SystemClock.sleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The supper solver home page. The websocket enables users to browse all the recipes recommended from the database.
 * From the home page the user can access search, inventory, profile, and post. They can also access the creator's profile or the recipe screen of whatever recipe they are currently viewing.
 */
public class HomeFeed extends AppCompatActivity implements WebSocketListener{
    private String userID, username;
    private Button homefeed_postBtn, nextBtn, creatorBtn, viewMyProfile, recipeName, inventory, searchBtn;
    private ImageView recipeImage;
    private TextView textDescription;
    private JSONObject curRecipe, creatorUser;
    private TextView adText;
    private int recipeID;
    private int creatorID = -1;
    private int count;
    private Boolean isAdmin, isAdvertiser;
    private static final String wsurl = "ws://coms-3090-025.class.las.iastate.edu:8080/wshomefeed/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_feed);

        homefeed_postBtn = findViewById(R.id.homefeed_postBtn);
        recipeName = findViewById(R.id.recipe_btn);
        textDescription = findViewById(R.id.recipe_description);
        nextBtn = findViewById(R.id.homefeed_nextBtn);
        creatorBtn = findViewById(R.id.creator_btn);
        searchBtn = findViewById(R.id.search_btn);
        viewMyProfile = findViewById(R.id.view_my_profile);
        recipeImage = findViewById(R.id.recipe_image);
        inventory = findViewById(R.id.inventory_btn);
        adText = findViewById(R.id.adtext);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(HomeFeed.this, "cannot find user", Toast.LENGTH_SHORT).show();
        } else {
            userID = extras.getString("USERID");
            username = extras.getString("username");
        }

        creatorBtn.setVisibility(View.VISIBLE);
        recipeName.setVisibility(View.VISIBLE);
        adText.setVisibility(View.INVISIBLE);

        WebSocketManager.getInstance().connectWebSocket(wsurl + userID);
        WebSocketManager.getInstance().setWebSocketListener(HomeFeed.this);

        count = 1;

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent intent = new Intent(HomeFeed.this, SearchScreen.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        //switch to PostRecipe class
        homefeed_postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent intent = new Intent(HomeFeed.this, PostRecipe.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count < 5){
                    try {
                        WebSocketManager.getInstance().sendMessage("scrolled");
                    } catch (Exception e) {
                        Log.d("ExceptionSendMessage:", e.getMessage().toString());
                    }
                }else{
                    count = 0;
                    try {
                        WebSocketManager.getInstance().sendMessage("advertisement");
                    } catch (Exception e) {
                        Log.d("ExceptionSendMessage:", e.getMessage().toString());
                    }
                }
            }
        });

        creatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creatorID != -1) {
                    WebSocketManager.getInstance().disconnectWebSocket();
                    Intent intent = new Intent(HomeFeed.this, ProfileScreen.class);
                    intent.putExtra("username", username);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("creatorID", String.valueOf(creatorID));
                    startActivity(intent);
                }
            }
        });

        recipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creatorID != -1) {
                    WebSocketManager.getInstance().disconnectWebSocket();
                    Intent intent = new Intent(HomeFeed.this, RecipeScreen.class);
                    intent.putExtra("username", username);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("recipeID", String.valueOf(recipeID));
                    intent.putExtra("recipeName", recipeName.getText().toString());
                    intent.putExtra("rDescription", textDescription.getText().toString());
                    intent.putExtra("creatorName", creatorBtn.getText().toString());
                    startActivity(intent);
                }
            }
        });

        viewMyProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent intent = new Intent(HomeFeed.this, ProfileScreen.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketManager.getInstance().disconnectWebSocket();
                Intent intent = new Intent(HomeFeed.this, InventoryScreen.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                intent.putExtra("creatorID", creatorID);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                creatorBtn.setVisibility(View.VISIBLE);
                recipeName.setVisibility(View.VISIBLE);
                adText.setVisibility(View.INVISIBLE);
            }
        });
        if (!message.equals("No more recipes in database")) {
            try{
                curRecipe = new JSONObject(message);
                String type = curRecipe.getString("type");

                if (type.equals("recipe")) {
                    creatorUser = curRecipe.getJSONObject("user");
                    recipeID = curRecipe.getInt("ID");
                    creatorID = creatorUser.getInt("ID");
                    String u = creatorUser.getString("username");
                    String n = curRecipe.getString("name");
                    String d = curRecipe.getString("description");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            creatorBtn.setText("Created By:\n" + u);
                            recipeName.setText(n);
                            textDescription.setText(d);
                        }
                    });

                    String imgSrcUrl = curRecipe.getString("imgURL");
                    makeImageRequest(imgSrcUrl);
                }else if(type.equals("advertisement")){
                    String d = curRecipe.getString("description");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textDescription.setText(d);
                            creatorBtn.setVisibility(View.INVISIBLE);
                            recipeName.setVisibility(View.INVISIBLE);
                            adText.setVisibility(View.VISIBLE);
                        }
                    });

                    String imgSrcUrl = curRecipe.getString("imgURL");
                    makeImageRequest(imgSrcUrl);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }else if(message.equals("No more recipes in database")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nextBtn.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {}

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

    private void makeImageRequest(String imageurl) {

        ImageRequest imageRequest = new ImageRequest(
                imageurl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        recipeImage.setImageBitmap(response);
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
