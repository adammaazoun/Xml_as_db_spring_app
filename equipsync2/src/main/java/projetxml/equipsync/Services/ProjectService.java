package projetxml.equipsync.Services;

import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.Project;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class ProjectService {
    private final BaseXService baseXService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ProjectService(BaseXService baseXService) {
        this.baseXService = baseXService;
    }

    // Insert Project
    public String insertProject(Project project) {
        try {
            // Convert task IDs to a comma-separated string
            String tasks = project.getTasksid() != null
                    ? String.join(",", Arrays.stream(project.getTasksid()).mapToObj(String::valueOf).toArray(String[]::new))
                    : "";

            // Build XML for the project
            String projectXml = String.format(
                    "<project>" +
                            "<projectId>%d</projectId>" +
                            "<operationManagerId>%d</operationManagerId>" +
                            "<tasks>%s</tasks>" +
                            "<startDate>%s</startDate>" +
                            "<deadline>%s</deadline>" +
                            "<description>%s</description>" +
                            "</project>",
                    project.getProjectId(),
                    project.getOperationManagerId(),
                    tasks,
                    dateFormat.format(project.getStartd()),
                    dateFormat.format(project.getDeadline()),
                    project.getDescription()
            );

            // XQuery to insert the project
            String xQuery = String.format(
                    "let $project := %s\n" +
                            "return insert node $project into doc('ProjectDatabase/Projects.xml')/projects",
                    projectXml
            );

            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error inserting project: " + e.getMessage();
        }
    }

    // Get all projects
    public String getAllProjects() {
        try {
            String xQuery = "for $project in doc('equipsync_db/Projects.xml')/projects/project return $project";
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching projects: " + e.getMessage();
        }
    }

    // Get project by ID
    public String getProjectById(int id) {
        try {
            String xQuery = String.format(
                    "for $project in doc('equipsync_db/Projects.xml')/projects/project " +
                            "where $project/projectId = '%d' return $project",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching project with ID " + id + ": " + e.getMessage();
        }
    }

    // Delete project by ID
    public String deleteProjectById(int id) {
        try {
            String xQuery = String.format(
                    "delete node doc('equipsync_db/Projects.xml')/projects/project[projectId = '%d']",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting project with ID " + id + ": " + e.getMessage();
        }
    }

    // Update project
    public String updateProject(Project project) {
        try {
            // Convert task IDs to a comma-separated string
            String tasks = project.getTasksid() != null
                    ? String.join(",", Arrays.stream(project.getTasksid()).mapToObj(String::valueOf).toArray(String[]::new))
                    : "";

            // Build XML for the updated project
            String projectXml = String.format(
                    "<project>" +
                            "<projectId>%d</projectId>" +
                            "<operationManagerId>%d</operationManagerId>" +
                            "<tasks>%s</tasks>" +
                            "<startDate>%s</startDate>" +
                            "<deadline>%s</deadline>" +
                            "<description>%s</description>" +
                            "</project>",
                    project.getProjectId(),
                    project.getOperationManagerId(),
                    tasks,
                    dateFormat.format(project.getStartd()),
                    dateFormat.format(project.getDeadline()),
                    project.getDescription()
            );

            // Delete the existing project node
            String deleteXQuery = String.format(
                    "delete node doc('equipsync_db/Projects.xml')/projects/project[projectId = '%d']",
                    project.getProjectId()
            );

            // Insert the updated project node
            String insertXQuery = String.format(
                    "let $project := %s\n" +
                            "return insert node $project into doc('equipsync_db/Projects.xml')/projects",
                    projectXml
            );

            baseXService.openDatabase("equipsync_db");
            baseXService.executeXQuery(deleteXQuery);
            return baseXService.executeXQuery(insertXQuery);
        } catch (Exception e) {
            return "Error updating project with ID " + project.getProjectId() + ": " + e.getMessage();
        }
    }
}
