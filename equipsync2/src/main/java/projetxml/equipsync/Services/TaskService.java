package projetxml.equipsync.Services;


import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.Task;

@Service
public class TaskService {
    private final BaseXService baseXService;

    public TaskService(BaseXService baseXService) {
        this.baseXService = baseXService;
    }

    // Insert Task
    public String insertTask(Task task) {
        try {
            // Build XML for the task
            String taskXml = String.format(
                    "<task>" +
                            "<taskId>%d</taskId>" +
                            "<equipmentId>%d</equipmentId>" +
                            "<projectId>%d</projectId>" +
                            "<description>%s</description>" +
                            "</task>",
                    task.getTaskId(),
                    task.getEquipmentId(),
                    task.getProjectId(),
                    task.getDescription()
            );

            // XQuery to insert the task
            String xQuery = String.format(
                    "let $task := %s\n" +
                            "return insert node $task into doc('TaskDatabase/Tasks.xml')/tasks",
                    taskXml
            );

            baseXService.openDatabase("TaskDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error inserting task: " + e.getMessage();
        }
    }

    // Get All Tasks
    public String getAllTasks() {
        try {
            String xQuery = "for $task in doc('TaskDatabase/Tasks.xml')/tasks/task return $task";
            baseXService.openDatabase("TaskDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching tasks: " + e.getMessage();
        }
    }

    // Get Task by ID
    public String getTaskById(int id) {
        try {
            String xQuery = String.format(
                    "for $task in doc('TaskDatabase/Tasks.xml')/tasks/task " +
                            "where $task/taskId = '%d' return $task",
                    id
            );
            baseXService.openDatabase("TaskDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching task with ID " + id + ": " + e.getMessage();
        }
    }

    // Delete Task by ID
    public String deleteTaskById(int id) {
        try {
            String xQuery = String.format(
                    "delete node doc('TaskDatabase/Tasks.xml')/tasks/task[taskId = '%d']",
                    id
            );
            baseXService.openDatabase("TaskDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting task with ID " + id + ": " + e.getMessage();
        }
    }

    // Update Task
    public String updateTask(Task task) {
        try {
            // Build XML for the updated task
            String taskXml = String.format(
                    "<task>" +
                            "<taskId>%d</taskId>" +
                            "<equipmentId>%d</equipmentId>" +
                            "<projectId>%d</projectId>" +
                            "<description>%s</description>" +
                            "</task>",
                    task.getTaskId(),
                    task.getEquipmentId(),
                    task.getProjectId(),
                    task.getDescription()
            );

            // Delete the existing task node
            String deleteXQuery = String.format(
                    "delete node doc('TaskDatabase/Tasks.xml')/tasks/task[taskId = '%d']",
                    task.getTaskId()
            );

            // Insert the updated task node
            String insertXQuery = String.format(
                    "let $task := %s\n" +
                            "return insert node $task into doc('TaskDatabase/Tasks.xml')/tasks",
                    taskXml
            );

            baseXService.openDatabase("TaskDatabase");
            baseXService.executeXQuery(deleteXQuery);
            return baseXService.executeXQuery(insertXQuery);
        } catch (Exception e) {
            return "Error updating task with ID " + task.getTaskId() + ": " + e.getMessage();
        }
    }
}

