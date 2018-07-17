package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.englishsubtitled.backend.model.EnglishWord;
import pl.edu.agh.ki.englishsubtitled.backend.model.PolishWord;
import pl.edu.agh.ki.englishsubtitled.backend.model.Translation;
import pl.edu.agh.ki.englishsubtitled.backend.repository.EnglishWordRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.PolishWordRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.TranslationRepository;

@Service
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;
    private final EnglishWordRepository englishWordRepository;
    private final PolishWordRepository polishWordRepository;

    @Autowired
    public TranslationServiceImpl(TranslationRepository translationRepository, EnglishWordRepository englishWordRepository, PolishWordRepository polishWordRepository){
        this.translationRepository = translationRepository;
        this.englishWordRepository = englishWordRepository;
        this.polishWordRepository = polishWordRepository;
    }

    @Override
    public Translation getOrCreateTranslation(String englishWordStr, String polishWordStr) {
        EnglishWord englishWord = englishWordRepository.findFirstByWord(englishWordStr);
        if (englishWord == null){
            englishWord = new EnglishWord(englishWordStr);
            englishWordRepository.save(englishWord);
        }
        PolishWord polishWord = polishWordRepository.findFirstByWord(polishWordStr);
        if (polishWord == null){
            polishWord = new PolishWord(polishWordStr);
            polishWordRepository.save(polishWord);
        }

        Translation translation = translationRepository.findFirstByEnglishWordAndPolishWord(englishWord, polishWord);
        if (translation == null){
            translation = new Translation(englishWord, polishWord);
            translationRepository.save(translation);
        }
        return translation;
    }
}
