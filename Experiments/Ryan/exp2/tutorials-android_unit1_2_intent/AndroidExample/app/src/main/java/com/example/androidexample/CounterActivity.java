package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private Button plus1; // define increase button variable
    private Button minus1;// define decrease button variable
    private Button x0; // define increase button variable
    private Button x2;
    private Button x5; // define increase button variable
    private Button divideBy2;
    private Button backBtn;     // define back button variable

    private int counter = 0;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        plus1 = findViewById(R.id.calc_plus_1);
        minus1 = findViewById(R.id.calc_minus_1);
        x0 = findViewById(R.id.calc_times_0);
        x2 = findViewById(R.id.calc_times_2);
        x5 = findViewById(R.id.calc_times_5);
        divideBy2 = findViewById(R.id.calc_divideBy_2);
        backBtn = findViewById(R.id.return_btn);

        /* when increase btn is pressed, counter++, reset number textview */
        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(++counter));
            }
        });

        /* when decrease btn is pressed, counter--, reset number textview */
        minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(--counter));
            }
        });

        x0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                numberTxt.setText(String.valueOf(counter));
            }
        });

        x2.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                counter *= 2;
                numberTxt.setText(String.valueOf(counter));
            }
        });

        x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter *= 5;
                numberTxt.setText(String.valueOf(counter));
            }
        });

        divideBy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter /= 2;
                numberTxt.setText(String.valueOf(counter));
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                if(counter == 0){
                    intent.putExtra("ZERO", "0");
                }else{
                    if(counter < 0) intent.putExtra("POSITIVITY", "negative");
                    else intent.putExtra("POSITIVITY", "positive");

                    if(counter % 2 == 0) intent.putExtra("ODDEVEN", "even");
                    else intent.putExtra("ODDEVEN", "odd");

                    intent.putExtra("NUM", String.valueOf(counter));
                }

                startActivity(intent);
            }
        });

    }
}