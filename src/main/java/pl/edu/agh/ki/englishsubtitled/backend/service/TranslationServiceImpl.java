package pl.edu.agh.ki.englishsubtitled.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.englishsubtitled.backend.exception.TranslationNotFoundException;
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
        EnglishWord englishWord = getOrCreateEnglishWord(englishWordStr);
        PolishWord polishWord = getOrCreatePolishWord(polishWordStr);

        Translation translation = translationRepository.findFirstByEnglishWordAndPolishWord(englishWord, polishWord);
        if (translation == null){
            translation = new Translation(englishWord, polishWord);
            translationRepository.save(translation);
        }
        return translation;
    }

    @Override
    public Translation getTranslation(String englishWordStr, String polishWordStr) {
        EnglishWord englishWord = englishWordRepository.findFirstByWord(englishWordStr);
        PolishWord polishWord = polishWordRepository.findFirstByWord(polishWordStr);

        if (polishWord == null || englishWord == null) throw new TranslationNotFoundException(englishWordStr, polishWordStr);

        Translation translation = translationRepository.findFirstByEnglishWordAndPolishWord(englishWord, polishWord);
        if (translation == null){
            throw new TranslationNotFoundException(englishWordStr, polishWordStr);
        }
        return translation;
    }

    private EnglishWord getOrCreateEnglishWord(String englishWordStr){
        EnglishWord englishWord = englishWordRepository.findFirstByWord(englishWordStr);
        if (englishWord == null){
            englishWord = new EnglishWord(englishWordStr);
            englishWordRepository.save(englishWord);
        }
        return englishWord;
    }

    private PolishWord getOrCreatePolishWord(String polishWordStr){
        PolishWord polishWord = polishWordRepository.findFirstByWord(polishWordStr);
        if (polishWord == null){
            polishWord = new PolishWord(polishWordStr);
            polishWordRepository.save(polishWord);
        }
        return polishWord;
    }
}
