package com.real.interview.external_call.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbMovieResponse {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Director")
    private String director;

    @JsonProperty("imdbRating")
    private String imdbRating;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Response")
    private String response;

    @JsonProperty("Error")
    private String error;

    // Add other fields if needed
}
