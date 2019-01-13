package com.courseproject.movienightsapi.models.movies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = false)
public class Movie {
    @JsonProperty("Title")
    private String title;
    @JsonProperty("imdbID")
    private String imdbId;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Runtime")
    private String runtime;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Actors")
    private String actors;
    @JsonProperty("Plot")
    private String plot;
    @JsonProperty("Language")
    private String language;
    @JsonProperty("Poster")
    private String poster;
    @JsonProperty("Website")
    private String website;
    @JsonProperty("Type")
    private String type;

    public Movie(String title, String imdbId, String year, String runtime, String genre, String director, String actors, String plot, String language, String poster, String website, String type) {
        this.title = title;
        this.imdbId = imdbId;
        this.year = year;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.poster = poster;
        this.website = website;
    }

    public String getTitle() {
        return title;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getYear() {
        return year;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getLanguage() {
        return language;
    }

    public String getPoster() {
        return poster;
    }

    public String getWebsite() {
        return website;
    }

    public String getType() {
        return type;
    }
}
