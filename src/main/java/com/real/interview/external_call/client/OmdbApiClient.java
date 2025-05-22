package com.real.interview.external_call.client;

import com.real.interview.exception.MovieNotFoundException;
import com.real.interview.external_call.dto.OmdbMovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class OmdbApiClient {

    @Value("${omdb.api.url}")
    private String omdbUrl;

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    @Autowired
    RestTemplate restTemplate;

//    private final RestTemplate restTemplate;

//    public OmdbApiClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    @Retryable(
            value = { RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)  // exponential backoff
    )
    public OmdbMovieResponse getMovieDetails(String title, int year) {
        String url = UriComponentsBuilder
                .fromHttpUrl(omdbUrl)
                .queryParam("t", title)
                .queryParam("y", year)
                .queryParam("apikey", omdbApiKey)
                .toUriString();


        // Just body
//        OmdbMovieResponse bodyOnly = restTemplate.getForObject(url, OmdbMovieResponse.class);

        // Full response
        try {
            ResponseEntity<OmdbMovieResponse> responseEntity = restTemplate.getForEntity(url, OmdbMovieResponse.class);
            HttpStatus status = (HttpStatus) responseEntity.getStatusCode();

            if (status.is2xxSuccessful()) {
                OmdbMovieResponse body = responseEntity.getBody();
//                HttpHeaders headers = responseEntity.getHeaders();
                if (body == null || "False".equalsIgnoreCase(body.getResponse())) {
                    throw new MovieNotFoundException("Movie not found in OMDb for title: " + title);
                }
                return body;
            } else if (status == HttpStatus.UNAUTHORIZED) {
                throw new RuntimeException("Invalid OMDb API key");
            } else {
                throw new RuntimeException("Unexpected response from OMDb: " + status);
            }

        } catch (HttpClientErrorException.NotFound ex) {
            throw new MovieNotFoundException("Movie not found (404): " + ex.getMessage());
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new RuntimeException("Unauthorized (401): Invalid OMDb API key");
        } catch (HttpServerErrorException ex) {
            throw new RuntimeException("OMDb server error: " + ex.getMessage());
        } catch (RestClientException ex) {
            throw new RuntimeException("OMDb call failed: " + ex.getMessage());
        }
    }


//        String url = "https://www.omdbapi.com/?t=Titanic&y=1997&apikey=431d9726";

        /*
        OmdbMovieResponse response = restTemplate.getForObject(url, OmdbMovieResponse.class);
        if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
            throw new RuntimeException("Movie not found in OMDb: " + (response != null ? response.getError() : "Unknown error"));

        return response;
        }*/

}