package projetxml.equipsync.Services;

import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.Equipment;
import projetxml.equipsync.entities.Task;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final BaseXService baseXService;
    private final XmlService<Task> xmlService;
    private final EquipmentService equipmentService;


    public TaskService(BaseXService baseXService,  EquipmentService equipmentService) {
        this.baseXService = baseXService;
        this.equipmentService = equipmentService;
        this.xmlService = new XmlService<>(Task.class);
    }

    // Insert Task
    public String insertTask(Task task) {
        try {
            String taskXml = xmlService.serialize(task);
            xmlService.validate(taskXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\task.xsd");

            // XQuery to insert the task
            String xQuery = String.format(
                    "let $task := %s\n" +
                            "return insert node $task into doc('equipsync_db/Tasks.xml')/tasks",
                    taskXml
            );

            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error inserting task: " + e.getMessage();
        }
    }

    // Get All Tasks
    public List<Task> getAllTasks(String id) {

        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $task in /tasks/task  return $task";
            String result = baseXService.executeXQuery(xQuery);

            // Split result into individual user XML strings and deserialize
            List<Task> tasks = new ArrayList<>();
            for (String userXml : result.split("(?=<task>)")) {
                if (!userXml.trim().isEmpty()) {
                    tasks.add(xmlService.deserialize(userXml));
                }
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Task> getTasksByUserId(String id) throws Exception {
        List<Task> taskList = new ArrayList<>();
        Equipment equipment=equipmentService.getEquipmentByUserId(id);
        return this.getTasksByEquipmentId(equipment.getEquipmentId());
    }

    private List<Task> getTasksByEquipmentId(String equipmentId) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve tasks filtered by equipmentId
            String xQuery = String.format(
                    "for $task in doc('equipsync_db/Tasks.xml')/tasks/task " +
                            "where $task/equipmentId = '%s' return $task",
                    equipmentId
            );

            String result = baseXService.executeXQuery(xQuery);

            // Split result into individual task XML strings and deserialize
            List<Task> tasks = new ArrayList<>();
            for (String taskXml : result.split("(?=<task>)")) {
                if (!taskXml.trim().isEmpty()) {
                    tasks.add(xmlService.deserialize(taskXml.trim()));
                }
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public Task getTaskById(String id) {
        try {
            String xQuery = String.format(
                    "for $task in doc('equipsync_db/Tasks.xml')/tasks/task " +
                            "where $task/taskId = '%s' return $task",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            String result = baseXService.executeXQuery(xQuery);

            if (result.trim().isEmpty()) {
                return null; // User not found
            }

            // Deserialize and return the user
            return xmlService.deserialize(result.trim());
        } catch (Exception e) {
            return null;
        }
    }

    // Delete Task by ID
    public String deleteTaskById(String id) {
        try {
            String xQuery = String.format(
                    "delete node doc('equipsync_db/Tasks.xml')/tasks/task[taskId = '%s']",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting task with ID " + id + ": " + e.getMessage();
        }
    }

    // Update Task
    public String updateTask(Task task) {
        try {
            String taskXml = xmlService.serialize(task);

            // Delete the existing task node
            String deleteXQuery = String.format(
                    "delete node doc('equipsync_db/Tasks.xml')/tasks/task[taskId = '%s']",
                    task.getTaskId()
            );

            // Insert the updated task node
            String insertXQuery = String.format(
                    "let $task := %s\n" +
                            "return insert node $task into doc('equipsync_db/Tasks.xml')/tasks",
                    taskXml
            );

            baseXService.openDatabase("equipsync_db");
            baseXService.executeXQuery(deleteXQuery);
            return baseXService.executeXQuery(insertXQuery);
        } catch (Exception e) {
            return "Error updating task with ID " + task.getTaskId() + ": " + e.getMessage();
        }
    }
}
