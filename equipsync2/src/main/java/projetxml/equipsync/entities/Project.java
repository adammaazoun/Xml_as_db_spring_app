package projetxml.equipsync.entities;

import jakarta.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

// Marking the class as an XML root element
@XmlRootElement(name = "Project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {


    private String projectId;

    private String operationManagerId;


    private List<String> tasks;

    private Date started;


    private Date deadline;


    private String description;


    private String photo;

    // Default constructor required for JAXB
    public Project() {}
    @XmlElement(name = "projectId")
    // Getters and setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    @XmlElement(name = "operationManagerId")

    public String getOperationManagerId() {
        return operationManagerId;
    }

    public void setOperationManagerId(String operationManagerId) {
        this.operationManagerId = operationManagerId;
    }
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
    @XmlElement(name = "started")

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }
    @XmlElement(name = "dead-line")
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @XmlElement(name = "photo")
    public String getPhoto() {
        return photo;
    }



    public void setPhoto(String photo) {
        this.photo = photo;
    }
    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", operationManagerId='" + operationManagerId + '\'' +
                ", tasks=" + tasks +
                ", started=" + started +
                ", deadline=" + deadline +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
