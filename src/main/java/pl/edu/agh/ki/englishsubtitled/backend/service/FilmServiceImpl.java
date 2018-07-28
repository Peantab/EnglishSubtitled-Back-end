package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.englishsubtitled.backend.model.Film;
import pl.edu.agh.ki.englishsubtitled.backend.repository.FilmRepository;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository){
        this.filmRepository = filmRepository;
    }

    @Override
    public Film getOrCreateFilm(String filmTitle) {
        Film film = filmRepository.findFirstByFilmTitle(filmTitle);

        if (film == null){
            film = new Film(filmTitle);
            filmRepository.save(film);
        }

        return film;
    }
}
