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

    private Instant registrationTimestamp;

    private int finishedLessonsCount = 0;

    private int mistakesCount = 0;

    // the biggest of recorded values
    private int correctAnswersInRowMax = 0;

    private int fullyCorrectLessons = 0;

    private int correctAnswersAsFirstCount = 0;

    // it's a total number of additions to dictionary and not current dictionary size (user could also delete words from a dictionary)
    private int dictionaryAdditions = 0;


    private int crosswordGamesCount = 0;

    private int abcdGamesCount = 0;

    private int wordGamesCount = 0;

    public UserStatistics() {
    }

    public static UserStatistics create() {
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.registrationTimestamp = Instant.now();
        userStatistics.achievements = new LinkedList<>();
        return userStatistics;
    }

    public void addAchievementEntry(AchievementEntry achievementEntry) {
        achievements.add(achievementEntry);
    }

    public Instant getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public Set<Achievement> getRecognizedAchievementsSet() {
        EnumSet<Achievement> recognizedAchievements = EnumSet.noneOf(Achievement.class);

        for (AchievementEntry achievementEntry : achievements) {
            recognizedAchievements.add(achievementEntry.achievement);
        }

        return recognizedAchievements;
    }

    public List<AchievementDto> getRecognizedAchievementsDtos() {
        return achievements.stream().map(AchievementEntry::getDto).collect(Collectors.toList());
    }

    public int getFinishedLessonsCount() {
        return finishedLessonsCount;
    }

    public void incrementFinishedLessonsCount() {
        finishedLessonsCount++;
    }

    public int getMistakesCount() {
        return mistakesCount;
    }

    public void increaseMistakesCountBy(int value) {
        if (value < 0) throw new AssertionError("Value must be positive.");
        mistakesCount += value;
    }

    public int getCorrectAnswersInRowMax() {
        return correctAnswersInRowMax;
    }

    public void updateCorrectAnswersInRowMaxIfHigher(int correctAnswersInRow) {
        if (correctAnswersInRow > correctAnswersInRowMax) correctAnswersInRowMax = correctAnswersInRow;
    }

    public int getFullyCorrectLessons() {
        return fullyCorrectLessons;
    }

    public void incrementFullyCorrectLessons() {
        fullyCorrectLessons++;
    }

    public int getCorrectAnswersAsFirstCount() {
        return correctAnswersAsFirstCount;
    }

    public void increaseCorrectAnswersAsFirstCountBy(int value) {
        if (value < 0) throw new AssertionError("Value must be positive.");
        correctAnswersAsFirstCount += value;
    }

    public int getDictionaryAdditions() {
        return dictionaryAdditions;
    }

    public void increaseDictionaryAdditionsBy(int value) {
        if (value < 0) throw new AssertionError("Value must be positive.");
        dictionaryAdditions += value;
    }

    public int getCrosswordGamesCount() {
        return crosswordGamesCount;
    }

    public void increaseCrosswordGamesCountBy(int value) {
        if (value < 0) throw new AssertionError("Value must be positive.");
        crosswordGamesCount += value;
    }

    public int getAbcdGamesCount() {
        return abcdGamesCount;
    }

    public void increaseAbcdGamesCountBy(int value) {
        if (value < 0) throw new AssertionError("Value must be positive.");
        abcdGamesCount += value;
    }

    public int getWordGamesCount() {
        return wordGamesCount;
    }

    public void increaseWordGamesCountBy(int value) {
        if (value < 0) throw new AssertionError("Value must be positive.");
        wordGamesCount += value;
    }
}
