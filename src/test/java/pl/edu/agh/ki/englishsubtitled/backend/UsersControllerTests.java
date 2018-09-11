package pl.edu.agh.ki.englishsubtitled.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.ki.englishsubtitled.backend.dto.UserDto;
import pl.edu.agh.ki.englishsubtitled.backend.repository.UserRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersControllerTests {

    @Autowired
    UserRepository userRepository;

    @LocalServerPort
    private int port;

    @Before
    public void setUp(){
        userRepository.deleteAll();
    }

    @After
    public void tearDown(){
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    public void endpointTest(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "user1");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(String.format("http://localhost:%d/users", port),
                        HttpMethod.GET, entity, UserDto.class);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().admin);
    }
}
