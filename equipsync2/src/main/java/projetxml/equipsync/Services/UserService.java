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
            String xQuery = """
                    let $user := <user>
                                    <id>3</id>
                                    <username>karim</username>
                                    <email>jojo@example.com</email>
                                    <role>bro</role>
                                 </user>
                    return
                      insert node  $user into doc('UserDatabase/Users2.xml')/users""";

            // Execute the query
            baseXService.openDatabase("UserDatabase");
            return baseXService.executeXQuery(xQuery);
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
}
