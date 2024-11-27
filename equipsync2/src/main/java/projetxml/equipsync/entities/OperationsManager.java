package projetxml.equipsync.entities;

import org.springframework.scheduling.config.Task;

import java.util.List;

class OperationsManager extends User {
    private int[] projects;

    // Getters and Setters


    public int[] getProjects() {
        return projects;
    }

    public void setProjects(int[] projects) {
        this.projects = projects;
    }
}
