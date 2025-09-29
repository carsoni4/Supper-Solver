package com.example.suppersolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Custom adapter for displaying the filtered listView for search screen.
 */
public class AdminHomeAdapter extends BaseAdapter implements ListAdapter, Filterable {
    private ArrayList<String> originalList;
    private ArrayList<String> filteredList;
    private ArrayList<String> creatorIDs;  // New list to hold creator IDs
    private ArrayList<String> filteredCreatorIDs;
    private String currentFilter;
    private Context context;
    private ItemFilter itemFilter = new ItemFilter();

    public AdminHomeAdapter(ArrayList<String> list, ArrayList<String> creatorIDs, String currentFilter, Context context) {
        this.originalList = new ArrayList<>(list);
        this.filteredList = new ArrayList<>(list);
        this.creatorIDs = new ArrayList<>(creatorIDs);  // Store creator IDs
        this.currentFilter = currentFilter;
        this.context = context;
        this.filteredCreatorIDs = new ArrayList<>(creatorIDs); // Create a filtered list for creatorIDs

    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int pos) {
        return filteredList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_layoutadminhome, null);
        }

        // Handle TextView and display string from filtered list
        TextView tvContact = (TextView) view.findViewById(R.id.tvContact);
        tvContact.setText(filteredList.get(position));

        // Handle buttons and add onClickListeners
        Button callbtn = (Button) view.findViewById(R.id.btn);
        Button deletebtn = (Button) view.findViewById(R.id.deletebtn);

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Bundle extras = activity.getIntent().getExtras();
                String userID = extras.getString("USERID");
                String username = extras.getString("username");
                String creatorID = filteredCreatorIDs.get(position);
                boolean isAdmin = extras.getBoolean("isAdmin");
                if (currentFilter.equals("User")) {
                    Intent intent = new Intent(activity, ProfileScreen.class);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("creatorID", creatorID);
                    intent.putExtra("username", username);
                    intent.putExtra("isAdmin", isAdmin);
                    activity.startActivity(intent);
                } else if (currentFilter.equals("Recipe")) {
                    Intent intent = new Intent(activity, RecipeScreen.class);
                    intent.putExtra("USERID", userID);
                    intent.putExtra("recipeID", creatorID);
                    intent.putExtra("username", username);
                    intent.putExtra("isAdmin", isAdmin);
                    activity.startActivity(intent);
                }
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String creatorID = filteredCreatorIDs.get(position);
                if (currentFilter.equals("User")) {
                    deleteRequest( "http://coms-3090-025.class.las.iastate.edu:8080/users/", creatorID);
                } else if (currentFilter.equals("Recipe")) {
                    deleteRequest("http://coms-3090-025.class.las.iastate.edu:8080/recipe/", creatorID);
                }
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final List<String> list = originalList;
            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<>(count);
            final ArrayList<String> nCreatorList = new ArrayList<>(count);  // Filtered creatorIDs list

            for (int i = 0; i < count; i++) {
                String item = list.get(i);
                if (item.toLowerCase().contains(filterString)) {
                    nlist.add(item);
                    nCreatorList.add(creatorIDs.get(i));  // Add corresponding creatorID

                }
            }

            results.values = nlist;
            results.count = nlist.size();
            filteredCreatorIDs = nCreatorList;  // Update filtered creatorIDs list

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

    private void deleteRequest(String url, String curID) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.DELETE,
                url + curID,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
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
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

}