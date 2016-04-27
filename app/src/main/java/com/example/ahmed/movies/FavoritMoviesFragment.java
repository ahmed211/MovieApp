package com.example.ahmed.movies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class FavoritMoviesFragment extends Fragment {

    private ArrayList<String> posterURLs = new ArrayList<>();
    Database data;
    ArrayList <MovieData> movieData=new ArrayList<MovieData>();
    GridView gridView;
    private ImageAdapter imageAdapter ;



    public FavoritMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_favorit_movies, container, false);

        data=new Database(getActivity());

        viewAll();
        gridView =  (GridView)view.findViewById(R.id.FavoritGridview)  ;
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


                FavoritMovies favoritMovies=(FavoritMovies)getActivity();
                boolean tablet= favoritMovies.setData (poster_path , overview, title, backdrop_path,
                        vote_average, release_date,  ID);

                if(false==tablet)
                {
                    posterURLs.clear();
                    movieData.clear();
                }


            }
        });


        return view;
    }

    public void viewAll()
    {
        Cursor res = data.getAllData();
        if (0 ==res.getCount()) {
            return;
        }
        while (res.moveToNext()) {
            MovieData d = new MovieData(res.getString(0), res.getString(2), res.getString(4), res.getString(5), res.getString(6), res.getString(3), res.getString(0));
            movieData.add(d);
            posterURLs.add(res.getString(1));
        }
    }
}


