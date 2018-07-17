package pl.edu.agh.ki.englishsubtitled.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.ki.englishsubtitled.backend.model.EnglishWord;
import pl.edu.agh.ki.englishsubtitled.backend.model.PolishWord;
import pl.edu.agh.ki.englishsubtitled.backend.model.Translation;

public interface TranslationRepository extends JpaRepository<Translation, Integer> {
    Translation findFirstByEnglishWordAndPolishWord(EnglishWord englishWord, PolishWord polishWord);
}
