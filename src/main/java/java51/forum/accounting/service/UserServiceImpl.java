package java51.forum.accounting.service;

import java51.forum.accounting.dao.UserAccountRepository;
import java51.forum.accounting.dto.UserRegisterDto;
import java51.forum.accounting.dto.RolesDto;
import java51.forum.accounting.dto.UserEditDto;
import java51.forum.accounting.dto.UserDto;
import java51.forum.accounting.dto.exception.UserExistException;
import java51.forum.accounting.dto.exception.UserNotFoundException;
import java51.forum.accounting.model.User;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final ModelMapper modelMapper;
    final UserAccountRepository userAccountRepository;

    @Override
    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistException();
        }
        User user = modelMapper.map(userRegisterDto, User.class);
        String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
        user.setPassword(password);
        userAccountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto deleteUser(String login) {
        User user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        User user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);  //TODO: использовать FindById если поиск по id
        // todo или открыть опшнл

        String firstName = userEditDto.getFirstName();
        if (firstName != null) user.setFirstName(firstName);

        String lastName = userEditDto.getLastName();
        if (lastName != null) user.setLastName(lastName);

        userAccountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        User user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        User userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        boolean res;
        if (isAddRole) {
            res = userAccount.addRole(role);
        } else {
            res = userAccount.removeRole(role);
        }
        if(res) {
            userAccountRepository.save(userAccount);
        }
        return modelMapper.map(userAccount, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        User user = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(password);
        userAccountRepository.save(user);
    }





//    @Override
//    public void run(String... args) throws Exception {
//        if (!userRepository.existsById("admin"))
//    }
}
