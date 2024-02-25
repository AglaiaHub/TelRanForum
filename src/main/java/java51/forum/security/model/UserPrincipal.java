package java51.forum.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;
import java.util.Set;

@AllArgsConstructor
@Getter
public class UserPrincipal implements Principal {
    private String name;
    Set<String> roles;
}
