package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.UserStatistics;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Integer> {
}
