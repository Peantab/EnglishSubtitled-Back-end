package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.AchievementEntry;

public interface AchievementEntryRepository extends JpaRepository<AchievementEntry, Integer> {
}
