package projetxml.equipsync.entities;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.security.core.GrantedAuthority;
import projetxml.equipsync.security.InstantAdapter;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "user")
@XmlType(propOrder = {
        "userId", "username", "password", "email", "role",
         "refreshToken", "refreshToken_expiryDate","skills"
})
public class User {

    private String userId;
    private String username;
    private String password;
    private String email;
    private String role;
    private String refreshToken;
    private Instant refreshToken_expiryDate;
    private List<String> skills;

    public User() {}

    public User(String userId, String username, String password, String email,
                String role, String refreshToken,
                Instant refreshToken_expiryDate, List<String> skills) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.refreshToken = refreshToken;
        this.refreshToken_expiryDate = refreshToken_expiryDate;
        this.skills = skills;

    }

    // Ensure all getters have XmlElement annotations
    @XmlElement(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @XmlElement(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @XmlElement(name = "refreshToken")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    @XmlElement(name = "refreshToken_expiryDate")
    @XmlJavaTypeAdapter(InstantAdapter.class)
    public Instant getRefreshToken_expiryDate() {
        return refreshToken_expiryDate;
    }

    public void setRefreshToken_expiryDate(Instant refreshToken_expiryDate) {
        this.refreshToken_expiryDate = refreshToken_expiryDate;
    }



    @XmlElementWrapper(name = "skills")
    @XmlElement(name = "skill")
    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", skills=" + skills +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshToken_expiryDate=" + refreshToken_expiryDate +
                '}';
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.getRole())
                .stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());
    }
}