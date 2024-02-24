package java51.forum.accounting.controller;

import java51.forum.accounting.service.UserService;
import java51.forum.accounting.dto.UserRegisterDto;
import java51.forum.accounting.dto.RolesDto;
import java51.forum.accounting.dto.UserEditDto;
import java51.forum.accounting.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class UserAccountController {
    final UserService userService;

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.registerUser(userRegisterDto);
    }

//    @PostMapping("/login")
//    public UserDto login(@RequestHeader("Authorization")  String token){
//        token = token.split(" ")[1];
//        String credentionals = new String(Base64.getDecoder().decode(token));
//        return userService.getUser(credentionals.split(":")[0]);
//    }

    @PostMapping
    public UserDto login(Principal principal) {
        return userService.getUser(principal.getName());
    }

    @DeleteMapping("/user/{login}")
    public UserDto deleteUser(@PathVariable("login") String login) {
        return userService.deleteUser(login);
    }

    @PutMapping("/user/{user}")
    public UserDto updateUser(@PathVariable("user") String login, @RequestBody UserEditDto userEditDto) {
        return userService.updateUser(login, userEditDto);
    }

    @GetMapping ("/user/{user}")
    public UserDto getUser(@PathVariable("user") String login) {
        return userService.getUser(login);
    }

    @PutMapping ("/user/{user}/role/{role}")
    public RolesDto addRole(@PathVariable("user") String login, @PathVariable String role) {
        return userService.changeRolesList(login, role, true);
    }

    @DeleteMapping("/user/{user}/role/{role}")
    public RolesDto deleteRole(@PathVariable("user") String login, @PathVariable String role) {
        return userService.changeRolesList(login, role, false);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword){
        userService.changePassword(principal.getName(), newPassword);
    }
}
