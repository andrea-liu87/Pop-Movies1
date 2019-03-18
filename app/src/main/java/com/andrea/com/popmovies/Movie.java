package com.andrea.com.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "movies_table")
public class Movie implements Parcelable {

    @PrimaryKey
    private int mid;
    private final String mtitle;
    private String murl;
    private String msynopsis;
    private String muserrating;
    private String mreleasedate;

    public Movie(int mid, String mtitle,String murl, String msynopsis, String muserrating, String mreleasedate) {
        this.mid = mid;
        this.murl = murl;
        this.mtitle = mtitle;
        this.msynopsis = msynopsis;
        this.muserrating = muserrating;
        this.mreleasedate = mreleasedate;
    }

    private Movie(Parcel parcel){
        mid = parcel.readInt();
     mtitle = parcel.readString();
     murl = parcel.readString();
     msynopsis = parcel.readString();
     muserrating = parcel.readString();
     mreleasedate = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mid);
        parcel.writeString(mtitle);
        parcel.writeString(murl);
        parcel.writeString(msynopsis);
        parcel.writeString(muserrating);
        parcel.writeString(mreleasedate);
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

    public int getMid() {
        return mid;
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

    public String getMuserrating() {
        return muserrating;
    }

    public String getmReleaseDate() {
        return mreleasedate;
    }

    public String getMreleasedate() {
        return mreleasedate;
    }

}
