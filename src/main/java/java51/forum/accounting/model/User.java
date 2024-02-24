package java51.forum.accounting.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
@Getter
@EqualsAndHashCode(of = "login")
@Document (collection = "users")
@Setter

public class User {
    @Id
    String login;
    String password;
    String firstName;
    String lastName;
    Set<Role> roles;

    public User(){
        roles = new HashSet<>();
        roles.add(Role.USER);
    }

    public User(String login, String password, String firstName, String lastName){
        this();
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Boolean addRole(String role){
        try {
            Role parsedRole = Role.valueOf(role);
            roles.add(parsedRole);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Boolean removeRole (String role) {
        try {
            Role parsedRole = Role.valueOf(role);
            roles.remove(parsedRole);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
