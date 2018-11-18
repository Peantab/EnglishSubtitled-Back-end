package pl.edu.agh.ki.englishsubtitled.backend.model;

import pl.edu.agh.ki.englishsubtitled.backend.dto.AchievementDto;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

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

    public AchievementEntry() {
    }

    public AchievementEntry(UserStatistics owner, Achievement achievement) {
        this.owner = owner;
        this.achievement = achievement;
        timestamp = Instant.now();
    }

    public AchievementDto getDto() {
        return new AchievementDto(achievement.toString(), timestamp.toEpochMilli());
    }

    public enum Achievement {
        FIRST_BLOOD((UserStatistics stats) -> stats.getFinishedLessonsCount() >= 1), // Finished one lesson
        AROUND_THE_WORLD_IN_80_DAYS((UserStatistics stats) -> stats.getRegistrationTimestamp().isBefore(Instant.now().minus(80, ChronoUnit.DAYS))), // 80 days since account creation
        THE_TEMPLE_OF_DOOM((UserStatistics stats) -> stats.getMistakesCount() >= 84), // Made 84 mistakes
        THE_ORIGINAL_SERIES((UserStatistics stats) -> stats.getCorrectAnswersInRowMax() >= 20), // Answered correctly 20 times in row during one game
        A_BEAUTIFUL_MIND((UserStatistics stats) -> stats.getFullyCorrectLessons() >= 28), // Finished without making mistakes 28 lessons
        AMERICAN_SNIPER((UserStatistics stats) -> stats.getCorrectAnswersAsFirstCount() >= 255), // 255 times answered without making mistakes
        BOOK_OF_SECRETS((UserStatistics stats) -> stats.getDictionaryAdditions() >= 13), // 13 words added to dictionary
        A_GAME_OF_SHADOWS((UserStatistics stats) -> stats.getCrosswordGamesCount() >= 21), // 21 crosswords solved
        SLUMDOG_MILLIONAIRE((UserStatistics stats) -> stats.getAbcdGamesCount() >= 100), // 100 closed questions solved
        THE_ORDER_OF_THE_PHOENIX((UserStatistics stats) -> stats.getWordGamesCount() >= 12); // 12 word games solved

        private Predicate<UserStatistics> condition;

        Achievement(Predicate<UserStatistics> condition) {
            this.condition = condition;
        }

        public boolean checkCondition(UserStatistics userStatistics) {
            return condition.test(userStatistics);
        }
    }
}
