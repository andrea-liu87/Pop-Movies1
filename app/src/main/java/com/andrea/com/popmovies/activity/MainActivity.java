package com.andrea.com.popmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.andrea.com.popmovies.DialogFragment;
import com.andrea.com.popmovies.Movie;
import com.andrea.com.popmovies.R;
import com.andrea.com.popmovies.adapter.GridViewAdapter;
import com.andrea.com.popmovies.data.MainViewModel;
import com.andrea.com.popmovies.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements GridViewAdapter.clickHandler,
        DialogFragment.passData {

    private GridViewAdapter mAdapter;

    private int mode; //Mode 0=Popular, 1=Top rated, 2= favorites

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

        executeBaseSelection(0);
    }

    private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getListMovies().observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(Movie[] movies) {
                if(movies == null){
                    Toast.makeText(getApplicationContext(),"No movie on your favorite list",Toast.LENGTH_LONG).show();
                    mode = 0;
                    executeBaseSelection(mode);
                } else {
                    mAdapter.setData(movies);
                }
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
        mode = selectedData;
        executeBaseSelection(mode);
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

}
