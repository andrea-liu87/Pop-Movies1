package com.andrea.com.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements GridViewAdapter.clickHandler,
DialogFragment.passData{

    private GridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_numbers);
        mAdapter = new GridViewAdapter(getApplicationContext(),this);

        int columnNo = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnNo);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        new fetchMovieData().execute(NetworkUtilities.POPULAR);
    }


    @Override
    public void onCLick(Movie movieSelected) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra(Intent.EXTRA_PACKAGE_NAME, movieSelected);
        startActivity(intent);
    }


    //This inner class is responsible for download data and set it to Main Activity
    class fetchMovieData extends AsyncTask<String, Void, Movie[]>{
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


    //Menu related operation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_settings){
           new DialogFragment().show(getSupportFragmentManager(), getLocalClassName());
        }
        return super.onOptionsItemSelected(item);
    }


    //This interface method is passing data from DialogFragment on the sortorder selection
    @Override
    public void onSelectedSortOrder(int selectedData) {
        if(selectedData == 0){
            new fetchMovieData().execute(NetworkUtilities.POPULAR);
        }
        if(selectedData ==1){
            new fetchMovieData().execute(NetworkUtilities.TOP_RATED);
        }
        if(selectedData == 99){
            Toast.makeText(getApplicationContext(), "No selection from sort order", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
