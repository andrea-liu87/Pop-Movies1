package com.andrea.com.popmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrea.com.popmovies.Movie;
import com.andrea.com.popmovies.R;
import com.andrea.com.popmovies.data.AppDatabase;
import com.andrea.com.popmovies.data.AppExecutors;
import com.andrea.com.popmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.detail_title) TextView mTitle;
    @BindView(R.id.detail_reldate) TextView mRelDate;
    @BindView(R.id.detail_rating) TextView mRating;
    @BindView(R.id.detail_poster) ImageView mPoster;
    @BindView(R.id.detail_synopsis) TextView mSysnopsis;

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    private AppDatabase mDatabase;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        movie = getIntent().getParcelableExtra(Intent.EXTRA_PACKAGE_NAME);

        mDatabase = AppDatabase.getInstance(getApplicationContext());

        mTitle.setText(movie.getMtitle());
        mRelDate.append(movie.getmReleaseDate());
        mRating.append(movie.getMuserrating());
        mSysnopsis.setText(movie.getMsynopsis());

        Picasso.get()
                .load(NetworkUtilities.POSTER_PATH+NetworkUtilities.POSTER_SIZE+movie.getMurl())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
                .into(mPoster);
    }

    //Menu related operation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.detail_favorite){
            item.setIcon(R.drawable.ic_favourite_color);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.tableDao().insertMovie(movie);
                    Log.i(LOG_TAG,"Insert movie to table is done");
                }
            });
        }
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
