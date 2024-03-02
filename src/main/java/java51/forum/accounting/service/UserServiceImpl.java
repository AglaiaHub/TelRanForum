package java51.forum.accounting.service;

import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.dto.UserRegisterDto;
import java51.forum.accounting.dto.RolesDto;
import java51.forum.accounting.dto.UserEditDto;
import java51.forum.accounting.dto.UserDto;
import java51.forum.accounting.dto.exception.UserExistException;
import java51.forum.accounting.dto.exception.UserNotFoundException;
import java51.forum.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final ModelMapper modelMapper;
    final UserAccountRepository userAccountRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistException();
        }
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto deleteUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);  //TODO: использовать FindById если поиск по id
        // todo или открыть опшнл

        String firstName = userEditDto.getFirstName();
        if (firstName != null) userAccount.setFirstName(firstName);

        String lastName = userEditDto.getLastName();
        if (lastName != null) userAccount.setLastName(lastName);

        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccountAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        boolean res;
        if (isAddRole) {
            res = userAccountAccount.addRole(role);
        } else {
            res = userAccountAccount.removeRole(role);
        }
        if(res) {
            userAccountRepository.save(userAccountAccount);
        }
        return modelMapper.map(userAccountAccount, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String password = passwordEncoder.encode(newPassword);
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userAccountRepository.existsById("admin")) {
            String password = passwordEncoder.encode("admin");
            UserAccount userAccountAccount = new UserAccount("admin", password, "", "");
            userAccountAccount.addRole("MODERATOR");
            userAccountAccount.addRole("ADMINISTRATOR");
            userAccountRepository.save(userAccountAccount);
        }
    }
}
