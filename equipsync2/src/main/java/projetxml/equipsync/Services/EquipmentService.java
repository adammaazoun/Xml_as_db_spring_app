package projetxml.equipsync.Services;

import org.springframework.beans.factory.annotation.Autowired;
import projetxml.equipsync.entities.Equipment;
import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
    public String insertEquipment(Equipment equipment) {
        try {
            // Serialize Equipment to XML using JAXB
            String equipmentXml = xmlService.serialize(equipment);
            xmlService.validate(equipmentXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\equipment.xsd");

            String xQuery = String.format(
                    "let $equipment := %s\n" +
                            "return insert node $equipment into doc('equipsync_db/Equipment.xml')/equipment",
                    equipmentXml
            );

            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error adding equipment: " + e.getMessage();
        }
    }

    // Serialize Equipment to XML using JAXB

    public String updateEquipment(Equipment updatedEquipment) {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $task in /equipments/equipment return $equipment";
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
                    "for $equipment in doc('equipsync_db/Equipment.xml')/equipment " +
                            "where $equipment/equipmentId = '%s' return $equipment",
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
            String xQuery = "for $equipment in doc('equipsync_db/Equipment.xml')/equipment return $equipment";
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
}
