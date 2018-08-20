package pl.edu.agh.ki.englishsubtitled.backend.service;

import pl.edu.agh.ki.englishsubtitled.backend.model.User;

public interface UserService {
    User authenticate(String token, boolean required);
    User authenticateAdmin(String token);
}
