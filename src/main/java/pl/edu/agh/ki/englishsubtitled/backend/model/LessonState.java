package pl.edu.agh.ki.englishsubtitled.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "LessonStates")
public class LessonState {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer rentalId;

    State state;

    @ManyToOne
    Lesson lesson;

    @ManyToOne
    User user;

    public LessonState(){}

    public LessonState(User user, Lesson lesson){
        this.user = user;
        this.lesson = lesson;
        this.state = State.RENTED;
    }

    public Lesson getLesson(){
        return lesson;
    }

    public State getState(){
        return state;
    }

    public void setFinished(){
        state = State.FINISHED;
    }

    public enum State{
        RENTED, FINISHED
    }
}
