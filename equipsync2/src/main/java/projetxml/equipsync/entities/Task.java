package projetxml.equipsync.entities;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

// Marking the class as an XML root element
@XmlRootElement(name = "task")
public class Task {
    private String taskId;
    private String equipmentId;
    private String projectId;
    private String description;

    // Default constructor required for JAXB
    public Task() {}

    // Constructor for convenience (optional)
    public Task(String taskId, String equipmentId, String projectId, String description) {
        this.taskId = taskId;
        this.equipmentId = equipmentId;
        this.projectId = projectId;
        this.description = description;
    }

    // Getters and setters with @XmlElement annotations

    @XmlElement(name = "taskId")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @XmlElement(name = "equipmentId")
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    @XmlElement(name = "projectId")
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
