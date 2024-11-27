package projetxml.equipsync.entities;

import org.springframework.scheduling.config.Task;

import java.util.List;

class Employee extends User {
    private int[] equipment;
    private int[] tasks;

    // Getters and Setters
    public int[] getEquipment() {
        return equipment;
    }

    public void setEquipment(int[] equipment) {
        this.equipment = equipment;
    }
    public int[] getTasks() {
        return tasks;
    }

    public void setTasks(int[] tasks) {
        this.tasks = tasks;
    }
}