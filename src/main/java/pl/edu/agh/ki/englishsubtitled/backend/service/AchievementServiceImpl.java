package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonResultsDto;
import pl.edu.agh.ki.englishsubtitled.backend.model.AchievementEntry;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.model.UserStatistics;
import pl.edu.agh.ki.englishsubtitled.backend.repository.AchievementEntryRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.edu.agh.ki.englishsubtitled.backend.model.AchievementEntry.Achievement;

@Service
public class AchievementServiceImpl implements AchievementService {

    private final AchievementEntryRepository achievementEntryRepository;

    @Autowired
    public AchievementServiceImpl(AchievementEntryRepository achievementEntryRepository) {
        this.achievementEntryRepository = achievementEntryRepository;
    }

    @Override
    public void acceptLessonResults(User user, int lessonId, LessonResultsDto lessonResults) {
        UserStatistics userStatistics = user.getUserStatistics();

        List<Lesson> finishedLessons = user.getFinishedLessons();
        Set<Integer> finishedLessonsIds = finishedLessons.stream().map(Lesson::getLessonId).collect(Collectors.toSet());
        Integer cachedLessonId = lessonId;
        if (!finishedLessonsIds.contains(cachedLessonId)) userStatistics.incrementFinishedLessonsCount();

        userStatistics.increaseMistakesCountBy(lessonResults.mistakes);

        userStatistics.updateCorrectAnswersInRowMaxIfHigher(lessonResults.correctAnswersInRow);

        if (lessonResults.crosswordGames + lessonResults.abcdGames + lessonResults.wordGames == lessonResults.correctAnswersAsFirst) {
            userStatistics.incrementFullyCorrectLessons();
        }

        userStatistics.increaseCorrectAnswersAsFirstCountBy(lessonResults.correctAnswersAsFirst);

        userStatistics.increaseDictionaryAdditionsBy(lessonResults.dictionaryAdditions);

        userStatistics.increaseCrosswordGamesCountBy(lessonResults.crosswordGames);

        userStatistics.increaseAbcdGamesCountBy(lessonResults.abcdGames);

        userStatistics.increaseWordGamesCountBy(lessonResults.wordGames);
    }

    @Override
    public List<AchievementEntry> recognizeNewAchievements(User user) {
        UserStatistics userStatistics = user.getUserStatistics();
        List<AchievementEntry> newAchievements = new LinkedList<>();

        Set<Achievement> ownedAchievements = userStatistics.getRecognizedAchievementsSet();

        for (Achievement achievement : Achievement.values()) {
            if (!ownedAchievements.contains(achievement)) {
                if (achievement.checkCondition(userStatistics)) {
                    AchievementEntry achievementEntry = new AchievementEntry(userStatistics, achievement);
                    userStatistics.addAchievementEntry(achievementEntry);
                    newAchievements.add(achievementEntry);
                }
            }
        }

        achievementEntryRepository.saveAll(newAchievements);
        achievementEntryRepository.flush();

        return newAchievements;
    }
}
