package projetxml.equipsync.controllers;

import projetxml.equipsync.Services.UserService;
import projetxml.equipsync.entities.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/insert")
    public String insertUser(@RequestBody User user) {
        return userService.insertUser(user);
    }

    @GetMapping("/all")
    public String getAllUsers() {
        return userService.getAllUsers().toString();
    }

    @PostMapping("/update")
    public String updatetUser(@RequestBody User user) {
        return userService.updateUser(user) ;
    }

    @GetMapping ("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }
}
