package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.AchievementDto;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Achievements")
public class AchievementEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer achievementEntryId;

    @ManyToOne
    UserStatistics owner;

    Achievement achievement;

    Instant timestamp;

    public AchievementEntry(){}

    public AchievementEntry(UserStatistics owner, Achievement achievement){
        this.owner = owner;
        this.achievement = achievement;
        timestamp = Instant.now();
    }

    public AchievementDto getDto(){
        return new AchievementDto(achievement.toString(), timestamp.toEpochMilli());
    }

    public enum Achievement{
        FIRST_STEPS
    }
}
