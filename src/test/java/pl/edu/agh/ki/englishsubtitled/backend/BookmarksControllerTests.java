package pl.edu.agh.ki.englishsubtitled.backend;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.ki.englishsubtitled.backend.dto.TranslationDto;
import pl.edu.agh.ki.englishsubtitled.backend.exception.TranslationNotFoundException;
import pl.edu.agh.ki.englishsubtitled.backend.model.Translation;
import pl.edu.agh.ki.englishsubtitled.backend.repository.TranslationRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserRepository;
import pl.edu.agh.ki.englishsubtitled.backend.service.TranslationService;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookmarksControllerTests {

    @Autowired
    BookmarksController bookmarksController;

    @Autowired
    TranslationService translationService;

    @Autowired
    TranslationRepository translationRepository;

    @Autowired
    UserRepository userRepository;

    @After
    public void tearDown(){
        translationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void addSingleBookmark(){
        Translation translation = translationService.getOrCreateTranslation("ala", "makota");
        translationRepository.flush();
        bookmarksController.addBookmark("test1", new TranslationDto("ala", "makota"));
        TranslationDto translationDto = bookmarksController.getBookmarks("test1").get(0);
        assertEquals(translation.getDto(), translationDto);
    }

    @Test
    @Transactional
    public void addTwoBookmarks(){
        Translation translation1 = translationService.getOrCreateTranslation("ala", "makota");
        Translation translation2 = translationService.getOrCreateTranslation("ola", "mapsa");
        translationRepository.flush();
        bookmarksController.addBookmark("test1", new TranslationDto("ala", "makota"));
        bookmarksController.addBookmark("test1", new TranslationDto("ola", "mapsa"));
        List<TranslationDto> translationDtos = bookmarksController.getBookmarks("test1");
        assertTrue(translationDtos.contains(translation1.getDto()));
        assertTrue(translationDtos.contains(translation2.getDto()));
    }

    @Test
    @Transactional
    public void removeBookmark(){
        translationService.getOrCreateTranslation("ala", "makota");
        Translation translation = translationService.getOrCreateTranslation("ola", "mapsa");
        translationRepository.flush();
        bookmarksController.addBookmark("test1", new TranslationDto("ala", "makota"));
        bookmarksController.addBookmark("test1", new TranslationDto("ola", "mapsa"));
        bookmarksController.removeBookmark("test1", new TranslationDto("ala", "makota"));

        TranslationDto translationDto = bookmarksController.getBookmarks("test1").get(0);
        assertEquals(translation.getDto(), translationDto);
    }

    @Test(expected = TranslationNotFoundException.class)
    @Transactional
    public void cantBookmarkNonExistingTranslation(){
        bookmarksController.addBookmark("test1", new TranslationDto("ala", "makota"));
    }

    @Test
    @Transactional
    public void endpoints() throws Exception {
        int ok = 200;
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookmarksController).build();
        translationService.getOrCreateTranslation("ala", "makota");

        int status = mockMvc.perform(MockMvcRequestBuilders.put("/bookmarks").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "\t\"engWord\": \"ala\",\n" +
                "\t\"plWord\": \"makota\"\n" +
                "}").header("Authorization", "test1")).andReturn().getResponse().getStatus();
        assertEquals("PUT result", ok, status);

        status = mockMvc.perform(MockMvcRequestBuilders.get("/bookmarks").contentType(MediaType.APPLICATION_JSON).header("Authorization", "test1")).andReturn().getResponse().getStatus();
        assertEquals("GET result", ok, status);

        status = mockMvc.perform(MockMvcRequestBuilders.put("/bookmarks/remove").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "\t\"engWord\": \"ala\",\n" +
                "\t\"plWord\": \"makota\"\n" +
                "}").header("Authorization", "test1")).andReturn().getResponse().getStatus();
        assertEquals("DELETE result", ok, status);
    }
}
