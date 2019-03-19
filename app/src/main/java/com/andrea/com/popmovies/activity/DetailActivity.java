package com.andrea.com.popmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrea.com.popmovies.Movie;
import com.andrea.com.popmovies.R;
import com.andrea.com.popmovies.adapter.ReviewAdapter;
import com.andrea.com.popmovies.adapter.TrailerAdapter;
import com.andrea.com.popmovies.data.AppDatabase;
import com.andrea.com.popmovies.data.AppExecutors;
import com.andrea.com.popmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity implements TrailerAdapter.clickHandler {
    @BindView(R.id.detail_title) TextView mTitle;
    @BindView(R.id.detail_reldate) TextView mRelDate;
    @BindView(R.id.detail_rating) TextView mRating;
    @BindView(R.id.detail_poster) ImageView mPoster;
    @BindView(R.id.detail_synopsis) TextView mSysnopsis;

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    private AppDatabase mDatabase;
    private Movie movie;
    private Boolean isFav = false;

    private ReviewAdapter mAdapter; //This adapter to set all review
    private TrailerAdapter mTrailerAdapter; //THis adapter to set trailer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            movie = getIntent().getParcelableExtra(Intent.EXTRA_PACKAGE_NAME);
        }

        mDatabase = AppDatabase.getInstance(getApplicationContext());
        isFavMovie(movie.getMid());

        mTitle.setText(movie.getMtitle());
        mRelDate.append(movie.getmReleaseDate());
        mRating.append(movie.getMuserrating());
        mSysnopsis.setText(movie.getMsynopsis());

        Picasso.get()
                .load(NetworkUtilities.POSTER_PATH+NetworkUtilities.POSTER_SIZE+movie.getMurl())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
                .into(mPoster);

        generateReviewRecylView();
        generateTrailerRecylView(getApplicationContext());
    }

    @Override
    public void onCLick(String trailerUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri url = Uri.parse(NetworkUtilities.YOUTUBE+trailerUrl).buildUpon()
            .build();
        intent.setData(url);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //Menu related operation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        if (isFav) {menu.findItem(R.id.detail_favorite).setIcon(R.drawable.ic_favourite_color);}
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.detail_favorite && !isFav){
            item.setIcon(R.drawable.ic_favourite_color);
            AppExecutors.getInstance().diskIO().execute(() -> {
                mDatabase.tableDao().insertMovie(movie);
                Log.i(LOG_TAG,"Insert movie to table is done");
            });
        }
        if(item.getItemId() == R.id.detail_favorite && isFav){
            item.setIcon(R.drawable.ic_favourite_white);
            AppExecutors.getInstance().diskIO().execute(() -> {
                mDatabase.tableDao().deleteMoviefromDB(movie);
                Log.i(LOG_TAG,"Delete movie from table is done");
            });
        }
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will check whether the movie is in Favorite list
     * @param id the movied id
     */
    private void isFavMovie (int id){
        try {
            final LiveData<Movie> movieret = mDatabase.tableDao().loadMovie(id);
            movieret.observe(this, movie -> {
                if (movie != null){
                        isFav = true;
                        Log.d(LOG_TAG, movie.getMtitle() + " is inside fav list");
                }
            });
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will fetch data from API and generate recycler view for movie review
     */
    private void generateReviewRecylView (){
        RecyclerView recyclerView = findViewById(R.id.rv_review);
        mAdapter = new ReviewAdapter(null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        new fetchReviewData().execute(movie.getMid());
    }

    private void generateTrailerRecylView (Context context){
        RecyclerView recyclerView = findViewById(R.id.rv_trailer);
        mTrailerAdapter = new TrailerAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mTrailerAdapter);
        new fetchVideoData().execute(movie.getMid());
    }


    //This inner class is responsible for download review data and set it to Main Activity
    class fetchReviewData extends AsyncTask<Integer, Void, String[]> {
        @Override
        protected String[] doInBackground(Integer... movieId) {

            URL url = NetworkUtilities.buildUrlforReview(movieId[0]);
            String jsonData = null;

            try {
                jsonData = NetworkUtilities.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] listReview = {};
            try {
                listReview = NetworkUtilities.jsonParsingGetReview(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listReview;
        }

        @Override
        protected void onPostExecute(String[] reviews) {
            mAdapter.setData(reviews);
        }
    }

    /**
     * This class to fetch data for trailer on background thread
     */
    class fetchVideoData extends AsyncTask<Integer, Void, String[]> {
        @Override
        protected String[] doInBackground(Integer... movieId) {

            URL url = NetworkUtilities.buildUrlforVideo(movieId[0]);
            String jsonData = null;

            try {
                jsonData = NetworkUtilities.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] listVideo = {};
            try {
                listVideo = NetworkUtilities.jsonParsingGetVideo(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listVideo;
        }

        @Override
        protected void onPostExecute(String[] videos) {
            mTrailerAdapter.setData(videos);
        }
    }
}
