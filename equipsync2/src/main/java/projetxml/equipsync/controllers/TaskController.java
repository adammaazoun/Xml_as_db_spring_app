package projetxml.equipsync.controllers;

import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.Services.TaskService;
import projetxml.equipsync.entities.Task;

import java.net.StandardSocketOptions;


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

    @GetMapping("/all/{projectId}")
    public String getAllProjectTasks(String id) {
        return taskService.getAllTasks(id).toString();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteTaskById(@PathVariable String id) {
        return taskService.deleteTaskById(id);
    }

    @PutMapping("/update")
    public String updateTask(@RequestBody Task task) {
        return taskService.updateTask(task);
    }
}
