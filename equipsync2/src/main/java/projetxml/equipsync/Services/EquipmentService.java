package projetxml.equipsync.Services;


import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.Equipment;



@Service
public class EquipmentService {
    private final BaseXService baseXService;

    // Constructor
    public EquipmentService(BaseXService baseXService) {
        this.baseXService = baseXService;
    }

    // Validate XML against XSD


    // Insert Equipment
    public String insertEquipment(Equipment equipment, String xsdSchema) {
        try {
            String equipmentXml = String.format(
                    "<equipment>" +
                            "<equipmentId>%d</equipmentId>" +
                            "<details>%s</details>" +
                            "<status>%s</status>" +
                            "<employeeId>%d</employeeId>" +
                            "<categorie>%s</categorie>" +
                            "</equipment>",
                    equipment.getEquipmentId(),
                    equipment.getDetails(),
                    equipment.getStatus(),
                    equipment.getEmployeeId(),
                    equipment.getCategorie()
            );



            // XQuery to insert equipment
            String xQuery = String.format(
                    "let $equipment := %s\n" +
                            "return insert node $equipment into doc('EquipmentDatabase/Equipment.xml')/equipments",
                    equipmentXml
            );

            baseXService.openDatabase("EquipmentDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error inserting equipment: " + e.getMessage();
        }
    }

    // Get all equipment
    public String getAllEquipment() {
        try {
            String xQuery = "for $equipment in doc('EquipmentDatabase/Equipment.xml')/equipments/equipment return $equipment";
            baseXService.openDatabase("EquipmentDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching equipment: " + e.getMessage();
        }
    }

    // Get equipment by ID
    public String getEquipmentById(int id) {
        try {
            String xQuery = String.format(
                    "for $equipment in doc('EquipmentDatabase/Equipment.xml')/equipments/equipment " +
                            "where $equipment/equipmentId = '%d' return $equipment",
                    id
            );
            baseXService.openDatabase("EquipmentDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching equipment with ID " + id + ": " + e.getMessage();
        }
    }

    // Delete equipment by ID
    public String deleteEquipmentById(int id) {
        try {
            String xQuery = String.format(
                    "delete node doc('EquipmentDatabase/Equipment.xml')/equipments/equipment[equipmentId = '%d']",
                    id
            );
            baseXService.openDatabase("EquipmentDatabase");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting equipment with ID " + id + ": " + e.getMessage();
        }
    }

    // Update Equipment
    public String updateEquipment(Equipment equipment, String xsdSchema) {
        try {
            String equipmentXml = String.format(
                    "<equipment>" +
                            "<equipmentId>%d</equipmentId>" +
                            "<details>%s</details>" +
                            "<status>%s</status>" +
                            "<employeeId>%d</employeeId>" +
                            "<categorie>%s</categorie>" +
                            "</equipment>",
                    equipment.getEquipmentId(),
                    equipment.getDetails(),
                    equipment.getStatus(),
                    equipment.getEmployeeId(),
                    equipment.getCategorie()
            );



            // Delete the existing node
            String deleteXQuery = String.format(
                    "delete node doc('EquipmentDatabase/Equipment.xml')/equipments/equipment[equipmentId = '%d']",
                    equipment.getEquipmentId()
            );

            // Insert the updated node
            String insertXQuery = String.format(
                    "let $equipment := %s\n" +
                            "return insert node $equipment into doc('EquipmentDatabase/Equipment.xml')/equipments",
                    equipmentXml
            );

            baseXService.openDatabase("EquipmentDatabase");
            baseXService.executeXQuery(deleteXQuery);
            return baseXService.executeXQuery(insertXQuery);
        } catch (Exception e) {
            return "Error updating equipment with ID " + equipment.getEquipmentId() + ": " + e.getMessage();
        }
    }
}

