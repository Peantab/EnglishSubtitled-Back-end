package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.Configuration;
import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;
import pl.edu.agh.ki.englishsubtitled.backend.dto.UserDto;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer userId;

    @Column(unique = true, nullable = false)
    String facebookUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @OrderColumn
    List<LessonState> lessonStates;

    @OneToOne(optional = false, cascade = CascadeType.REMOVE)
    UserStatistics userStatistics;

    boolean admin = false;

    @ManyToMany
    List<Translation> bookmarkedTranslations;

    public User(){}

    public User(String facebookUserId){
        this.facebookUserId = facebookUserId;
        bookmarkedTranslations = new LinkedList<>();
        lessonStates = new LinkedList<>();
        userStatistics = UserStatistics.create();
    }

    public boolean isAdmin() {
        return admin;
    }

    public UserDto getDto(){
        return new UserDto(admin);
    }

    public UserStatistics getUserStatistics(){
        return userStatistics;
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

    public void addRental(LessonState lessonState){
        lessonStates.add(lessonState);
    }

    public void removeRental(LessonState lessonState){
        lessonStates.remove(lessonState);
    }

    public Optional<LessonState> getLessonState(Integer lessonId){
        for (LessonState lessonState: lessonStates){
            if (lessonState.getLesson().getLessonId().equals(lessonId)){
                return Optional.of(lessonState);
            }
        }
        return Optional.empty();
    }

    public List<Lesson> getRelatedLessons(){
        return lessonStates.stream().map(LessonState::getLesson).collect(Collectors.toList());
    }

    public List<Lesson> getRentedLessons(){
        return lessonStates.stream().filter(l->l.getState() == LessonState.State.RENTED).map(LessonState::getLesson).collect(Collectors.toList());
    }

    public List<LessonState> makePlaceForRentedLesson(){
        int limit = Configuration.getInstance().getRentedLessonsLimit();
        List<LessonState> toBeRemoved = new LinkedList<>();
        for(Iterator<LessonState> i = lessonStates.iterator(); getRentedLessons().size() >  limit - 1; ){
            LessonState lessonState = i.next();
            if (lessonState.getState() == LessonState.State.RENTED){
                toBeRemoved.add(lessonState);
                i.remove();
            }
        }
        return toBeRemoved;
    }

    public List<Lesson> getFinishedLessons(){
        return lessonStates.stream().filter(l->l.getState() == LessonState.State.FINISHED).map(LessonState::getLesson).collect(Collectors.toList());
    }
}
