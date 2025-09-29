package com.example.app;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button submit;
    private TextView msgResponse;
    private static final String URL_JSON_OBJECT = "http://coms-3090-025.class.las.iastate.edu:8080/rating/userCheck/123/1";
    private boolean hasRated;
    int rating = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        submit = findViewById(R.id.return_btn);
        msgResponse = findViewById(R.id.msgResponse);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                two.setBackgroundTintList(null);
                three.setBackgroundTintList(null);
                four.setBackgroundTintList(null);
                five.setBackgroundTintList(null);
                rating = 1;
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                two.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                three.setBackgroundTintList(null);
                four.setBackgroundTintList(null);
                five.setBackgroundTintList(null);
                rating = 2;
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                two.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                three.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                four.setBackgroundTintList(null);
                five.setBackgroundTintList(null);
                rating = 3;
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                two.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                three.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                four.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                five.setBackgroundTintList(null);
                rating = 4;
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                two.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                three.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                four.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                five.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                rating = 5;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjReq();
            }
        });
    }

    private void makeJsonObjReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        msgResponse.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }
}