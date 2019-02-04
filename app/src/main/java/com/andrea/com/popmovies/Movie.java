package com.andrea.com.popmovies;

public class Movie {

    String mtitle;
    String murl;
    String msynopsis;
    String muserRating;
    String mReleaseDate;

    public Movie(String mtitle, String murl, String msynopsis, String muserRating, String mReleaseDate) {
        this.mtitle = mtitle;
        this.murl = murl;
        this.msynopsis = msynopsis;
        this.muserRating = muserRating;
        this.mReleaseDate = mReleaseDate;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getMurl() {
        return murl;
    }

    public String getMsynopsis() {
        return msynopsis;
    }

    public String getMuserRating() {
        return muserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }
}
