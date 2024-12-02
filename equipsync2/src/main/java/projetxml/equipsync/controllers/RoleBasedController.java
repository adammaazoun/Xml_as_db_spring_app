package projetxml.equipsync.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleBasedController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint accessible to everyone";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userEndpoint() {
        return "This endpoint is accessible only to users";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "This endpoint is accessible only to admins";
    }
}
