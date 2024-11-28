package projetxml.equipsync.Services;

import projetxml.equipsync.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final BaseXService baseXService;

    public UserService(BaseXService baseXService) {
        this.baseXService = baseXService;
    }

    public String insertUser(User user) {
        try {
            // Construct User XML string
            String userXml = String.format(
                    "<user>" +
                            "<id>%s</id>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                            "<role>%s</role>" +
                            "</user>",
                    user.getId() != null ? user.getId() : "",
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
            );

            // Properly format the XQuery statement with curly braces for the XML
            String xQuery =String.format(
                    "let $user := %s\n" +
                    "return insert node  $user into doc('UserDatabase/Users2.xml')/users",userXml);

            // Execute the query
            baseXService.openDatabase("UserDatabase");
            return baseXService.executeXQuery(xQuery)+xQuery;
        } catch (Exception e) {
            return "Error inserting user: " + e.getMessage();
        }
    }


    public String getAllUsers() {
        try {
            // XQuery to retrieve all user nodes
            baseXService.openDatabase("UserDatabase");
            String xQuery = "for $user in /users/user return $user";
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching users: " + e.getMessage();
        }
    }


    public String getUserById(int id) {
        try {
            baseXService.openDatabase("UserDatabase");
            String xQuery = String.format(
                    "for $user in /users/user where $user/id = '%s' return $user", id);
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching user by ID: " + e.getMessage();
        }
    }

    public String deleteUserById(int id) {
        try {
            baseXService.openDatabase("UserDatabase");
            String xQuery = String.format(
                    "for $user in /users/user where $user/id = '%s' return delete node $user", id);
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting user: " + e.getMessage();
        }
    }

    public String updateUser(User user) {
        try {
            baseXService.openDatabase("UserDatabase");
            // Update specific fields while preserving the XML structure
            String xQuery = String.format(
                    "for $user in /users/user where $user/id = '%s' " +
                            "return (replace value of node $user/username with '%s', " +
                            "replace value of node $user/email with '%s', " +
                            "replace value of node $user/role with '%s')",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
            );
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error updating user: " + e.getMessage();
        }
    }

    public boolean userExists(int id) {
        try {
            baseXService.openDatabase("UserDatabase");
            String xQuery = String.format(
                    "exists(/users/user[id = '%s'])", id);
            String result = baseXService.executeXQuery(xQuery);
            return "true".equalsIgnoreCase(result.trim());
        } catch (Exception e) {
            return false;
        }
    }
}
