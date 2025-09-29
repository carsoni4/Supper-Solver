package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView usernameText;  // define username textview variable
    private TextView textNew;
    private Button loginButton;     // define login button variable
    private Button signupButton; // define signup button variable
    private Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        usernameText = findViewById(R.id.main_username_txt);// link to username textview in the Main activity XML
        loginButton = findViewById(R.id.main_login_btn);    // link to login button in the Main activity XML
        signupButton = findViewById(R.id.main_signup_btn);  // link to signup button in the Main activity XML
        myButton = findViewById(R.id.button);
        textNew = findViewById(R.id.textnew);

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            messageText.setText("Home Page");
            usernameText.setVisibility(View.INVISIBLE);             // set username text invisible initially
        } else {
            if (extras.containsKey("USERNAME")) {
                messageText.setText("Welcome to Supper Solver!!");
                messageText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                usernameText.setText(extras.getString("USERNAME")); // this will come from LoginActivity
                loginButton.setVisibility(View.INVISIBLE);              // set login button invisible
                signupButton.setVisibility(View.INVISIBLE);             // set signup button invisible
            }
            if (extras.containsKey("NEW")) {
                textNew.setText(extras.getString("NEW"));
                usernameText.setVisibility(View.INVISIBLE);
                messageText.setText("Home Page");
            }
            if (extras.containsKey("USER")){
                String myString = "Welcome, " + extras.getString("USER");
                String loginMsg = "You may now login";
                messageText.setText(myString);
                usernameText.setText(loginMsg);
                signupButton.setVisibility(View.INVISIBLE);
            }
        }

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        /* open new screen on click */
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when button is pressed, use intent to switch to the new screen */
                Intent intent = new Intent(MainActivity.this, NewScreen.class);
                startActivity(intent);
            }
        });
    }
}