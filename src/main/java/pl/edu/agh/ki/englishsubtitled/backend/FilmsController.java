package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.FilmDto;
import pl.edu.agh.ki.englishsubtitled.backend.exception.FilmTitleNotFoundException;
import pl.edu.agh.ki.englishsubtitled.backend.model.Film;
import pl.edu.agh.ki.englishsubtitled.backend.repository.FilmRepository;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/films")
public class FilmsController {

    private final FilmRepository filmRepository;

    @Autowired
    FilmsController(FilmRepository filmRepository){
        this.filmRepository = filmRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<FilmDto> getFilms(){
        return filmRepository.findAll().stream().map(Film::getDto).collect(Collectors.toList());
    }

    @RequestMapping(path = "/{filmTitle}", method = RequestMethod.GET)
    public FilmDto getFilm(@PathVariable String filmTitle){

        Film film = filmRepository.findFirstByFilmTitle(filmTitle);

        if(film == null) {
            throw new FilmTitleNotFoundException(filmTitle);
        }
        return film.getDto();
    }
}
