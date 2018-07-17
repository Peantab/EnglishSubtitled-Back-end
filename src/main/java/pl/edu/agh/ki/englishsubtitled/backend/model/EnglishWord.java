package pl.edu.agh.ki.englishsubtitled.backend.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WordsEn")
public class EnglishWord extends Word {

    public EnglishWord(){}

    public EnglishWord(String word){
        super(word);
    }
}
