package projetxml.equipsync.Services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import projetxml.equipsync.entities.User;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

@Service
public class UserService {
    private final BaseXService baseXService;
    private final XmlService<User> xmlService;

    public UserService(BaseXService baseXService) {
        this.baseXService = baseXService;
        this.xmlService = new XmlService<>(User.class);
    }
    // Serialize a User object into an XML string


    public String insertUser(User user) {
        try {
            String userXml = xmlService.serialize(user);
            xmlService.validate(userXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\user.xsd");
            String xQuery = String.format(
                    "let $user := %s\n" +
                            "return insert node $user into doc('equipsync_db/Users.xml')/users",
                    userXml
            );

            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error inserting user: " + e.getMessage();
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

    public String deleteUserById(String id) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to delete a user node by ID
            String xQuery = String.format(
                    "for $user in /users/user where $user/id = '%s' return delete node $user",
                    id
            );
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting user: " + e.getMessage();
        }
    }

    public String updateUser(User user) {
        try {
            String userXml = xmlService.serialize(user);

            baseXService.openDatabase("equipsync_db");

            // XQuery to replace the user node with the updated XML
            String xQuery = String.format(
                    "for $user in /users/user where $user/id = '%s' " +
                            "return replace node $user with %s",
                    user.getUserId(),
                    userXml
            );
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error updating user: " + e.getMessage();
        }
    }

    public boolean userExists(int id) {
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
}
