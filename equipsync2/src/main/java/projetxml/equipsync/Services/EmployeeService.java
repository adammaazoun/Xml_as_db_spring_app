package projetxml.equipsync.Services;


import org.springframework.stereotype.Service;
import projetxml.equipsync.entities.Employee;

@Service
public class EmployeeService {
    private final BaseXService baseXService;

    public EmployeeService(BaseXService baseXService) {
        this.baseXService = baseXService;
    }

    public String addEmployee(Employee employee) {
        try {
            String employeeXml = String.format(
                    "<employee>" +
                            "<id>%s</id>" +
                            "<username>%s</username>" +
                            "<email>%s</email>" +
                            "<role>%s</role>" +
                            "<equipment>%s</equipment>" +
                            "<tasks>%s</tasks>" +
                            "</employee>",
                    employee.getId() != null ? employee.getId() : "",
                    employee.getUsername(),
                    employee.getEmail(),
                    employee.getRole(),
                    formatArray(employee.getEquipment()),
                    formatArray(employee.getTasks())
            );

            String xQuery = String.format(
                    "let $employee := %s\n" +
                            "return insert node $employee into doc('equipsync_db/Employees.xml')/employees",
                    employeeXml
            );

            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error adding employee: " + e.getMessage();
        }
    }

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

    public String deleteEmployeeById(int id) {
        try {
            String xQuery = String.format(
                    "delete node doc('equipsync_db/Employees.xml')/employees/employee[id = '%d']",
                    id
            );
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error deleting employee with ID " + id + ": " + e.getMessage();
        }
    }

    public String getAllEmployees() {
        try {
            String xQuery = "for $employee in doc('equipsync_db/Employees.xml')/employees/employee return $employee";
            baseXService.openDatabase("equipsync_db");
            return baseXService.executeXQuery(xQuery);
        } catch (Exception e) {
            return "Error fetching all employees: " + e.getMessage();
        }
    }

    private String formatArray(int[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder formattedArray = new StringBuilder();
        for (int value : array) {
            formattedArray.append("<value>").append(value).append("</value>");
        }
        return formattedArray.toString();
    }
}

