package com.real.interview.repository;

import com.real.interview.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleAndReleaseYear(String title, int releaseYear);

    List<Movie> findByTitle(String title);

    List<Movie> findByTitleIgnoreCase(String title);

    List<Movie> findByReleaseYear(int releaseYear);

}
