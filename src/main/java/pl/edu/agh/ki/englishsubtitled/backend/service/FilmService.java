package pl.edu.agh.ki.englishsubtitled.backend.service;

import pl.edu.agh.ki.englishsubtitled.backend.model.Film;

public interface FilmService {
    Film getOrCreateFilm(String filmTitle);
}
