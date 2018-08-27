package pl.edu.agh.ki.englishsubtitled.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;
import pl.edu.agh.ki.englishsubtitled.backend.model.Translation;
import pl.edu.agh.ki.englishsubtitled.backend.model.User;
import pl.edu.agh.ki.englishsubtitled.backend.service.TranslationService;
import pl.edu.agh.ki.englishsubtitled.backend.service.UserService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/bookmarks")
public class BookmarksController {

    private final TranslationService translationService;
    private final UserService userService;

    @Autowired
    BookmarksController(TranslationService translationService, UserService userService){
        this.translationService = translationService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TranslationDto> getBookmarks(@RequestHeader("Authorization") String token){
        User user = userService.authenticate(token, true);
        return user.getTranslationDtos();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addBookmark(@RequestHeader("Authorization") String token, @RequestBody TranslationDto translationDto){
        User user = userService.authenticate(token, true);
        Translation translation = translationService.getTranslation(translationDto.engWord, translationDto.plWord);
        user.addBookmark(translation);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void removeBookmark(@RequestHeader("Authorization") String token, @RequestBody TranslationDto translationDto){
        User user = userService.authenticate(token, true);
        Translation translation = translationService.getTranslation(translationDto.engWord, translationDto.plWord);
        user.removeBookmark(translation);
    }
}
