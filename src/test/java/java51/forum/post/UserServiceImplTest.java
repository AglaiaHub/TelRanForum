package java51.forum.post;

import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.dto.RolesDto;
import java51.forum.accounting.dto.UserDto;
import java51.forum.accounting.dto.UserEditDto;
import java51.forum.accounting.dto.UserRegisterDto;
import java51.forum.accounting.dto.exception.UserExistException;
import java51.forum.accounting.dto.exception.UserNotFoundException;
import java51.forum.accounting.model.Role;
import java51.forum.accounting.model.User;
import java51.forum.accounting.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ModelMapper.class, UserServiceImpl.class})
public class UserServiceImplTest {

    private ModelMapper modelMapper;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_UserNotExist() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setLogin("testuser");
        userRegisterDto.setPassword("password");
        userRegisterDto.setFirstName("John");
        userRegisterDto.setLastName("Doe");

        when(userAccountRepository.existsById("testuser")).thenReturn(false);
//        when(modelMapper.map(userRegisterDto, User.class)).thenReturn()

        assertDoesNotThrow(() -> userService.registerUser(userRegisterDto));
        verify(userAccountRepository, times(1)).save(any());
    }

    @Test
    void testRegisterUser_UserExists() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setLogin("existinguser");
        userRegisterDto.setPassword("password");
        userRegisterDto.setFirstName("Jane");
        userRegisterDto.setLastName("Doe");

        when(userAccountRepository.existsById("existinguser")).thenReturn(true);

        assertThrows(UserExistException.class, () -> userService.registerUser(userRegisterDto));
        verify(userAccountRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_UserExists() {
        // Arrange
        String login = "testUser";
        User user = new User();
        user.setLogin(login);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(user));

        // Act
        UserDto deletedUserDto = userService.deleteUser(login);

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).delete(user);
        verify(modelMapper, times(1)).map(user, UserDto.class);

        // Assert
        assertEquals(user.getLogin(), deletedUserDto.getLogin());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Arrange
        String login = "nonExistingUser";

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(login));

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, never()).delete(any());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void testUpdateUser_UserExists() {
        // Arrange
        String login = "testUser";
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setFirstName("John");
        userEditDto.setLastName("Doe");

        User existingUser = new User();
        existingUser.setLogin(login);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(existingUser));

        // Act
        UserDto updatedUserDto = userService.updateUser(login, userEditDto);

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).save(existingUser);
        verify(modelMapper, times(1)).map(existingUser, UserDto.class);

        // Assert
        assertEquals(existingUser.getFirstName(), userEditDto.getFirstName());
        assertEquals(existingUser.getLastName(), userEditDto.getLastName());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        String login = "nonExistingUser";
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setFirstName("John");
        userEditDto.setLastName("Doe");

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(login, userEditDto));

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void testGetUser_UserExists() {
        // Arrange
        String login = "testUser";
        User user = new User();
        user.setLogin(login);
        UserDto userDto = new UserDto();
        userDto.setLogin(login);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Act
        UserDto retrievedUserDto = userService.getUser(login);

        // Assert
        assertEquals(userDto.getLogin(), retrievedUserDto.getLogin());

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(modelMapper, times(1)).map(user, UserDto.class);
    }

    @Test
    void testGetUser_UserNotFound() {
        // Arrange
        String login = "nonExistingUser";

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUser(login));

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(modelMapper, never()).map(any(), any());
    }


    @Test
    void testChangeRolesList_AddRole() {
        // Arrange
        String login = "testUser";
        String role = "MODERATOR";
        boolean isAddRole = true;
        User user = new User(login, "password", "John", "Doe");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(user));
        when(userAccountRepository.save(user)).thenReturn(user);

        // Act
        RolesDto rolesDto = userService.changeRolesList(login, role, isAddRole);

        // Assert
        assertTrue(user.getRoles().contains(Role.MODERATOR));
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).save(user);
        verify(modelMapper, times(1)).map(user, RolesDto.class);
    }

    @Test
    void testChangeRolesList_RemoveRole() {
        // Arrange
        String login = "testUser";
        String role = "MODERATOR";
        boolean isAddRole = false;
        Set<Role> roles = new HashSet<>();
        roles.add(Role.MODERATOR);
        User user = new User(login, "password", "John", "Doe");
        user.setRoles(roles);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(user));
        when(userAccountRepository.save(user)).thenReturn(user);

        // Act
        RolesDto rolesDto = userService.changeRolesList(login, role, isAddRole);

        // Assert
        assertFalse(user.getRoles().contains(Role.MODERATOR));
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).save(user);
        verify(modelMapper, times(1)).map(user, RolesDto.class);
    }

    @Test
    void testChangeRolesList_UserNotFound() {
        // Arrange
        String login = "nonExistingUser";
        String role = "MODERATOR";
        boolean isAddRole = true;

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.changeRolesList(login, role, isAddRole));

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), eq(RolesDto.class));
    }


}

