package java51.forum.post;

import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.dto.UserDto;
import java51.forum.accounting.dto.UserRegisterDto;
import java51.forum.accounting.dto.exception.UserExistException;
import java51.forum.accounting.model.UserAccount;
import java51.forum.accounting.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

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

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin(userRegisterDto.getLogin());
        userAccount.setPassword("hashed_password"); // Assuming password hashing is handled in the service
        userAccount.setFirstName(userRegisterDto.getFirstName());
        userAccount.setLastName(userRegisterDto.getLastName());

        when(userAccountRepository.existsById(userRegisterDto.getLogin())).thenReturn(false);
        when(modelMapper.map(userRegisterDto, UserAccount.class)).thenReturn(userAccount);
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);

        // Act
        UserDto registeredUser = userService.registerUser(userRegisterDto);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(userAccount.getLogin(), registeredUser.getLogin());
        assertEquals(userAccount.getFirstName(), registeredUser.getFirstName());
        assertEquals(userAccount.getLastName(), registeredUser.getLastName());
        verify(userAccountRepository, times(1)).existsById(userRegisterDto.getLogin());
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    public void testRegisterUser_UserExists() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto();

        when(userAccountRepository.existsById(userRegisterDto.getLogin())).thenReturn(true);

        // Act and Assert
        assertThrows(UserExistException.class, () -> userService.registerUser(userRegisterDto));
        verify(userAccountRepository, times(1)).existsById(userRegisterDto.getLogin());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

}
