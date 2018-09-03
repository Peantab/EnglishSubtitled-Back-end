package pl.edu.agh.ki.englishsubtitled.backend.service;

import pl.edu.agh.ki.englishsubtitled.backend.dto.LessonResultsDto;
import pl.edu.agh.ki.englishsubtitled.backend.model.AchievementEntry;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;

import java.util.List;

public interface AchievementService {
    void acceptLessonResults(User user, int lessonId, LessonResultsDto lessonResults);
    List<AchievementEntry> recognizeNewAchievements(User user);
}
