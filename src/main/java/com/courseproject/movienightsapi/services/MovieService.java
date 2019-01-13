package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.movies.Movie;
import com.courseproject.movienightsapi.models.movies.MovieList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MovieService {

    @Value("${omdb.searchUrl}")
    private String searchUrl;

    @Value("${omdb.apikey}")
    private String apiKey;

    @Value("${omdb.getUrl}")
    private String getUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public MovieService() {
    }

    public MovieList searchMovies(String title) {
        return restTemplate.getForObject(searchUrl + title, MovieList.class);
    }

    public Movie findMovie(String imdbId) {
        return restTemplate.getForObject(getUrl + imdbId, Movie.class);
    }
}
