package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.models.movies.Movie;
import com.courseproject.movienightsapi.models.movies.MovieList;
import com.courseproject.movienightsapi.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/search/{name}")
    public MovieList searchMovies(@PathVariable String name){
        return movieService.searchMovies(name);
    }

    @GetMapping("/movie/{imdbId}")
    public Movie findMovie(@PathVariable String imdbId) {
        return movieService.findMovie(imdbId);
    }
}
