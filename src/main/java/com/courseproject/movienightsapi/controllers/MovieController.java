package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.models.movies.Movie;
import com.courseproject.movienightsapi.models.movies.MovieList;
import com.courseproject.movienightsapi.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchMovies(@PathVariable String name){
        MovieList result = movieService.searchMovies(name);

        if (result.getMovieList() != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/movie/{imdbId}")
    public ResponseEntity<?> findMovie(@PathVariable String imdbId) {
        Movie result = movieService.findMovie(imdbId);
        if (result.getTitle() != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
