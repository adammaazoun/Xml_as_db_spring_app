package projetxml.equipsync.Services;

import org.springframework.stereotype.Service;
import projetxml.equipsync.Services.BaseXService;
import projetxml.equipsync.entities.Employee;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import projetxml.equipsync.entities.Equipment;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    private final BaseXService baseXService;
    private final XmlService<Employee> xmlService;

    // Constructor
    public EmployeeService(BaseXService baseXService) {
        this.baseXService = baseXService;
        this.xmlService = new XmlService<>(Employee.class);
    }



    // Insert Employee
    public String insertEmployee(Employee employee) {
        try {
            // Serialize Employee to XML string
            String employeeXml = xmlService.serialize(employee);
            xmlService.validate(employeeXml, "C:\\Users\\maazo\\Documents\\Xml_as_db_spring_app\\equipsync2\\src\\main\\java\\projetxml\\equipsync\\xml_shemas\\user.xsd");

            // XQuery to insert
            String xQuery = String.format(
                    "let $employee := %s\n" +
                            "return insert node $employee into doc('equipsync_db/Employees.xml')/employees",
                    employeeXml
            );

            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error inserting employee: " + e.getMessage();
        }
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        try {
            baseXService.openDatabase("equipsync_db");

            // XQuery to retrieve all user nodes as XML
            String xQuery = "for $user in /users/user return $user";
            String result = baseXService.executeXQuery(xQuery);

            // Split result into individual user XML strings and deserialize
            List<Employee> employees = new ArrayList<>();
            for (String userXml : result.split("(?=<user>)")) {
                if (!userXml.trim().isEmpty()) {
                    employees.add(xmlService.deserialize(userXml));
                }
            }
            return employees;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get employee by ID
    public String getEmployeeById(int id) {
        try {
            String xQuery = String.format(
                    "for $employee in doc('equipsync_db/Employees.xml')/employees/employee " +
                            "where $employee/id = '%d' return $employee",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching employee with ID " + id + ": " + e.getMessage();
        }
    }

    // Delete employee by ID
    public String deleteEmployeeById(String id) {
        try {
            String xQuery = String.format(
                    "delete node doc('equipsync_db/Employees.xml')/employees/employee[id = '%s']",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting employee with ID " + id + ": " + e.getMessage();
        }
    }

    // Update Employee
    public String updateEmployee(Employee employee) {
        try {
            // Serialize Employee to XML string
            String employeeXml = xmlService.serialize(employee);

            // Delete the existing node
            String deleteXQuery = String.format(
                    "delete node doc('equipsync_db/Employees.xml')/employees/employee[id = '%s']",
                    employee.getUserId()
            );

            // Insert the updated node
            String insertXQuery = String.format(
                    "let $employee := %s\n" +
                            "return insert node $employee into doc('equipsync_db/Employees.xml')/employees",
                    employeeXml
            );

            baseXService.openDatabase("equipsync_db");
            baseXService.executeXQuery(deleteXQuery);
            return baseXService.executeXQuery(insertXQuery);
        } catch (Exception e) {
            return "Error updating employee with ID " + employee.getUserId() + ": " + e.getMessage();
        }
    }
}
