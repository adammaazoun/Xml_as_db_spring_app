package projetxml.equipsync.entities;

import java.util.Date;

public class Project {
    private int projectId;
    private int OperationManagerId;
    private int[] tasksid;
    private Date startd;
    private Date deadline;
    private String description;
    // Getters and Setters

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getOperationManagerId() {
        return OperationManagerId;
    }

    public void setOperationManagerId(int operationManagerId) {
        OperationManagerId = operationManagerId;
    }

    public int[] getTasksid() {
        return tasksid;
    }

    public void setTasksid(int[] tasksid) {
        this.tasksid = tasksid;
    }

    public Date getStartd() {
        return startd;
    }

    public void setStartd(Date startd) {
        this.startd = startd;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
