package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.java_websocket.handshake.ServerHandshake;

public class GroupMessageScreen extends AppCompatActivity implements WebSocketListener {
    private String serverURL = "ws://coms-3090-025.class.las.iastate.edu:8080/chat/";

    private String curUsername;
    private String userID;

    private TextView msgTxt;
    private EditText message;
    private Button backBtn;
    private Button sendMessageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupmessage_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.groupMessageScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        message = findViewById(R.id.sendMessage);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        msgTxt = findViewById(R.id.msgTxt);
        backBtn = findViewById(R.id.back_button);

        Bundle extras = getIntent().getExtras();

        curUsername = extras.getString("username");
        userID = extras.getString("USERID");

        Log.d("Websocket Test", serverURL + userID);
        WebSocketManager.getInstance().connectWebSocket(serverURL + userID);
        WebSocketManager.getInstance().setWebSocketListener(GroupMessageScreen.this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GroupMessageScreen.this, GroupScreen.class);
                intent.putExtra("username", curUsername);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Websocket Test","#" + message.getText().toString());
                WebSocketManager.getInstance().sendMessage("#" + message.getText().toString());
            }
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("Websocket Test","Received message: " + message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgTxt.setText(msgTxt.getText().toString() + "\n" + message);
            }
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("Websocket Test", "Websocket Closed");
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("Websocket Test", "Websocket Opened");
        WebSocketManager.getInstance().sendMessage("[OPENGROUP]");
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("Websocket Test", String.valueOf(ex));
    }
}
