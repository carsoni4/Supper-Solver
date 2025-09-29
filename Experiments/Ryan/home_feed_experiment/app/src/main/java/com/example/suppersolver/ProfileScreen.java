package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;

import android.util.Base64;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;


import org.w3c.dom.Text;

public class ProfileScreen extends AppCompatActivity {

    private static final String URL_IMAGE = "";
    private String userID;

    private TextView welcomeMessage;
    private TextView description;

    private Button viewFriends;
    private Button viewMessages;
    private Button viewRecipes;
    private Button editProfile;

    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        description = findViewById(R.id.description);
        welcomeMessage = findViewById(R.id.welcomeMessage);
        viewFriends = findViewById(R.id.viewFriends);
        viewMessages = findViewById(R.id.viewMessages);
        viewRecipes = findViewById(R.id.viewRecipes);
        editProfile = findViewById(R.id.editProfile);
        profileImage = findViewById(R.id.profileImage);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            welcomeMessage.setText("Error getting username");
        } else {
            userID = extras.getString("USERID");
            String getUsername = extras.getString("username");  // this will come from LoginActivity
            String getDescription = extras.getString("description");

            description.setText(getDescription);
            welcomeMessage.setText("Welcome " + getUsername + "(" + userID + ")");
        }

        makeImageRequest();

        

        viewRecipes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, RecipeScreen.class);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, EditProfileScreen.class);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

    }

    private void makeImageRequest() {

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        profileImage.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }


}
