package com.real.interview.controller;

import com.real.interview.external_call.client.OmdbApiClient;
import com.real.interview.external_call.dto.OmdbMovieResponse;
import com.real.interview.model.Movie;
import com.real.interview.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    OmdbApiClient omdbApiClient;

    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody Movie movie) {
        return new ResponseEntity(movieService.create(movie), HttpStatus.CREATED);
    }


    @GetMapping("{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getById(id));
    }


    // GET /api/v1/movies?page=0&size=5
    @GetMapping
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> page = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(page);
    }

    // GET /api/v1/movies/all
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAll() {
        return ResponseEntity.ok(movieService.getAll());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.update(id, movie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetches movie metadata from OMDb API using title and release year.
     * Example: GET /api/v1/movies/omdb?title=Inception&year=2010
     */
    @GetMapping("/omdb")
    public ResponseEntity<OmdbMovieResponse> getMovieFromOmdb(
            @RequestParam String title,
            @RequestParam int year) {

        OmdbMovieResponse response = omdbApiClient.getMovieDetails(title, year);
        return ResponseEntity.ok(response);
    }

    // Search for movies by title and year
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> findByTitleAndYear(@RequestParam String title, @RequestParam Integer year) {

        List<Movie> movies;

        if (title != null && year != null) {
            movies = movieService.searchByTitleAndReleaseYear(title, year);
        } else if (title != null) {
            movies = movieService.searchByTitle(title);
        } else if (year != null) {
            movies = movieService.searchByReleaseYear(year);
        } else {
            movies = movieService.getAll();
        }

        return ResponseEntity.ok(movies);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Movie>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchByTitle(title));
    }

    @GetMapping("/search/year")
    public ResponseEntity<List<Movie>> searchByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(movieService.searchByReleaseYear(year));
    }



    /*

    @PostMapping
    public Long createMovie(@Valid @RequestBody Movie movie) {
        return movieService.create(movie).getId();
    }

    @GetMapping("{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAll() {
        return ResponseEntity.ok(movieService.getAll());
    }

    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.update(id, movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
    }*/

}
