package projetxml.equipsync.Services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import projetxml.equipsync.entities.User;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import projetxml.equipsync.exeption.TokenRefreshException;
import projetxml.equipsync.security.AuthRequest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Service
public class UserService {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final BaseXService baseXService;
    private final XmlService<User> xmlService;
    @Value("${security.jwt.refresh-expiration-time}")
    private Long refreshTokenDurationMs;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(BaseXService baseXService, PasswordEncoder encoder) {
        this.baseXService = baseXService;
        this.encoder = encoder;
        this.xmlService = new XmlService<>(User.class);
    }

    // Serialize a User object into an XML string

    public boolean authenticateUser(String username, String rawPassword) {
        try {
            baseXService.openDatabase("equipsync_db");

            // Query to find the user by username
            String xQuery = String.format(
                    "for $user in /users/user " +
                            "where $user/username = '%s' " +
                            "return $user/password",
                    username
            );
            String storedPassword = baseXService.executeXQuery(xQuery).trim();

            // Compare hashed password
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(rawPassword, storedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String insertUser(User user) {
        String userXml = null;
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));

        try {
            userXml = xmlService.serialize(user);
//            xmlService.validate(userXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\user.xsd");
            String xQuery = String.format(
                    "let $user := %s\n" +
                            "return insert node $user into doc('equipsync_db/Users.xml')/users",
                    userXml
            );

            baseXService.openDatabase("equipsync_db");
            return userXml + baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return userXml + "Error inserting user: " + e.getMessage();
        }
    }

    public List<User> getAllUsers() {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $user in /users/user return $user";
            String result = baseXService.executeXQuery(xQuery);

            // Split result into individual user XML strings and deserialize
            List<User> users = new ArrayList<>();
            for (String userXml : result.split("(?=<user>)")) {
                if (!userXml.trim().isEmpty()) {
                    users.add(xmlService.deserialize(userXml));
                }
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public User getUserById(String id) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve a user node by ID
            String xQuery = String.format(
                    "for $user in /users/user where $user/id = '%s' return $user",
                    id
            );
            String result = baseXService.executeXQuery(xQuery);

            if (result.trim().isEmpty()) {
                return null; // User not found
            }

            // Deserialize and return the user
            return xmlService.deserialize(result.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve a user node by ID
            String xQuery = String.format(
                    "for $user in /users/user where $user/username = '%s' return $user",
                    username
            );
            String result = baseXService.executeXQuery(xQuery);

            if (result.trim().isEmpty()) {
                return null; // User not found
            }

            // Deserialize and return the user
            return xmlService.deserialize(result.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public User getUserByToken(String token) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve a user node by ID
            String xQuery = String.format(
                    "for $user in /users/user where $user/refreshToken = '%s' return $user",
                    token
            );
            String result = baseXService.executeXQuery(xQuery);

            if (result.trim().isEmpty()) {
                return null; // User not found
            }

            // Deserialize and return the user
            return xmlService.deserialize(result.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String deleteUserById(String id) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to delete a user node by ID
            String xQuery = String.format(
                    "for $user in /users/user where $user/userId = '%s' return delete node $user",
                    id
            );
            return xQuery+baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting user: " + e.getMessage();
        }
    }

    public String updateUser(User user) {
        try {
            this.deleteUserById(user.getUserId());
            return this.insertUser(user);
        } catch (Exception e) {
            return "Error updating user: " + e.getMessage();
        }
    }

    public boolean userExists(String id) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to check if a user node exists with the given ID
            String xQuery = String.format(
                    "exists(/users/user[id = '%s'])",
                    id
            );
            String result = baseXService.executeXQuery(xQuery);
            return "true".equalsIgnoreCase(result.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public User createRefreshToken(String username) {
        User user = this.getUserByUsername(username);
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setRefreshToken_expiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        this.updateUser(user);
        return user;
    }

    public void verifyExpiration(User user) {
        // If the refresh token is empty, throw an exception (i.e., user has no refresh token)
        if (user.getRefreshToken().isEmpty()) {
            throw new TokenRefreshException(user.getRefreshToken(), "No refresh token provided.");
        }

        // If the refresh token has expired, throw an exception
        if (user.getRefreshToken_expiryDate().compareTo(Instant.now()) < 0) {
            user.setRefreshToken("");  // Clear the refresh token in case of expiration
            throw new TokenRefreshException(user.getRefreshToken(), "Refresh token has expired. Please log in again.");
        }
    }


    public boolean isAuthenticated(AuthRequest authRequest) {
        // Fetch the user by username
        User user = this.getUserByUsername(authRequest.getUsername());

        if (user == null) {
            // User not found
            return false;
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
    }
}
