package pl.edu.agh.ki.englishsubtitled.backend.service;

import pl.edu.agh.ki.englishsubtitled.backend.model.Translation;

public interface TranslationService {
    Translation getOrCreateTranslation(String englishWord, String polishWord);
    Translation getTranslation(String englishWord, String polishWord);
}
