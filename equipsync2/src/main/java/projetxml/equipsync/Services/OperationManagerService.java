package projetxml.equipsync.services;

import org.springframework.stereotype.Service;
import projetxml.equipsync.Services.BaseXService;
import projetxml.equipsync.entities.OperationsManager;

@Service
public class OperationManagerService {
    private final BaseXService baseXService;

    // Constructor
    public OperationManagerService(BaseXService baseXService) {
        this.baseXService = baseXService;
    }

    // Insert OperationsManager
    public String insertOperationsManager(OperationsManager manager) {
        try {
            // Convert array to comma-separated string for XML
            String projects = manager.getProjects() != null
                    ? String.join(",",
                    java.util.Arrays.stream(manager.getProjects())
                            .mapToObj(String::valueOf)
                            .toArray(String[]::new))
                    : "";

            // Build XML representation
            String managerXml = String.format(
                    "<operationsManager>" +
                            "<id>%d</id>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                            "<role>%s</role>" +
                            "<projects>%s</projects>" +
                            "</operationsManager>",
                    manager.getId(),
                    manager.getUsername(),
                    manager.getEmail(),
                    manager.getRole(),
                    projects
            );

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
    public String getAllOperationsManagers() {
        try {
            String xQuery = "for $manager in doc('equipsync_db/OperationsManagers.xml')/operationsManagers/operationsManager return $manager";
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching operations managers: " + e.getMessage();
        }
    }

    // Get operations manager by ID
    public String getOperationsManagerById(int id) {
        try {
            String xQuery = String.format(
                    "for $manager in doc('equipsync_db/OperationsManagers.xml')/operationsManagers/operationsManager " +
                            "where $manager/id = '%d' return $manager",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching operations manager with ID " + id + ": " + e.getMessage();
        }
    }

    // Delete operations manager by ID
    public String deleteOperationsManagerById(int id) {
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
            // Convert array to comma-separated string for XML
            String projects = manager.getProjects() != null
                    ? String.join(",",
                    java.util.Arrays.stream(manager.getProjects())
                            .mapToObj(String::valueOf)
                            .toArray(String[]::new))
                    : "";

            // Build XML representation
            String managerXml = String.format(
                    "<operationsManager>" +
                            "<id>%d</id>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                            "<role>%s</role>" +
                            "<projects>%s</projects>" +
                            "</operationsManager>",
                    manager.getId(),
                    manager.getUsername(),
                    manager.getEmail(),
                    manager.getRole(),
                    projects
            );

            // Delete the existing node
            String deleteXQuery = String.format(
                    "delete node doc('equipsync_db/OperationsManagers.xml')/operationsManagers/operationsManager[id = '%d']",
                    manager.getId()
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
            return "Error updating operations manager with ID " + manager.getId() + ": " + e.getMessage();
        }
    }
}
