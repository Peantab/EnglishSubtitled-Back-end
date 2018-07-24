package pl.edu.agh.ki.englishsubtitled.backend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LessonsControllerTests {

    private MockMvc mockMvc;

    @Autowired
    LessonsController lessonsController;

    @Autowired
    LessonRepository lessonRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonsController).build();
    }

    @Test
    public void addSingleLessonFullDto(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                    "\t\"lessonId\": 15,\n" +
                    "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                    "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                    "\t\"translations\": [{\n" +
                    "\t\t\"engWord\": \"Alice\",\n" +
                    "\t\t\"plWord\": \"Ala\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"have\",\n" +
                    "\t\t\"plWord\": \"ma\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"cat\",\n" +
                    "\t\t\"plWord\": \"kot\"\n" +
                    "\t}]\n" +
                    "}]"));
        }catch (Exception e){
            fail();
        }
        lessonRepository.flush();
        Lesson lesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        assertNotNull(lesson);
        lessonRepository.delete(lesson);
    }

    @Test
    public void addSingleLessonNoId(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                    "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                    "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                    "\t\"translations\": [{\n" +
                    "\t\t\"engWord\": \"Alice\",\n" +
                    "\t\t\"plWord\": \"Ala\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"have\",\n" +
                    "\t\t\"plWord\": \"ma\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"cat\",\n" +
                    "\t\t\"plWord\": \"kot\"\n" +
                    "\t}]\n" +
                    "}]"));
        }catch (Exception e){
            fail();
        }
        lessonRepository.flush();
        Lesson lesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        assertNotNull(lesson);
        lessonRepository.delete(lesson);
    }

    @Test(expected = NestedServletException.class)
    public void LessonWithNoTitleCannotBeAdded() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                "\t\"translations\": [{\n" +
                "\t\t\"engWord\": \"Alice\",\n" +
                "\t\t\"plWord\": \"Ala\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"engWord\": \"have\",\n" +
                "\t\t\"plWord\": \"ma\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"engWord\": \"cat\",\n" +
                "\t\t\"plWord\": \"kot\"\n" +
                "\t}]\n" +
                "}]"));
        Lesson lesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        lessonRepository.delete(lesson);
    }

    @Test(expected = NestedServletException.class)
    public void oneLessonCannotBeAddedTwiceInOneRequest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                "\t\"translations\": [{\n" +
                "\t\t\"engWord\": \"Alice\",\n" +
                "\t\t\"plWord\": \"Ala\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"engWord\": \"have\",\n" +
                "\t\t\"plWord\": \"ma\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"engWord\": \"cat\",\n" +
                "\t\t\"plWord\": \"kot\"\n" +
                "\t}]\n" +
                "}," +
                "{" +
                "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                "\t\"translations\": [{\n" +
                "\t\t\"engWord\": \"Alice\",\n" +
                "\t\t\"plWord\": \"Ala\"\n" +
                "\t}]\n" +
                "}]"));
        lessonRepository.flush();
        Lesson lesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        lessonRepository.delete(lesson);
    }

    @Test
    public void oneLessonCannotBeAddedTwiceInDifferentRequests() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                    "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                    "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                    "\t\"translations\": [{\n" +
                    "\t\t\"engWord\": \"Alice\",\n" +
                    "\t\t\"plWord\": \"Ala\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"have\",\n" +
                    "\t\t\"plWord\": \"ma\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"cat\",\n" +
                    "\t\t\"plWord\": \"kot\"\n" +
                    "\t}]\n" +
                    "}]"));
            lessonRepository.flush();
            mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                    "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                    "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                    "\t\"translations\": [{\n" +
                    "\t\t\"engWord\": \"cat\",\n" +
                    "\t\t\"plWord\": \"kot\"\n" +
                    "\t}]\n" +
                    "}]"));
            lessonRepository.flush();
            fail();
        }catch (NestedServletException e){
            // It should be thrown
        }finally{
            Lesson lesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
            lessonRepository.delete(lesson);
        }
    }

    @Test
    public void oneLessonCanHaveMultipleTranslationsOfOneWord(){
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                    "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                    "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                    "\t\"translations\": [{\n" +
                    "\t\t\"engWord\": \"Alice\",\n" +
                    "\t\t\"plWord\": \"Ala\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"have\",\n" +
                    "\t\t\"plWord\": \"ma\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"engWord\": \"Alice\",\n" +
                    "\t\t\"plWord\": \"Alicja\"\n" +
                    "\t}]\n" +
                    "}]"));
        }catch (Exception e){
            fail();
        }
        lessonRepository.flush();
        Lesson lesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        assertNotNull(lesson);
        lessonRepository.delete(lesson);
    }
}
