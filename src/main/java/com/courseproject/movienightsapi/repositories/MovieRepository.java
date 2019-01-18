package com.courseproject.movienightsapi.repositories;

import com.courseproject.movienightsapi.models.movies.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    Movie findByImdbId(String imdbId);
    Boolean existsByImdbId(String imdbId);
}
