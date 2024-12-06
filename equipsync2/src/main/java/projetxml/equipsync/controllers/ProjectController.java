package projetxml.equipsync.controllers;

import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.Services.ProjectService;
import projetxml.equipsync.entities.Project;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public String addProject(@RequestBody Project project) throws Exception {
        return projectService.insertProject(project);
    }

    @GetMapping("/all/{managerId}")
    public String getAllProjects(String managerId) {
        return projectService.getAllProjectsByManger(managerId).toString();
    }

    @GetMapping("project/{id}")
    public Project getProjectById(@PathVariable String id) {
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProjectById(@PathVariable String id) {
        return projectService.deleteProjectById(id);
    }

    @PutMapping("/update")
    public String updateProject(@RequestBody Project project) {
        return projectService.updateProject(project);
    }
}

