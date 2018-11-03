package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.FilmDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonSummaryDto;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "Films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer filmId;

    @Column(unique = true, nullable = false)
    public String filmTitle;

    @OneToMany(mappedBy = "film", fetch = FetchType.EAGER)
    List<Lesson> lessons;

    public Film(){}

    public Film(String filmTitle){
        this.filmTitle = filmTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(filmTitle, film.filmTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmTitle);
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public FilmDto getDto(){
        List<LessonSummaryDto> lessonDtos = lessons.stream().map(Lesson::getSummary).collect(Collectors.toList());
        return new FilmDto(filmTitle, lessonDtos);
    }
}
