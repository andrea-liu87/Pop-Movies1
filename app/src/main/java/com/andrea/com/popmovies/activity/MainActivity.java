package com.andrea.com.popmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.andrea.com.popmovies.Movie;
import com.andrea.com.popmovies.R;
import com.andrea.com.popmovies.adapter.GridViewAdapter;
import com.andrea.com.popmovies.data.MainViewModel;
import com.andrea.com.popmovies.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements GridViewAdapter.clickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private GridViewAdapter mAdapter;

    private int mode; //Mode 0=Popular, 1=Top rated, 2= favorites
    private final String BUNDLER_RECYCLER = "classname.recycler.layout";
    private Parcelable msavedInstance;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_numbers);
        mAdapter = new GridViewAdapter(this);

        int columnNo = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnNo);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        executeBaseSelection(getPrefMode());
    }


    private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getListMovies().observe(this, movies -> {
            if(movies == null){
                Toast.makeText(getApplicationContext(),"No movie on your favorite list",Toast.LENGTH_LONG).show();
                mode = 0;
                executeBaseSelection(mode);
            } else {
                mAdapter.setData(movies);
            }
        });
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
                listMovie = NetworkUtilities.jsonParsing(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listMovie;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mAdapter.setData(movies);
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(msavedInstance);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will register the OnSharedPreferenceListener and get movie mode
     * @return movie mode
     */
    private int getPrefMode(){
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreference.registerOnSharedPreferenceChangeListener(this);
        String mode = sharedPreference.getString(getString(R.string.pref_list_key),getString(R.string.pref_popular));
        if (Objects.requireNonNull(mode).equals(getString(R.string.pref_top_rated))){return 1;}
        else if (mode.equals(getString(R.string.pref_favorite))){return 2;}
        else {return 0;}
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String mode = sharedPreferences.getString(getString(R.string.pref_list_key),key);
        if (Objects.requireNonNull(mode).equals(getString(R.string.pref_top_rated))){executeBaseSelection(1);}
        else if (mode.equals(getString(R.string.pref_favorite))){executeBaseSelection(2);}
        else {executeBaseSelection(0);}
    }

    private void executeBaseSelection (int selectedMode) {
        if (selectedMode == 0) {
            new fetchMovieData().execute(NetworkUtilities.POPULAR);
        }
        if (selectedMode == 1) {
            new fetchMovieData().execute(NetworkUtilities.TOP_RATED);
        }
        if (selectedMode == 2) {
            setupViewModel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    //To retain same scroll position when configuration change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLER_RECYCLER, Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null){
            msavedInstance = savedInstanceState.getParcelable(BUNDLER_RECYCLER);
        }
    }
}
