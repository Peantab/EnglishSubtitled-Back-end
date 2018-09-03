package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.AchievementDto;

import javax.persistence.*;
import java.time.Instant;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.edu.agh.ki.englishsubtitled.backend.model.AchievementEntry.Achievement;

@Entity
@Table(name = "UserStatistics")
public class UserStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer statisticsId;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    List<AchievementEntry> achievements;

    Instant registrationTimestamp;

    int finishedLessonsCount = 0;

    public UserStatistics(){}

    public static UserStatistics create(){
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.registrationTimestamp = Instant.now();
        userStatistics.achievements = new LinkedList<>();
        return userStatistics;
    }

    public void addAchievementEntry(AchievementEntry achievementEntry){
        achievements.add(achievementEntry);
    }

    public Instant getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public Set<Achievement> getRecognizedAchievementsSet(){
        EnumSet<Achievement> recognizedAchievements = EnumSet.noneOf(Achievement.class);

        for (AchievementEntry achievementEntry: achievements){
            recognizedAchievements.add(achievementEntry.achievement);
        }

        return recognizedAchievements;
    }

    public List<AchievementDto> getRecognizedAchievementsDtos(){
        return achievements.stream().map(AchievementEntry::getDto).collect(Collectors.toList());
    }

    public int getFinishedLessonsCount() {
        return finishedLessonsCount;
    }

    public void increaseFinishedLessonsCountBy(int value){
        if (value < 0) throw new AssertionError("Increment must be positive.");
        finishedLessonsCount += value;
    }
}
