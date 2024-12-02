package projetxml.equipsync.entities;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.time.Instant;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"userId", "username", "password", "email", "role", "skills", "projects", "tasks", "equipment","refreshToken", "refreshToken_expiryDate"})
public class OperationsManager extends User {

    private List<String> projects;  // Directly use List<String> for projects
    public OperationsManager(String userId, String username, String password, String email, String role, String refreshToken, Instant refreshToken_expiryDate, List<String> skills) {
        super(userId, username, password, email, role,refreshToken,refreshToken_expiryDate, skills);
    }

    // Getter and Setter for 'projects'
    @XmlElementWrapper(name = "projects")
    @XmlElement(name = "project")  // Directly map each project as a "project" element
    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    // Other fields and methods can be inherited from User, which should be properly annotated
}
