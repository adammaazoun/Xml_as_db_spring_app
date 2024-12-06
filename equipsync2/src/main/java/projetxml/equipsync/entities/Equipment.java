package projetxml.equipsync.entities;

import jakarta.persistence.Lob;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "equipment")
@XmlType(propOrder = {
        "equipmentId" , "category" , "employeeId" , "tasks", "details", "status","photo"})
public class Equipment {

    private String equipmentId;
    private String category;
    private String employeeId;
    private List<String> tasks;
    private String details;
    private String status;
    private byte[] photo;

    public Equipment() {
    }

    public Equipment(String equipmentId, String category, String employeeId, List<String> tasks, String details, String status, byte[] photo) {
        this.equipmentId = equipmentId;
        this.category = category;
        this.employeeId = employeeId;
        this.tasks = tasks;
        this.details = details;
        this.status = status;
        this.photo = photo;
    }

    @XmlElement(name = "employeeId")

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
    @XmlElement(name = "photo")

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @XmlElement(name = "equipmentId")
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    @XmlElement(name = "details")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @XmlElement(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    @XmlElement(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId='" + equipmentId + '\'' +
                ", category='" + category + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", tasks=" + tasks +
                ", details='" + details + '\'' +
                ", status='" + status + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
