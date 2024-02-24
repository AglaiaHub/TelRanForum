package java51.forum.accounting.dto;


import java51.forum.accounting.model.Role;
import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class UserDto {
    String login;
    String firstName;
    String lastName;
    @Singular
    Set<Role> roles;
}
