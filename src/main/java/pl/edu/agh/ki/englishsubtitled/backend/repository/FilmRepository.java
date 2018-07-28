package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.Film;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    Film findFirstByFilmTitle(String title);
}
