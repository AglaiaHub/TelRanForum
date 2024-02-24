package java51.forum.accounting.service;

import java51.forum.accounting.dto.UserRegisterDto;
import java51.forum.accounting.dto.RolesDto;
import java51.forum.accounting.dto.UserEditDto;
import java51.forum.accounting.dto.UserDto;

public interface UserService {

    UserDto registerUser (UserRegisterDto userRegisterDto);

    UserDto deleteUser (String login);

    UserDto updateUser (String login, UserEditDto userEditDto);

    UserDto getUser (String login);

    RolesDto changeRolesList(String login, String role, boolean isAddRole);

    void changePassword (String login, String newPassword);

}
