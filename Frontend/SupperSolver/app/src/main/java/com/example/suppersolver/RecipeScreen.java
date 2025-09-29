package com.example.suppersolver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Screen for viewing an individual recipe. Shows image, name, description, and rating.
 */
public class RecipeScreen extends AppCompatActivity {
    private Button commentsBtn;
    private Button backButton;
    private Button rateBtn;
    private String userID, recipeID, username;
    private boolean isAdmin;
    private TextView createdBy, recipeName, description;
    private ImageView recipeImage;
    private Button starOne, starTwo, starThree, starFour, starFive;
    private Boolean hasRated;
    private int curRating;

    private static final String firstImgUrl = "http://coms-3090-025.class.las.iastate.edu:8080/image/first/";
    private static final String getRecipeURL = "http://coms-3090-025.class.las.iastate.edu:8080/recipe/";
    private static final String ratingUrl = "http://coms-3090-025.class.las.iastate.edu:8080/rating";


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
        createdBy = findViewById(R.id.recipe_creator);
        recipeImage = findViewById(R.id.recipe_image);
        recipeName = findViewById(R.id.recipe_name);
        description = findViewById(R.id.recipe_description);
        backButton = findViewById(R.id.back_button);
        starOne = findViewById(R.id.star_one);
        starTwo = findViewById(R.id.star_two);
        starThree = findViewById(R.id.star_three);
        starFour = findViewById(R.id.star_four);
        starFive = findViewById(R.id.star_five);
        rateBtn = findViewById(R.id.rate_button);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            createdBy.setText("Error: user unknown");
        }
        else {
            if (extras.containsKey("USERID")) {
                userID = extras.getString("USERID");
                username = extras.getString("username");
                recipeID = extras.getString("recipeID");
                isAdmin = extras.getBoolean("isAdmin");
            }
        }

        getRequest(recipeID);
        getRequestRecipe(recipeID);
        hasRated(recipeID, userID);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isAdmin) {
                    Intent intent = new Intent(RecipeScreen.this, AdminHomeScreen.class);
                    intent.putExtra("username", username);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("isAdmin", isAdmin);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RecipeScreen.this, HomeFeed.class);
                    intent.putExtra("username", username);
                    intent.putExtra("USERID", userID);
                    startActivity(intent);
                }
            }
        });

        commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeScreen.this, CommentScreen.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                intent.putExtra("recipeID", recipeID);
                startActivity(intent);
            }
        });

        starOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasRated){
                    setStars(1);
                    curRating = 1;
                }
            }
        });

        starTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasRated){
                    setStars(2);
                    curRating = 2;
                }
            }
        });

        starThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasRated){
                    setStars(3);
                    curRating = 3;
                }
            }
        });

        starFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasRated){
                    setStars(4);
                    curRating = 4;
                }
            }
        });

        starFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasRated){
                    setStars(5);
                    curRating = 5;
                }
            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                postRating(curRating);
                hasRated = true;
                rateBtn.setVisibility(View.INVISIBLE);
           }
        });
    }

    private void getRequest(String rid) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                firstImgUrl + rid,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String imgSource = response.getString("imgUrl");
                            makeImageRequest(imgSource);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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

    private void getRequestRecipe(String rid) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                getRecipeURL + rid,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            recipeName.setText(response.getString("name"));
                            description.setText(response.getString("description"));
                            createdBy.setText("Created By: " + response.getJSONObject("user").getString("username"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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

    private void makeImageRequest(String imageurl) {

        ImageRequest imageRequest = new ImageRequest(
                imageurl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        recipeImage.setImageBitmap(response);
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

    private void hasRated(String rid, String uid) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                ratingUrl + "/" + uid + "/" + rid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean rated = Boolean.parseBoolean(response);
                        hasRated = rated;
                        if (rated) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    rateBtn.setVisibility(View.INVISIBLE);
                                }
                            });
                            avgRating(recipeID);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void postRating(int rating) {

        JSONObject postBody = new JSONObject();
        try{
            postBody.put("rating", rating);
            postBody.put("recipeID", Integer.parseInt(recipeID));
            postBody.put("userID", Integer.parseInt(userID));
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                ratingUrl,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        avgRating(recipeID);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void avgRating(String rid) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                ratingUrl + "/avg/" + rid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int val = Integer.parseInt(response);
                        setStars(val);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void setStars(int num){
        starOne.setBackgroundResource(R.drawable.star_filled);
        starTwo.setBackgroundResource(R.drawable.star_filled);
        starThree.setBackgroundResource(R.drawable.star_filled);
        starFour.setBackgroundResource(R.drawable.star_filled);
        starFive.setBackgroundResource(R.drawable.star_filled);
        if(num < 5){
            starFive.setBackgroundResource(R.drawable.star);
            if(num < 4){
                starFour.setBackgroundResource(R.drawable.star);
                if(num < 3){
                    starThree.setBackgroundResource(R.drawable.star);
                    if(num < 2){
                        starTwo.setBackgroundResource(R.drawable.star);
                    }
                }
            }
        }
    }
}
