package com.andrea.com.popmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.andrea.com.popmovies.BuildConfig;
import com.andrea.com.popmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtilities {

    //ALl static keyword to build the movie Api url
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String TOP_RATED = "top_rated";
    public static final String POPULAR = "popular";
    private static final String QUERY_PARAM = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;

    //All the static String keyword for Json parsing
    private static final String J_RESULT = "results";
    private static final String J_TITLE = "title";
    private static final String J_POSTER = "poster_path";
    private static final String J_OVERVIEW = "overview";
    private static final String J_USERRAT ="vote_average";
    private static final String J_RELDATE ="release_date";

    //This String static is the base url for the poster url
    public final static String POSTER_PATH = "http://image.tmdb.org/t/p";
    public final static String POSTER_SIZE = "/w185/";

    private NetworkUtilities(){}

    /**
     * Builds the URL used to retrieve the movie data. THe data
     * query based on popular or top_rated.
     *
     * @param mode popular or top_rated.
     * @return The URL to use to query the movie data.
     */
    public static URL buildUrl(String mode){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mode)
                .appendQueryParameter(QUERY_PARAM, API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL (builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method parses JSON from a web response and returns an array of Movie
     * as structure data.
     *
     * @param rawJsonData JSON response from server
     *
     * @return Array of Movie
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] jsonParsing(Context context, String rawJsonData) throws JSONException {
       if(rawJsonData == null){
           Toast.makeText(context,"No Json data from server",Toast.LENGTH_LONG).show();
           return null;
       }

        JSONObject wholeData = new JSONObject(rawJsonData);
        JSONArray mResult = wholeData.getJSONArray(J_RESULT);
        Movie[] listmovie = new Movie[mResult.length()];

        for(int i =0; i< mResult.length(); i++){
            JSONObject object = mResult.getJSONObject(i);
            String mTitle = object.optString(J_TITLE);
            String murl = object.optString(J_POSTER);
            String msynopsis = object.optString(J_OVERVIEW);
            String mUserRat = object.optString(J_USERRAT);
            String mRelDate = object.optString(J_RELDATE);

            listmovie[i] = new Movie(mTitle, murl, msynopsis, mUserRat, mRelDate);
        }

        return listmovie;
    }
}
