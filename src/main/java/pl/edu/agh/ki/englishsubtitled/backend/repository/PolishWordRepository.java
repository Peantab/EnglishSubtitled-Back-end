package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.PolishWord;

public interface PolishWordRepository extends JpaRepository<PolishWord, Integer> {
    PolishWord findFirstByWord(String word);
}
