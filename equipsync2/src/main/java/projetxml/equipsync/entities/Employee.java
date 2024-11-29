package projetxml.equipsync.entities;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"userId", "username", "password", "email", "role", "equipment", "tasks"})
public class Employee extends User {
    private String equipmentId;
    private List<String> tasksIds;

    public Employee(String userId, String username, String password, String email, String role, List<String> skills) {
        super(userId, username, password, email, role, skills);
    }

    @XmlElement
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public List<String> getTasksIds() {
        return tasksIds;
    }

    public void setTasksIds(List<String> tasksIds) {
        this.tasksIds = tasksIds;
    }


    // Getter and Setter for 'equipment'



    // Getter and Setter for 'tasks'
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<String> getTasks() {
        return tasksIds;
    }

    public void setTasks(List<String> tasks) {
        this.tasksIds = tasks;
    }
}
