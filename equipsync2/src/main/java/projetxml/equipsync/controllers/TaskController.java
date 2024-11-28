package projetxml.equipsync.controllers;

import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.Services.TaskService;
import projetxml.equipsync.entities.Task;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public String addTask(@RequestBody Task task) {
        return taskService.insertTask(task);
    }

    @GetMapping("/all")
    public String getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteTaskById(@PathVariable int id) {
        return taskService.deleteTaskById(id);
    }

    @PutMapping("/update")
    public String updateTask(@RequestBody Task task) {
        return taskService.updateTask(task);
    }
}
