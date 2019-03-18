package com.andrea.com.popmovies.data;

import android.app.Application;

import com.andrea.com.popmovies.Movie;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private final LiveData<Movie []> listMovies;

    public MainViewModel (Application application){
        super (application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        listMovies = db.tableDao().loadAllMovies();
    }

    public LiveData<Movie []> getListMovies(){
        return listMovies;
    }
}
