package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private Button increaseBtn; // define increase button variable
    private Button decreaseBtn; // define decrease button variable
    private Button backBtn;// define back button variable
    private Button increase5Btn;// created new button variable for +5 increase
    private Button decrease5Btn;// created new button variable for -5 decrease

    private int counter = 0;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        increaseBtn = findViewById(R.id.counter_increase_btn);
        decreaseBtn = findViewById(R.id.counter_decrease_btn);
        backBtn = findViewById(R.id.counter_back_btn);
        increase5Btn = findViewById(R.id.counter_increase5_btn);
        decrease5Btn = findViewById(R.id.counter_decrease5_btn);

        /* when increase btn is pressed, counter++, reset number textview */
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(++counter));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(--counter));
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class); // used to switch back to Main Activity page
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });

        /* created a new btn when pressed, counter +=, reset number textview */
        increase5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {numberTxt.setText(String.valueOf(counter+=5)); }
        });

        decrease5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {numberTxt.setText(String.valueOf(counter-=5)); }
        });

    }
}