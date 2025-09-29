package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The inventory displays a list of all the user's ingredients. They can post new ingredients or delete existing ones.
 */
public class InventoryScreen extends AppCompatActivity {

    private Button backButton, sendBtn;
    private EditText editName, editQty;
    private String userID, username;
    private ListView ingredientList;
    private ArrayList<String> ingredientsArray;
    private int[] ingredientIds;
    private static final String getUrl = "http://coms-3090-025.class.las.iastate.edu:8080/ingredients/";
    private static final String postUrl = "http://coms-3090-025.class.las.iastate.edu:8080/ingredients";
    private String getInGroupURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/userInGroup/";
    private String getGroupIDURL = "http://coms-3090-025.class.las.iastate.edu:8080/group/userGroupID/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_screen);

        backButton = findViewById(R.id.inv_back_button);
        ingredientList = findViewById(R.id.ingredientList);
        sendBtn = findViewById(R.id.send_ingredient);
        editName = findViewById(R.id.ingredient_name);
        editQty = findViewById(R.id.ingredient_qty);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
        }
        else {
            if (extras.containsKey("USERID")) {
                userID = extras.getString("USERID");
                username = extras.getString("username");
            }
        }

        ingredientsArray = new ArrayList<>();
        ingredientIds = new int[50];
        getInGroupRequest();

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(InventoryScreen.this, HomeFeed.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postRequest();
            }
        });
    }

    private void postRequest() {

        JSONObject postBody = new JSONObject();
        try{
            postBody.put("recipeID", -1);
            postBody.put("userID", Integer.parseInt(userID));
            postBody.put("name", editName.getText().toString());

            Double qty = Double.valueOf(editQty.getText().toString());
            postBody.put("quantity", qty);
        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postUrl,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(InventoryScreen.this, InventoryScreen.class);
                        intent.putExtra("username", username);
                        intent.putExtra("USERID", userID);
                        startActivity(intent);
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

    private void getRequest(String path, String id) {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getUrl + path + id,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curIngredient = response.getJSONObject(i);
                                String iname = curIngredient.getString("name");
                                String iqty = String.valueOf(curIngredient.getDouble("quantity"));
                                ingredientIds[i] = curIngredient.getInt("id");
                                ingredientsArray.add(iname + " x" + iqty);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ingredientList.setAdapter(new InventoryAdapter(ingredientsArray, ingredientIds, InventoryScreen.this));
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

    private void getInGroupRequest() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                getInGroupURL + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse the response as a boolean
                        boolean isUserInGroup = Boolean.parseBoolean(response); // Convert response to boolean
                        if(isUserInGroup) {
                            getGroupIDRequest();
                        }else{
                            getRequest("user/", userID);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error (inGroupCheck)", error.toString());
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getGroupIDRequest() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                getGroupIDURL + userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getRequest("groupID/", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error (groupid)", error.toString());
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
