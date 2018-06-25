package pl.edu.agh.ki.englishsubtitled.backend.dto;

public class TranslationDto {
    public String englishWord;
    public String polishWord;

    public TranslationDto(String englishWord, String polishWord){
        this.englishWord = englishWord;
        this.polishWord = polishWord;
    }
}
