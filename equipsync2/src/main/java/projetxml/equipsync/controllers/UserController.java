package projetxml.equipsync.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import projetxml.equipsync.Services.UserService;
import projetxml.equipsync.entities.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/me")

    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>((User) authentication.getPrincipal(), HttpStatus.OK);
    }
    @PostMapping("/hr/insert")
    public ResponseEntity<String> insertUser(@RequestBody User user) {
        // Assuming userService.insertUser() returns a message or status
        try {
            String response = userService.insertUser(user);
            return ResponseEntity.ok(response);  // Return 200 OK with response
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error inserting user: " + e.getMessage());  // Return error response
        }
    }
    @GetMapping("/hr/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/hr/update")
    public String updatetUser(@RequestBody User user) {
        return userService.updateUser(user) ;
    }

    @DeleteMapping  ("/hr/{id}")
    public String deleteUser(@PathVariable String id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }
}
