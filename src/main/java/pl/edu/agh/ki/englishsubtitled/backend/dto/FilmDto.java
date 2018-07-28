package pl.edu.agh.ki.englishsubtitled.backend.dto;

import java.util.List;
import java.util.Objects;

public class FilmDto {
    public String filmTitle;
    public List<LessonSummaryDto> lessons;

    public FilmDto(String filmTitle, List<LessonSummaryDto> lessons){
        this.filmTitle = filmTitle;
        this.lessons = lessons;
    }

    public FilmDto(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDto filmDto = (FilmDto) o;
        return Objects.equals(filmTitle, filmDto.filmTitle) &&
                Objects.equals(lessons, filmDto.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmTitle);
    }
}
