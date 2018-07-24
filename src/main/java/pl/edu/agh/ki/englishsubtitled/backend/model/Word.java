package pl.edu.agh.ki.englishsubtitled.backend.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer wordId;

    @Column(unique = true, nullable = false)
    String word;

    public Word(){}

    public Word(String word){
        this.word = word;
    }

    public String getWord(){
        return word;
    }
}
