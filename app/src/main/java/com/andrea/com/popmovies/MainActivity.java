package com.andrea.com.popmovies;

import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GridViewAdapter mAdapter;
    int columnNo = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_numbers);
        mAdapter = new GridViewAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,columnNo);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        new fetchMovieData().execute(NetworkUtilities.POPULAR);
    }

    public class fetchMovieData extends AsyncTask<String, Void, Movie[]>{
        @Override
        protected Movie[] doInBackground(String... modeData) {

            URL url = NetworkUtilities.buildUrl(modeData[0]);
            String jsonData = null;

            try {
                jsonData = NetworkUtilities.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Movie[] listMovie = {};
            try {
                listMovie = NetworkUtilities.jsonParsing(getApplicationContext(),jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listMovie;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mAdapter.setData(movies);
        }
    }
}
