package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView message2;
    private Button myButton;
    private boolean isGreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        messageText.setText("Supper Solver");

        message2 = findViewById(R.id.sub_msg_txt);
        message2.setText("GREEN");

        myButton = findViewById(R.id.button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGreen) {
                    message2.setTextColor(Color.RED);
                    message2.setText("RED!");
                    isGreen = false;
                }
                else{
                    message2.setTextColor(Color.GREEN);
                    message2.setText("GREEN");
                    isGreen = true;
                }
            }
        });
    }
}