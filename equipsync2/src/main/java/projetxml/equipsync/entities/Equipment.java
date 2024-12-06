package projetxml.equipsync.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "equipment")
@XmlType(propOrder = {"equipmentId", "details", "status", "employeeId", "category", "tasks","photo"})
public class Equipment {

    private String equipmentId;
    private String category;
    private String employeeId;
    private List<String> tasks;
    private String details;
    private String status;
    private String photo;

    public Equipment() {
    }

    public Equipment(String equipmentId, String category, String employeeId, List<String> tasks, String details, String status, String photo) {
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
    @XmlElement(name = "tasks")

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
    @XmlElement(name = "photo")

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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


}
