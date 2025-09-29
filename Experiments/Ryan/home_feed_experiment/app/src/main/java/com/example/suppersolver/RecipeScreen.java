package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeScreen extends AppCompatActivity {
    private Button commentsBtn;
    private Button ratingsBtn;
    private String userID;
    private TextView createdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_screen);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recipe), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        commentsBtn = findViewById(R.id.recipe_comments_btn);
        ratingsBtn = findViewById(R.id.recipe_ratings_btn);
        createdBy = findViewById(R.id.recipe_creator);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            createdBy.setText("Error: user unknown");
        }
        else {
            if (extras.containsKey("USERID")) {
                userID = extras.getString("USERID");
                createdBy.setText("Created By: User #" + userID);
            }
        }

        commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeScreen.this, CommentScreen.class);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        ratingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeScreen.this, RatingScreen.class);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });
    }
}
