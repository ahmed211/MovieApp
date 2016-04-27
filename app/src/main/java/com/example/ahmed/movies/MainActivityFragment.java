package com.example.ahmed.movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {
    private ArrayList<String> posterURLs = new ArrayList<>();
    ArrayList <MovieData> movieData=new ArrayList<>();
    private ImageAdapter imageAdapter ;
    GridView gridView;
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        posterURLs.clear();
        movieData.clear();

        FetchMovieTask movieTask;
        int id = item.getItemId();
        if (R.id.popular == id)
        {
            movieTask = new FetchMovieTask("http://api.themoviedb.org/3/movie/now_playing");
            movieTask.execute();

            return true;
        }

        else if(R.id.rated == id)
        {
            movieTask = new FetchMovieTask("http://api.themoviedb.org/3/movie/top_rated");
            movieTask.execute();
            return true;

        }
        else if(R.id.favorit == id)
        {
            Intent i = new Intent(getActivity(),FavoritMovies.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        movieData.clear();
        posterURLs.clear();
        FetchMovieTask movieTask = new FetchMovieTask("http://api.themoviedb.org/3/discover/movie");
        movieTask.execute();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView =  (GridView)rootView.findViewById(R.id.mainGridview)  ;
        imageAdapter = new ImageAdapter(getActivity(), posterURLs);

        gridView.setAdapter(new ImageAdapter(getActivity(), posterURLs));



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String poster_path , overview, title, backdrop_path, vote_average, release_date, ID;
                overview = movieData.get(position).overview;
                release_date=movieData.get(position).release_date.substring(0, 4);
                title=movieData.get(position).title;
                backdrop_path="http://image.tmdb.org/t/p/w185/"+movieData.get(position).backdrop_path;
                vote_average= movieData.get(position).vote_average+"/10";
                ID=movieData.get(position).id;
                poster_path="http://image.tmdb.org/t/p/w185/"+ movieData.get(position).poster_path;


                MainActivity mainActivity=(MainActivity)getActivity();
                mainActivity.setData (poster_path , overview, title, backdrop_path,
                         vote_average, release_date,  ID);

            }
        });
        return rootView;
    }




    public class FetchMovieTask extends AsyncTask<String, Void, Void>{

        String baseURL;
        public FetchMovieTask(String baseURL)
        {
            this.baseURL = baseURL;
        }
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String movieJsonStr = null;

            try {
                String API_KEY = "?api_key="+BuildConfig.OPEN_MOVIE_API_KEY;
                URL url = new URL(baseURL.concat(API_KEY));

                Log.v(LOG_TAG, url.toString());

                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream=httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(null == inputStream){
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line=bufferedReader.readLine())!=null)
                {
                    buffer.append(line + "\n");
                }

                if(0 == buffer.length()){
                    return  null;
                }


                movieJsonStr = buffer.toString();
                JSONObject jsonObject=new JSONObject(movieJsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject myMovie = jsonArray.getJSONObject(i);

                        String poster_path = myMovie.getString("poster_path");
                        String overview = myMovie.getString("overview");
                        String title = myMovie.getString("title");
                        String backdrop_path = myMovie.getString("backdrop_path");
                        String vote_average = myMovie.getString("vote_average");
                        String release_date = myMovie.getString("release_date");
                        String id = myMovie.getString("id");

                        posterURLs.add("http://image.tmdb.org/t/p/w185/" + poster_path);
                        MovieData d = new MovieData(poster_path, overview, title, backdrop_path, vote_average, release_date, id);
                        movieData.add(d);
                    }
                Log.v(LOG_TAG, " movieJSON "+ movieJsonStr );

            }catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (null != httpURLConnection) {
                    httpURLConnection.disconnect();
                }
                if (null != bufferedReader) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gridView.setAdapter(new ImageAdapter(getActivity(), posterURLs));
        }

    }
}



