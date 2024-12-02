package projetxml.equipsync.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.Services.JwtService;
import projetxml.equipsync.Services.UserService;
import projetxml.equipsync.entities.User;
import projetxml.equipsync.exeption.TokenRefreshException;
import projetxml.equipsync.security.AuthRequest;
import projetxml.equipsync.security.AuthResponse;
import projetxml.equipsync.security.TokenRefreshRequest;
import projetxml.equipsync.security.TokenRefreshResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService service;
    private final JwtService jwtService;
    @Autowired
    AuthController(UserService service, JwtService jwtService ) {
        this.service = service;
        this.jwtService = jwtService;
    }



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        if (service.isAuthenticated(authRequest)) {
            String token = jwtService.generateToken(authRequest.getUsername());
            Date expiresIn = jwtService.extractExpiration(token);

            User user = service.createRefreshToken(authRequest.getUsername());
            AuthResponse authResponse = new AuthResponse(token,expiresIn , user.getRefreshToken());
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        // Retrieve the user by the refresh token
        User user = service.getUserByToken(requestRefreshToken);

        // Verify that the refresh token is valid (not empty and not expired)
        service.verifyExpiration(user);

        // Generate a new refresh token and access token only if the old token is valid
        user = service.createRefreshToken(user.getUserId());  // Creates a new refresh token for the user

        String newAccessToken = jwtService.generateToken(user.getUsername());  // Generate a new access token
        user.setRefreshToken(newAccessToken);  // Set the new refresh token
        service.updateUser(user);  // Update user with the new refresh token

        // Return the new access token and refresh token in the response
        return new ResponseEntity<>(new TokenRefreshResponse(newAccessToken, user.getRefreshToken()), HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!refreshToken.isEmpty() && service.userExists(service.getUserByToken(refreshToken).getUserId())) {
            User user=service.getUserByToken(refreshToken);
            user.setRefreshToken("");
            service.updateUser(user);
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }
    }
}
