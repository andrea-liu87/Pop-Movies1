package com.andrea.com.popmovies.data;

import com.andrea.com.popmovies.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface DbDao {

    @Query("SELECT * FROM movies_table")
    LiveData<Movie[]> loadAllMovies();

    @Query("SELECT * FROM movies_table WHERE mid = :id")
    LiveData<Movie> loadMovie(int id);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMoviefromDB (Movie movie);
}
