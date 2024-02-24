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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserAccountServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setLogin("test_user");
        userRegisterDto.setPassword("password");
        userRegisterDto.setFirstName("John");
        userRegisterDto.setLastName("Doe");

        User user = new User();
        user.setLogin(userRegisterDto.getLogin());
        user.setPassword("hashed_password"); // Assuming password hashing is handled in the service
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());

        when(userAccountRepository.existsById(userRegisterDto.getLogin())).thenReturn(false);
        when(modelMapper.map(userRegisterDto, User.class)).thenReturn(user);
        when(userAccountRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDto registeredUser = userService.registerUser(userRegisterDto);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(user.getLogin(), registeredUser.getLogin());
        assertEquals(user.getFirstName(), registeredUser.getFirstName());
        assertEquals(user.getLastName(), registeredUser.getLastName());
        verify(userAccountRepository, times(1)).existsById(userRegisterDto.getLogin());
        verify(userAccountRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UserExists() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto();

        when(userAccountRepository.existsById(userRegisterDto.getLogin())).thenReturn(true);

        // Act and Assert
        assertThrows(UserExistException.class, () -> userService.registerUser(userRegisterDto));
        verify(userAccountRepository, times(1)).existsById(userRegisterDto.getLogin());
        verify(userAccountRepository, never()).save(any(User.class));
    }

}
