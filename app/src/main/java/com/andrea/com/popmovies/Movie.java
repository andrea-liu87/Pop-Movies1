package com.andrea.com.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private final String mtitle;
    private final String murl;
    private final String msynopsis;
    private final String muserRating;
    private final String mReleaseDate;

    public Movie(String mtitle, String murl, String msynopsis, String muserRating, String mReleaseDate) {
        this.mtitle = mtitle;
        this.murl = murl;
        this.msynopsis = msynopsis;
        this.muserRating = muserRating;
        this.mReleaseDate = mReleaseDate;
    }

    private Movie(Parcel parcel){
     mtitle = parcel.readString();
     murl = parcel.readString();
     msynopsis = parcel.readString();
     muserRating = parcel.readString();
     mReleaseDate = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mtitle);
        parcel.writeString(murl);
        parcel.writeString(msynopsis);
        parcel.writeString(muserRating);
        parcel.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

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
