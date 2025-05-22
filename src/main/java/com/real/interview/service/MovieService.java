package com.real.interview.service;

import com.real.interview.exception.MovieNotFoundException;
import com.real.interview.model.Movie;
import com.real.interview.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Component
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with ID " + id));
    }

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie update(Long id, Movie updatedMovie) {
        Movie movie = getById(id);
        movie.setTitle(updatedMovie.getTitle());
        movie.setReleaseYear(updatedMovie.getReleaseYear());
        return movieRepository.save(movie);
    }

    public void delete(Long id) {
        Movie movie = getById(id);
        movieRepository.delete(movie);
    }

    public List<Movie> searchByTitle(String title) {
        return movieRepository.findByTitleIgnoreCase(title);
    }

    public List<Movie> searchByReleaseYear(int year) {
        return movieRepository.findByReleaseYear(year);
    }


    public List<Movie> searchByTitleAndReleaseYear(String title, int year) {
        return movieRepository.findByTitleAndReleaseYear(title, year);
    }


}
