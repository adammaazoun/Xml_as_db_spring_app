package projetxml.equipsync.entities;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.Date;

// Marking the class as an XML root element
@XmlType(propOrder = {
        "taskId", "taskName","deadline","status", "equipmentId", "projectId", "description"
})
@XmlRootElement(name = "task")
public class Task {
    private String taskId;
    private String taskName;
    private Date deadline;
    private String status;
    private String equipmentId;
    private String projectId;
    private String description;



    public Task() {
    }

    public Task(String taskId, String taskName, Date deadline, String status, String equipmentId, String projectId, String description) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.deadline = deadline;
        this.status = status;
        this.equipmentId = equipmentId;
        this.projectId = projectId;
        this.description = description;
    }
    // Getters and setters with @XmlElement annotations

    @XmlAttribute(name = "taskId")
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
    @XmlElement(name = "taskName")

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    @XmlElement(name = "deadline")

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    @XmlElement(name = "status")

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", deadline=" + deadline +
                ", status='" + status + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
