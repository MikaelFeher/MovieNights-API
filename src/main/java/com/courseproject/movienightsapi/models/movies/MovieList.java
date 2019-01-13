package com.courseproject.movienightsapi.models.movies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MovieList {
    @JsonProperty("Search")
    private List<Movie> movieList;

    public MovieList() {
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
