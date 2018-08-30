package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.LessonState;

public interface LessonStateRepository extends JpaRepository<LessonState, Integer> {
}
