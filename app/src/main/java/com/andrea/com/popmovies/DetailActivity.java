package com.andrea.com.popmovies;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.detail_title) TextView mTitle;
    @BindView(R.id.detail_reldate) TextView mRelDate;
    @BindView(R.id.detail_rating) TextView mRating;
    @BindView(R.id.detail_poster) ImageView mPoster;
    @BindView(R.id.detail_synopsis) TextView mSysnopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

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
