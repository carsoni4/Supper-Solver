package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;     // define message textview variable
    private Button counterButton;     // define counter button variable
    private TextView firstMsg;
    private TextView secondMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML
        firstMsg = findViewById(R.id.msgone);
        secondMsg = findViewById(R.id.msgtwo);

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            messageText.setText("Intent Example");
            firstMsg.setVisibility(View.INVISIBLE);
            secondMsg.setVisibility(View.INVISIBLE);
        } else {
            String number;
            if(extras.containsKey("ZERO")) number = extras.getString("ZERO");
            else {
                number = extras.getString("NUM");
                firstMsg.setText("the number was " + extras.getString("POSITIVITY"));
                secondMsg.setText("the number was " + extras.getString("ODDEVEN"));
            }
            messageText.setText("The number was " + number);
        }

        /* click listener on counter button pressed */
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });
    }
}