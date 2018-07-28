package pl.edu.agh.ki.englishsubtitled.backend;

import org.junit.After;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;
import pl.edu.agh.ki.englishsubtitled.backend.model.Lesson;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;

import static org.junit.Assert.*;

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

    @After
    public void tearDown(){
        lessonRepository.deleteAll();
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
    }

    @Test
    @Transactional
    public void updatedLessonShouldHaveTheSameId() throws Exception {
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
        Integer lessonId = lessonRepository.findByLessonTitleEquals("Ala ma kota").getLessonId();

        mockMvc.perform(MockMvcRequestBuilders.put("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
                "\t\"lessonTitle\": \"Ala ma kota\",\n" +
                "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                "\t\"translations\": [{\n" +
                "\t\t\"engWord\": \"have\",\n" +
                "\t\t\"plWord\": \"ma\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"engWord\": \"cat\",\n" +
                "\t\t\"plWord\": \"kot\"\n" +
                "\t}]\n" +
                "}]"));

        lessonRepository.flush();
        Lesson updatedLesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        if (updatedLesson == null){
            fail("Lesson ceased to exist!");
        }
        Integer updatedLessonId = updatedLesson.getLessonId();

        assertEquals(lessonId, updatedLessonId);
        assertEquals("One of translations should no longer exist!", 2L, (long) updatedLesson.translations.size());
    }

    @Test
    public void removalFunctionalityWorks() throws Exception{
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

        Lesson lesson = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        assertNotNull("Failed to create a lesson!", lesson);
        int lessonId = lesson.getLessonId();

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/lessons/%d", lessonId)));
        lessonRepository.flush();

        Lesson lessonAfterRemoval = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        assertNull("Lesson should be removed!", lessonAfterRemoval);
    }

    @Test
    public void removingEndpointRemovesOnlyRequestedLesson() throws Exception{
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
                "\t\"lessonTitle\": \"Ola ma psa\",\n" +
                "\t\"filmTitle\": \"Ola ma psa The Sequel to Ala ma kota\",\n" +
                "\t\"translations\": [{\n" +
                "\t\t\"engWord\": \"dog\",\n" +
                "\t\t\"plWord\": \"pies\"\n" +
                "\t}]\n" +
                "}]"));
        lessonRepository.flush();
        int lessonId = lessonRepository.findByLessonTitleEquals("Ala ma kota").getLessonId();

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/lessons/%d", lessonId)));
        lessonRepository.flush();

        Lesson lessonRemoved = lessonRepository.findByLessonTitleEquals("Ala ma kota");
        Lesson lessonNotRemoved = lessonRepository.findByLessonTitleEquals("Ola ma psa");

        assertNull(lessonRemoved);
        assertNotNull(lessonNotRemoved);
    }
}
