package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.movies.Movie;
import com.courseproject.movienightsapi.models.movies.MovieList;
import com.courseproject.movienightsapi.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieService {

    @Value("${omdb.searchUrl}")
    private String searchUrl;

    @Value("${omdb.apikey}")
    private String apiKey;

    @Value("${omdb.getUrl}")
    private String getUrl;

    private RestTemplate restTemplate = new RestTemplate();
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieList searchMovies(String title) {
        return restTemplate.getForObject(searchUrl + title, MovieList.class);
    }

    public Movie findMovie(String imdbId) {
        if (!movieRepository.existsByImdbId(imdbId)) {
            return movieRepository.save(getFromOmdb(imdbId));
        }
        return getFromDB(imdbId);
    }

    private Movie getFromOmdb(String imdbId) {
        Movie movie = restTemplate.getForObject(getUrl + imdbId, Movie.class);
        return movie;
    }

    private Movie getFromDB(String imdbId) {
        Movie movie = movieRepository.findByImdbId(imdbId);
        return movie;
    }
}
