package pl.edu.agh.ki.englishsubtitled.backend.dto;

import java.util.Objects;

public class TranslationDto {
    public String engWord;
    public String plWord;

    public TranslationDto(String engWord, String plWord){
        this.engWord = engWord;
        this.plWord = plWord;
    }

    public TranslationDto(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationDto that = (TranslationDto) o;
        return Objects.equals(engWord, that.engWord) &&
                Objects.equals(plWord, that.plWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engWord, plWord);
    }
}
