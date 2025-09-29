package com.example.suppersolver;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Live chat with another user, displays message history upon opening.
 */
public class SendMessageScreen extends AppCompatActivity implements WebSocketListener{

    private String serverURL = "ws://coms-3090-025.class.las.iastate.edu:8080/chat/";

    private String userID;
    private String openUSER;
    private String curUsername;

    private TextView msgTxt;
    private EditText message;
    private Button backBtn;
    private Button sendMessageBtn;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmessage_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sendMessageScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.back_button);
        message = findViewById(R.id.sendMessage);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        msgTxt = findViewById(R.id.msgTxt);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(SendMessageScreen.this, "Failed to get extras from intent", Toast.LENGTH_LONG).show();
        } else {
            curUsername = extras.getString("username");
            userID = extras.getString("USERID");
            openUSER = extras.getString("openUSER");
        }

        Log.d("Websocket Test", serverURL + userID);
        WebSocketManager.getInstance().connectWebSocket(serverURL + userID);
        WebSocketManager.getInstance().setWebSocketListener(SendMessageScreen.this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SendMessageScreen.this, FriendScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Websocket Test","@" + openUSER + " " + message.getText().toString());
                WebSocketManager.getInstance().sendMessage("@" + openUSER + " " + message.getText().toString());
            }
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("Websocket Test","Received message: " + message);
        if (message.startsWith(curUsername) || message.startsWith(openUSER)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgTxt.setText(msgTxt.getText().toString() + "\n" + message);
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
        WebSocketManager.getInstance().sendMessage("[OPENUSER] " + openUSER);
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("Websocket Test", String.valueOf(ex));
    }

}
