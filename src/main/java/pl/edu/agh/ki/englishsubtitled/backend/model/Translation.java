package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;

import javax.persistence.*;

@Entity
@Table(name = "Translations")
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer translationId;

    @ManyToOne
    EnglishWord englishWord;

    @ManyToOne
    PolishWord polishWord;

    public Translation(){}

    public Translation(EnglishWord englishWord, PolishWord polishWord){
        this.englishWord = englishWord;
        this.polishWord = polishWord;
    }

    public TranslationDto getDto(){
        return new TranslationDto(englishWord.getWord(), polishWord.getWord());
    }
}
