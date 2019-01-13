package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.models.movies.MovieList;
import com.courseproject.movienightsapi.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService = new MovieService();

    @GetMapping("/search/{name}")
    public MovieList findMovies(@PathVariable String name){
        return movieService.findMovie(name);
    }
}
