package projetxml.equipsync.Services;

import org.basex.query.value.item.Str;
import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.Project;
import projetxml.equipsync.entities.Task;
import projetxml.equipsync.entities.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final BaseXService baseXService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final XmlService<Project> xmlService;

    public ProjectService(BaseXService baseXService) {
        this.baseXService = baseXService;
        this.xmlService = new XmlService<>(Project.class);
    }


    // Insert Project
    public String insertProject(Project project) throws Exception {
        String projectXml = "";
        try {
            projectXml = xmlService.serialize(project);
//            xmlService.validate(userXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\user.xsd");
            String xQuery = String.format(
                    "let $project := %s\n" +
                            "return insert node $project into doc('equipsync_db/Projects.xml')/projects",
                    projectXml
            );

            baseXService.openDatabase("equipsync_db");
            return projectXml + baseXService.executeXQuery(xQuery);
                
            } catch (Exception e) {
                return projectXml + "Error inserting task: " + e.getMessage();
            }
    }

    // Get all projects
    public List<Project> getAllProjects() {

        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $project in /projects/project return $project";
            String result = baseXService.executeXQuery(xQuery);

            // Split result into individual user XML strings and deserialize
            List<Project> projects = new ArrayList<>();
            for (String userXml : result.split("(?=<project>)")) {
                if (!userXml.trim().isEmpty()) {
                    projects.add(xmlService.deserialize(userXml));
                }
            }
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get project by ID
    public Project getProjectById(String id) {
        try {
            String xQuery = String.format(
                    "for $project in /projects/Project where $project/projectId = '%s' return $project",

                    id
            );
            baseXService.openDatabase("equipsync_db");
            return xmlService.deserialize(baseXService.executeXQuery(xQuery));
        } catch (Exception e) {
            return null;
        }
    }

    // Delete project by ID
    public String deleteProjectById(String id) {
        try {
            String xQuery = String.format(
                    "delete node doc('equipsync_db/Projects.xml')/projects/project[projectId = '%s']",
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
            // Convert project to XML using JAXB
            String projectXml = xmlService.serialize(project);

            // Delete the existing project node
            String deleteXQuery = String.format(
                    "delete node doc('equipsync_db/Projects.xml')/projects/project[projectId = '%s']",
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

    public List<Project> getAllProjectsByManger(String managerId) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $project in /projects/project return $project";
            String result = baseXService.executeXQuery(xQuery);

            // Split result into individual user XML strings and deserialize
            List<Project> projects = new ArrayList<>();
            for (String userXml : result.split("(?=<project>)")) {
                if (!userXml.trim().isEmpty()) {
                    projects.add(xmlService.deserialize(userXml));
                }
            }
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
