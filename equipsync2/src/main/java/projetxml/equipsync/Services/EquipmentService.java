package projetxml.equipsync.Services;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import projetxml.equipsync.entities.Equipment;
import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.User;
import projetxml.equipsync.request.Affect_req;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentService {
    @Autowired
    private final BaseXService baseXService;
    private final XmlService<Equipment> xmlService;

    public EquipmentService(BaseXService baseXService, XmlService<Equipment> xmlService) {
        this.baseXService = baseXService;
        this.xmlService = new XmlService<>(Equipment.class);
    }

    // Add equipment to XML database
    public String insertEquipment(Equipment equipment) throws Exception{
        String equipmentXml = "";
        equipmentXml = xmlService.serialize(equipment);
        try {
            equipmentXml = xmlService.serialize(equipment);

//            xmlService.validate(userXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\user.xsd");
            String xQuery = String.format(
                    "let $equipment := %s\n" +
                            "return insert node $equipment into doc('equipsync_db/Equipments.xml')/equipments",
                    equipmentXml
            );

            baseXService.openDatabase("equipsync_db");
            return equipmentXml + baseXService.executeXQuery(xQuery);

        } catch (Exception e) {
            return equipmentXml + "Error inserting equipment: " + e.getMessage();
        }
    }

    // Serialize Equipment to XML using JAXB

    public String updateEquipment(Equipment updatedEquipment) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $equipment in /equipments/equipment return $equipment";
            String result = baseXService.executeXQuery(xQuery);


            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get equipment by equipmentId
    public Equipment getEquipmentById(String equipmentId) {
        try {
            String xQuery = String.format(
                    "for $equipment in /equipments/equipment where $equipment/equipmentId = '%s' return $equipment",
                    equipmentId
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

    // Delete equipment by equipmentId
    public String deleteEquipmentById(String equipmentId) {
        try {
            String xQuery = String.format(
                    "delete node doc('equipsync_db/Equipment.xml')/equipment[equipmentId = '%s']",
                    equipmentId
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting equipment with ID " + equipmentId + ": " + e.getMessage();
        }
    }

    // Get all equipment
    public List<Equipment> getAllEquipment() {
        try {
            String xQuery = "for $equipment in /equipments/equipment return $equipment";
            baseXService.openDatabase("equipsync_db");
            String result = baseXService.executeXQuery(xQuery);


            List<Equipment> users = new ArrayList<>();
            for (String userXml : result.split("(?=<equipment>)")) {
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


    public Equipment getEquipmentByUserId(String id) {
        try {
            String xQuery = String.format(
                    "for $equipment in doc('equipsync_db/Equipment.xml')/equipment " +
                            "where $equipment/userId = '%s' return $equipment",
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


    public String affect(Affect_req req) {
        if (req == null || req.getEquipmentId() == null || req.getUserId() == null) {
            throw new IllegalArgumentException("Invalid request: Equipment ID and User ID must not be null.");
        }

        // Try to fetch the equipment by ID
        Equipment equipment = this.getEquipmentById(req.getEquipmentId());

        if (equipment.getEmployeeId() != null) {
            throw new IllegalStateException("Equipment with ID " + req.getEquipmentId() + " is already assigned to an employee.");
        }

        // Assign the user ID to the equipment
        equipment.setEmployeeId(req.getUserId());

        // Save the changes (assuming you have a method to update equipment)
        this.updateEquipment(equipment);

        return "Equipment with ID " + req.getEquipmentId() + " successfully assigned to user with ID " + req.getUserId() + ".";
    }

}
