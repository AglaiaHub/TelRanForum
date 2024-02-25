package java51.forum.post;

import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.dto.RolesDto;
import java51.forum.accounting.dto.UserDto;
import java51.forum.accounting.dto.UserEditDto;
import java51.forum.accounting.dto.UserRegisterDto;
import java51.forum.accounting.dto.exception.UserExistException;
import java51.forum.accounting.dto.exception.UserNotFoundException;
import java51.forum.accounting.model.Role;
import java51.forum.accounting.model.UserAccount;
import java51.forum.accounting.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
//        when(modelMapper.map(userRegisterDto, UserAccount.class)).thenReturn()

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
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin(login);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(userAccount));

        // Act
        UserDto deletedUserDto = userService.deleteUser(login);

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).delete(userAccount);
        verify(modelMapper, times(1)).map(userAccount, UserDto.class);

        // Assert
        assertEquals(userAccount.getLogin(), deletedUserDto.getLogin());
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

        UserAccount existingUserAccount = new UserAccount();
        existingUserAccount.setLogin(login);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(existingUserAccount));

        // Act
        UserDto updatedUserDto = userService.updateUser(login, userEditDto);

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).save(existingUserAccount);
        verify(modelMapper, times(1)).map(existingUserAccount, UserDto.class);

        // Assert
        assertEquals(existingUserAccount.getFirstName(), userEditDto.getFirstName());
        assertEquals(existingUserAccount.getLastName(), userEditDto.getLastName());
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
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin(login);
        UserDto userDto = new UserDto();
        userDto.setLogin(login);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(userAccount));
        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);

        // Act
        UserDto retrievedUserDto = userService.getUser(login);

        // Assert
        assertEquals(userDto.getLogin(), retrievedUserDto.getLogin());

        // Verify
        verify(userAccountRepository, times(1)).findById(login);
        verify(modelMapper, times(1)).map(userAccount, UserDto.class);
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
        UserAccount userAccount = new UserAccount(login, "password", "John", "Doe");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        userAccount.setRoles(roles);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.save(userAccount)).thenReturn(userAccount);

        // Act
        RolesDto rolesDto = userService.changeRolesList(login, role, isAddRole);

        // Assert
        assertTrue(userAccount.getRoles().contains(Role.MODERATOR));
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).save(userAccount);
        verify(modelMapper, times(1)).map(userAccount, RolesDto.class);
    }

    @Test
    void testChangeRolesList_RemoveRole() {
        // Arrange
        String login = "testUser";
        String role = "MODERATOR";
        boolean isAddRole = false;
        Set<Role> roles = new HashSet<>();
        roles.add(Role.MODERATOR);
        UserAccount userAccount = new UserAccount(login, "password", "John", "Doe");
        userAccount.setRoles(roles);

        // Mocking behavior of userAccountRepository
        when(userAccountRepository.findById(login)).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.save(userAccount)).thenReturn(userAccount);

        // Act
        RolesDto rolesDto = userService.changeRolesList(login, role, isAddRole);

        // Assert
        assertFalse(userAccount.getRoles().contains(Role.MODERATOR));
        verify(userAccountRepository, times(1)).findById(login);
        verify(userAccountRepository, times(1)).save(userAccount);
        verify(modelMapper, times(1)).map(userAccount, RolesDto.class);
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

