package com.example.suppersolver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Search by recipes or users. Click on one to view a recipe screen or view a profile.
 */
public class SearchScreen extends AppCompatActivity {

    // URLs for server requests
    private String getURLUsers = "http://coms-3090-025.class.las.iastate.edu:8080/users";
    private String getURLRecipes = "http://coms-3090-025.class.las.iastate.edu:8080/recipes";

    // Variables to hold user and search data
    private String userID;
    private String username;
    private ArrayList<String> creatorIDList;
    private ArrayList<String> searchList;

    private ListView searchListView;
    private SearchScreenAdapter arrayAdapter;

    private Button backBtn;
    private Button filterUsers;
    private Button filterRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.search_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.back_button);
        filterUsers = findViewById(R.id.filterUsers);
        filterRecipes = findViewById(R.id.filterRecipes);

        searchList = new ArrayList<>();
        creatorIDList = new ArrayList<>();
        searchListView = findViewById(R.id.searchListView);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(SearchScreen.this, "Failed to get extras from intent", Toast.LENGTH_LONG).show();
        } else {
            userID = extras.getString("USERID");
            username = extras.getString("username");
        }

        // Initial request for users
        getRequestUsers();

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchScreen.this, HomeFeed.class);
                intent.putExtra("username", username);
                intent.putExtra("USERID", userID);
                startActivity(intent);
            }
        });

        // Set listeners for filter buttons
        filterUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList.clear();
                creatorIDList.clear();
                getRequestUsers();
            }
        });

        filterRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList.clear();
                creatorIDList.clear();
                getRequestRecipes();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String newText) {

                arrayAdapter.getFilter().filter(newText);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Sends a GET request to retrieve a list of users from the server and updates the ListView with the retrieved data.
     */
    private void getRequestUsers() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURLUsers,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curObj = response.getJSONObject(i);
                                String username = curObj.getString("username");
                                String creatorID = curObj.getString("id");
                                searchList.add(username);
                                creatorIDList.add(creatorID);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        arrayAdapter = new SearchScreenAdapter(searchList, creatorIDList,"User",SearchScreen.this);
                        searchListView.setAdapter(arrayAdapter);
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

    /**
     * Sends a GET request to retrieve a list of recipes from the server and updates the ListView with the retrieved data.
     */
    private void getRequestRecipes() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                getURLRecipes,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject curObj = response.getJSONObject(i);
                                String recipeName = curObj.getString("name");
                                String creatorID = curObj.getString("id");
                                creatorIDList.add(creatorID);
                                searchList.add(recipeName);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        arrayAdapter = new SearchScreenAdapter(searchList, creatorIDList,"Recipe",SearchScreen.this);
                        searchListView.setAdapter(arrayAdapter);
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
