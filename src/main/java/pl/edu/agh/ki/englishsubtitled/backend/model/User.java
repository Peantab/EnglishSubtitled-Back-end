package pl.edu.agh.ki.englishsubtitled.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer userId;

    @Column(unique = true, nullable = false)
    String facebookUserId;

    boolean admin = false;

    public User(){}

    public User(String facebookUserId){
        this.facebookUserId = facebookUserId;
    }

    public boolean isAdmin() {
        return admin;
    }
}
