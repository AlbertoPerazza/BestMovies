package com.example.movieapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.database.MovieProvider;
import com.example.movieapp.database.MovieTableHelper;
import com.example.movieapp.utils.EndlessScrollListener;
import com.example.movieapp.utils.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    public static final int MY_ID = 1;
    public static final String staticUrl = "https://api.themoviedb.org/3/movie/popular?api_key=c98bc68bed14ed23cdcfc19103d97c20&language=it-IT&region=IT&page=";
    RecyclerView recyclerView;
    MovieAdapter adapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getApi();
        recyclerView = findViewById(R.id.movieList);
        GridLayoutManager manager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            manager = new GridLayoutManager(this,2);
        }else {
            manager = new GridLayoutManager(this, 3);
        }
        recyclerView.setLayoutManager(manager);
        getSupportLoaderManager().initLoader(MY_ID,null,this);
        recyclerView.addOnScrollListener(new EndlessScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getApi();
            }
        });
    }
    public void getApi() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = staticUrl + StaticValues.pagePosition;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("results");
                    for (int i = 0 ; i<array.length();i++){
                        JSONObject movie = array.getJSONObject(i);
                        String checkId = movie.getString("id");
                        Cursor checkCursor = getContentResolver().query(MovieProvider.MOVIES_URI,null,MovieTableHelper.API_ID + " = " + checkId,null,null );
                        if (checkCursor.getCount()<=0){
                            ContentValues values = new ContentValues();
                            values.put(MovieTableHelper.TITLE,movie.getString("title"));
                            values.put(MovieTableHelper.DESCRIPTION,movie.getString("overview"));
                            values.put(MovieTableHelper.IMAGE_LIST,movie.getString("poster_path"));
                            values.put(MovieTableHelper.IMAGE_DETAILS,movie.getString("backdrop_path"));
                            values.put(MovieTableHelper.API_ID,checkId);
                            getContentResolver().insert(MovieProvider.MOVIES_URI,values);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, MovieProvider.MOVIES_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (recyclerView.getAdapter() == null) {
            adapter = new MovieAdapter(this, data);
            recyclerView.setAdapter(adapter);
        }else{
            adapter.setmCursor(data);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursor = null;
        adapter.notifyDataSetChanged();
    }
}
