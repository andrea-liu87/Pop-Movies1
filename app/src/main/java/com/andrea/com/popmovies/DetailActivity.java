package com.andrea.com.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mTitle = findViewById(R.id.detail_title);
        TextView mRelDate = findViewById(R.id.detail_reldate);
        TextView mRating = findViewById(R.id.detail_rating);
        ImageView mPoster = findViewById(R.id.detail_poster);
        TextView mSysnopsis = findViewById(R.id.detail_synopsis);

        Movie movie = getIntent().getParcelableExtra(Intent.EXTRA_PACKAGE_NAME);

        mTitle.setText(movie.getMtitle());
        mRelDate.append(movie.getmReleaseDate());
        mRating.append(movie.getMuserRating());
        mSysnopsis.setText(movie.getMsynopsis());

        Picasso.get()
                .load(NetworkUtilities.POSTER_PATH+NetworkUtilities.POSTER_SIZE+movie.getMurl())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
                .into(mPoster);
    }

}
