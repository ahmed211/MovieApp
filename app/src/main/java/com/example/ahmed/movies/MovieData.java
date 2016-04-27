package com.example.ahmed.movies;

public class MovieData {
    String poster_path , overview, title, backdrop_path, vote_average, release_date, id;
    public MovieData (String poster_path , String overview,String title,String backdrop_path,
                      String vote_average,String release_date, String id)
    {
        this.poster_path=poster_path;
        this.overview=overview;
        this.title=title;
        this.backdrop_path=backdrop_path;
        this.vote_average=vote_average;
        this.release_date=release_date;
        this.id=id;
    }
}
