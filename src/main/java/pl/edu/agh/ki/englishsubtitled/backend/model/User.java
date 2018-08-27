package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer userId;

    @Column(unique = true, nullable = false)
    String facebookUserId;

    boolean admin = false;

    @ManyToMany
    List<Translation> bookmarkedTranslations;

    public User(){}

    public User(String facebookUserId){
        this.facebookUserId = facebookUserId;
        bookmarkedTranslations = new LinkedList<>();
    }

    public boolean isAdmin() {
        return admin;
    }

    public List<TranslationDto> getTranslationDtos(){
        return bookmarkedTranslations.stream().map(Translation::getDto).collect(Collectors.toList());
    }

    public void addBookmark(Translation translation){
        if (!bookmarkedTranslations.contains(translation)){
            bookmarkedTranslations.add(translation);
        }
    }

    public void removeBookmark(Translation translation){
        bookmarkedTranslations.remove(translation);
    }
}
