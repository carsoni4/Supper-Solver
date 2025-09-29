package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewScreen extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_screen);

        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        myButton = findViewById(R.id.button);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewScreen.this, MainActivity.class);
                intent.putExtra("NEW", "hello");  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });
    }
}