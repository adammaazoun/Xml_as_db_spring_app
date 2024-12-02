package projetxml.equipsync.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import projetxml.equipsync.Services.UserService;
import projetxml.equipsync.entities.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
    public String getAllUsers() {
        return userService.getAllUsers().toString();
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
