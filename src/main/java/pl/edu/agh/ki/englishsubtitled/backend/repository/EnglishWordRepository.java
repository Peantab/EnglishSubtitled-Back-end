package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.EnglishWord;

public interface EnglishWordRepository extends JpaRepository<EnglishWord, Integer> {
    EnglishWord findFirstByWord(String word);
}
