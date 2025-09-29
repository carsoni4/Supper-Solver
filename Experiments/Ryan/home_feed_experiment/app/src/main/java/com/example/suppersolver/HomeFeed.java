package com.example.suppersolver;

import static android.os.SystemClock.sleep;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.java_websocket.handshake.ServerHandshake;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFeed extends AppCompatActivity implements WebSocketListener{
    private String userID, username, strRecipeId, recipeDescription;
    private Button homefeed_postBtn, nextBtn, creatorBtn;
    private TextView recipeName, textDescription, help;
    private JSONObject curRecipe;
    private JSONArray recipeArray;
    private String posterID;
    private MRecipe recipeModel;
    private static final String url = "http://coms-3090-025.class.las.iastate.edu:8080/";
    private static final String wsurl = "ws://coms-3090-025.class.las.iastate.edu:8080/wshomefeed/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_feed);

        homefeed_postBtn = findViewById(R.id.homefeed_postBtn);
        recipeName = findViewById(R.id.recipe_name);
        textDescription = findViewById(R.id.recipe_description);
        nextBtn = findViewById(R.id.homefeed_nextBtn);
        creatorBtn = findViewById(R.id.creator_btn);
        help = findViewById(R.id.help);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(HomeFeed.this, "cannot find user", Toast.LENGTH_SHORT).show();
        } else {
            userID = extras.getString("USERID");
            username = extras.getString("username");
        }

        WebSocketManager.getInstance().connectWebSocket(wsurl + userID);
        WebSocketManager.getInstance().setWebSocketListener(HomeFeed.this);


        homefeed_postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFeed.this, PostRecipe.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                startActivity(intent);  // go to SignupActivity
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WebSocketManager.getInstance().sendMessage("scrolled");
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });

        creatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posterID != null){
                    Intent intent = new Intent(HomeFeed.this, ProfileScreen.class);
                    intent.putExtra("username", username);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("creatorID", posterID);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        if(message.equals("No more recipes in database")){
            help.setText(message);
        }else{
            help.setText(message);
            try {
                curRecipe = new JSONObject(message);
                recipeName.setText(curRecipe.getString("name"));
                textDescription.setText(curRecipe.getString("description"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {}

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

}
