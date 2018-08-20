package pl.edu.agh.ki.englishsubtitled.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.ki.englishsubtitled.backend.dto.FilmDto;
import pl.edu.agh.ki.englishsubtitled.backend.model.Film;
import pl.edu.agh.ki.englishsubtitled.backend.repository.FilmRepository;
import pl.edu.agh.ki.englishsubtitled.backend.repository.LessonRepository;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmsControllerTests {

    private MockMvc mockMvc;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    LessonsController lessonsController;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    FilmsController filmsController;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonsController, filmsController).build();
    }

    @After
    public void tearDown(){
        lessonRepository.deleteAll();
        filmRepository.deleteAll();
    }

    @Test
    public void gettingSingleFilmTest(){
        try{
            int status = mockMvc.perform(MockMvcRequestBuilders.post("/lessons").contentType(MediaType.APPLICATION_JSON).content("[{\n" +
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
                    "}]").header("Authorization", "mock")).andReturn().getResponse().getStatus();
            assertEquals(200, status);
        }catch (Exception e){
            fail();
        }
        lessonRepository.flush();

        Film createdFilm = filmRepository.findFirstByFilmTitle("Ala ma kota The Movie");
        assertNotNull("Failed to create a film!", createdFilm);
        FilmDto dtoOfCreatedFilm = createdFilm.getDto();

        RestTemplate restTemplate = new RestTemplate();

        FilmDto filmDtoFromEndpoint = restTemplate.getForObject(String.format("http://localhost:%d/films/%s", port, dtoOfCreatedFilm.filmTitle), FilmDto.class);

        assertEquals(dtoOfCreatedFilm, filmDtoFromEndpoint);
    }

    @Test
    public void gettingAllFilmsTest(){
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
                    "}," +
                    "{" +
                    "\t\"lessonTitle\": \"Ola ma psa\",\n" +
                    "\t\"filmTitle\": \"Ola ma psa The Sequel to Ala ma kota\",\n" +
                    "\t\"translations\": [{\n" +
                    "\t\t\"engWord\": \"dog\",\n" +
                    "\t\t\"plWord\": \"pies\"\n" +
                    "\t}]\n" +
                    "}]").header("Authorization", "mock"));
        }catch (Exception e){
            fail();
        }
        lessonRepository.flush();

        Film createdFilm1 = filmRepository.findFirstByFilmTitle("Ala ma kota The Movie");
        assertNotNull("Failed to create a film!", createdFilm1);
        FilmDto dtoOfCreatedFilm1 = createdFilm1.getDto();

        Film createdFilm2 = filmRepository.findFirstByFilmTitle("Ola ma psa The Sequel to Ala ma kota");
        assertNotNull("Failed to create a film!", createdFilm2);
        FilmDto dtoOfCreatedFilm2 = createdFilm2.getDto();

        RestTemplate restTemplate = new RestTemplate();

        // Because one cannot simply get for List<>...
        ResponseEntity<List<FilmDto>> responseEntity =
                restTemplate.exchange(String.format("http://localhost:%d/films", port),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<FilmDto>>() {});

        List<FilmDto> filmDtosFromEndpoint = responseEntity.getBody();

        assertEquals(2, filmDtosFromEndpoint.size());
        assertTrue("First film is not present in endpoint's response!", filmDtosFromEndpoint.contains(dtoOfCreatedFilm1));
        assertTrue("Second film is not present in endpoint's response!", filmDtosFromEndpoint.contains(dtoOfCreatedFilm2));
    }

    @Test
    public void twoLessonsForTheSameFilmShouldNotCreateSeparateFilms(){
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
                    "}," +
                    "{" +
                    "\t\"lessonTitle\": \"Ola ma psa\",\n" +
                    "\t\"filmTitle\": \"Ala ma kota The Movie\",\n" +
                    "\t\"translations\": [{\n" +
                    "\t\t\"engWord\": \"dog\",\n" +
                    "\t\t\"plWord\": \"pies\"\n" +
                    "\t}]\n" +
                    "}]").header("Authorization", "mock"));
        }catch (Exception e){
            fail();
        }
        lessonRepository.flush();

        List<Film> allFilms = filmRepository.findAll();

        assertEquals("Exactly one film should be created!",1, allFilms.size());
        assertEquals("Title of the created film should match request's content", "Ala ma kota The Movie", allFilms.get(0).filmTitle);
    }
}
