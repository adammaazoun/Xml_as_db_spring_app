package projetxml.equipsync.Services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.stereotype.Service;
import projetxml.equipsync.Services.BaseXService;
import projetxml.equipsync.entities.OperationsManager;
import jakarta.xml.bind.JAXBException;
import projetxml.equipsync.entities.Project;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OperationManagerService {
    private final BaseXService baseXService;
    private final XmlService<OperationsManager> xmlService;

    // Constructor
    public OperationManagerService(BaseXService baseXService) {
        this.baseXService = baseXService;
        this.xmlService = new XmlService<>(OperationsManager.class);
    }

    // Method to serialize OperationsManager to XML string

    public String insertOperationsManager(OperationsManager manager) {
        try {
            // Serialize OperationsManager to XML string
            String managerXml = xmlService.serialize(manager);
            xmlService.validate(managerXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\user.xsd");

            // XQuery to insert
            String xQuery = String.format(
                    "let $manager := %s\n" +
                            "return insert node $manager into doc('equipsync_db/OperationsManagers.xml')/operationsManagers",
                    managerXml
            );

            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error inserting operations manager: " + e.getMessage();
        }
    }

    // Get all operations managers
    public List<OperationsManager> getAllOperationsManagers() {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $user in /users/user return $user";
            String result = baseXService.executeXQuery(xQuery);

            // Split result into individual user XML strings and deserialize
            List<OperationsManager> users = new ArrayList<>();
            for (String userXml : result.split("(?=<user>)")) {
                if (!userXml.trim().isEmpty()) {
                    users.add(xmlService.deserialize(userXml));
                }
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get operations manager by ID
    public String getOperationsManagerById(String id) {
        try {
            String xQuery = String.format(
                    "for $manager in doc('equipsync_db/OperationsManagers.xml')/operationsManagers/operationsManager " +
                            "where $manager/id = '%s' return $manager",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching operations manager with ID " + id + ": " + e.getMessage();
        }
    }

    // Delete operations manager by ID
    public String deleteOperationsManagerById(String id) {
        try {
            String xQuery = String.format(
                    "delete node doc('equipsync_db/OperationsManagers.xml')/operationsManagers/operationsManager[id = '%d']",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting operations manager with ID " + id + ": " + e.getMessage();
        }
    }

    // Update OperationsManager
    public String updateOperationsManager(OperationsManager manager) {
        try {
            // Serialize OperationsManager to XML string
            String managerXml = xmlService.serialize(manager);

            // Delete the existing node
            String deleteXQuery = String.format(
                    "delete node doc('equipsync_db/OperationsManagers.xml')/operationsManagers/operationsManager[id = '%s']",
                    manager.getUserId()
            );

            // Insert the updated node
            String insertXQuery = String.format(
                    "let $manager := %s\n" +
                            "return insert node $manager into doc('equipsync_db/OperationsManagers.xml')/operationsManagers",
                    managerXml
            );

            baseXService.openDatabase("equipsync_db");
            baseXService.executeXQuery(deleteXQuery);
            return baseXService.executeXQuery(insertXQuery);
        } catch (Exception e) {
            return "Error updating operations manager with ID " + manager.getUserId() + ": " + e.getMessage();
        }
    }
}
