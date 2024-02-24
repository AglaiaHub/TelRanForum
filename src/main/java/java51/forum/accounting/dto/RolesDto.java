package java51.forum.accounting.dto;

import java51.forum.accounting.model.Role;
import lombok.*;

import java.util.Set;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesDto {
    String login;
    @Singular
    Set<Role> roles;

}
