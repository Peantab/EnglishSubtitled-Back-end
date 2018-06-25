package pl.edu.agh.ki.englishsubtitled.backend.dto;

public class TranslationDto {
    public String engWord;
    public String plWord;

    public TranslationDto(String engWord, String plWord){
        this.engWord = engWord;
        this.plWord = plWord;
    }
}
