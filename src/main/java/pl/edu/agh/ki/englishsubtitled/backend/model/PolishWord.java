package pl.edu.agh.ki.englishsubtitled.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "WordsPl")
public class PolishWord extends Word {

    public PolishWord(){}

    public PolishWord(String word){
        super(word);
    }
}
