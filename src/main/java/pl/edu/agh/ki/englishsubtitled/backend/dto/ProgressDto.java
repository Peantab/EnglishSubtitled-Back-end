package pl.edu.agh.ki.englishsubtitled.backend.dto;

import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ProgressDto {
    public List<LessonSummaryDto> rented;
    public List<LessonSummaryDto> finished;

    public ProgressDto(User user){
        rented = user.getRentedLessons().stream().map(Lesson::getSummary).collect(Collectors.toList());
        finished = user.getFinishedLessons().stream().map(Lesson::getSummary).collect(Collectors.toList());
    }
}
