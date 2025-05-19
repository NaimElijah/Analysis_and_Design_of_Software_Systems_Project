package DomainLayer;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import DomainLayer.EmployeeSubModule.*;
import DomainLayer.enums.ShiftType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.jdi.LocalVariable;


public class DataInitializer {
    private String dataDirectory;
    private ObjectMapper jsonMapper;

    public DataInitializer(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.registerModule(new JavaTimeModule()); // For handling LocalDate
    }

    /**
     * Initialize the system with data from files
     */
    public SystemInitData initializeFromFiles() throws IOException {
        // Load permissions and roles
        AuthorisationController authController = initializeAuthController();

        // Load employees
        EmployeeController employeeController = initializeEmployeeController(authController);

        // Load shifts
        ShiftController shiftController = initializeShiftController(employeeController);

        return new SystemInitData(authController, employeeController, shiftController);
    }

    /**
     * Initialize the system with minimal data (only permissions and basic admin role)
     * This is used when the system needs to be started with minimal configuration
     */
    public SystemInitData initializeMinimal() throws IOException {
        // Load only permissions from file
        Set<String> permissions = loadPermissions();

        // Create a basic admin role with all permissions
        Map<String, HashSet<String>> roles = new HashMap<>();
        HashSet<String> adminPermissions = new HashSet<>(permissions);
        roles.put("Admin", adminPermissions);

        // Create the authorization controller
        AuthorisationController authController = new AuthorisationController(roles, permissions);

        // Create a basic admin employee
        Set<Employee> employees = new HashSet<>();
        long adminId = 123456789;
        String adminFirstName = "Admin";
        String adminLastName = "User";
        long adminSalary = 20000;
        Map<String, Object> adminTerms = new HashMap<>();
        adminTerms.put("Position", "System Administrator");
        adminTerms.put("Department", "IT");
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("Admin");
        LocalDate adminStartDate = LocalDate.of(2020, 1, 1);

        Employee admin = new Employee(
            adminId, 
            adminFirstName, 
            adminLastName, 
            adminSalary, 
            adminTerms, 
            adminRoles, 
            adminStartDate, 
            true, 
            LocalDate.now(), 
            LocalDate.now()
        );
        employees.add(admin);

        // Create the employee controller
        EmployeeController employeeController = new EmployeeController(employees, authController);

        // Create an empty shift controller
        ShiftController shiftController = new ShiftController(new HashSet<>(), authController, employeeController);

        return new SystemInitData(authController, employeeController, shiftController);
    }

    /**
     * Initialize the AuthorisationController with data from files
     */
    private AuthorisationController initializeAuthController() throws IOException {
        // Load permissions from file
        Set<String> permissions = loadPermissions();

        // Load roles and their permissions from file
        Map<String, HashSet<String>> roles = loadRoles();

        return new AuthorisationController(roles, permissions);
    }

    /**
     * Initialize the EmployeeController with data from files
     */
    private EmployeeController initializeEmployeeController(AuthorisationController authController) throws IOException {
        // Load employees from file
        Set<Employee> employees = loadEmployees();

        return new EmployeeController(employees, authController);
    }

    /**
     * Initialize the ShiftController with data from files
     */
    private ShiftController initializeShiftController(EmployeeController employeeController) throws IOException {
        // Load shifts from file
        Set<Shift> shifts = loadShifts(employeeController);

        return new ShiftController(shifts, employeeController.getAuthorisationController(), employeeController);
    }

    /**
     * Load permissions from a JSON file
     */
    private Set<String> loadPermissions() throws IOException {
//        String filePath = dataDirectory + "/permissions.json";
//        JsonNode rootNode = jsonMapper.readTree(new File(filePath));

        // for JAR
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("permissions.json");
        JsonNode rootNode = jsonMapper.readTree(inputStream);


        Set<String> permissions = new HashSet<>();
        if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                permissions.add(node.asText());
            }
        }

        return permissions;
    }

    /**
     * Load roles and their permissions from a JSON file
     */
    private Map<String, HashSet<String>> loadRoles() throws IOException {
        // String filePath = dataDirectory + "/roles.json";
        // JsonNode rootNode = jsonMapper.readTree(new File(filePath));

        // for JAR
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("roles.json");
        JsonNode rootNode = jsonMapper.readTree(inputStream);

        Map<String, HashSet<String>> roles = new HashMap<>();

        if (rootNode.isObject()) {
            rootNode.fields().forEachRemaining(entry -> {
                String roleName = entry.getKey();
                HashSet<String> rolePermissions = new HashSet<>();

                if (entry.getValue().isArray()) {
                    for (JsonNode permNode : entry.getValue()) {
                        rolePermissions.add(permNode.asText());
                    }
                }

                roles.put(roleName, rolePermissions);
            });
        }

        return roles;
    }

    /**
     * Load employees from a JSON file
     */
    private Set<Employee> loadEmployees() throws IOException {
        // String filePath = dataDirectory + "/employees.json";
        // JsonNode rootNode = jsonMapper.readTree(new File(filePath));

        // for JAR
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("employees.json");
        JsonNode rootNode = jsonMapper.readTree(inputStream);

        Set<Employee> employees = new HashSet<>();

        if (rootNode.isArray()) {
            for (JsonNode empNode : rootNode) {
                long israeliId = empNode.get("israeliId").asLong();
                String firstName = empNode.get("firstName").asText();
                String lastName = empNode.get("lastName").asText();
                long salary = empNode.get("salary").asLong();

                // Parse terms of employment
                Map<String, Object> terms = new HashMap<>();
                JsonNode termsNode = empNode.get("termsOfEmployment");
                if (termsNode != null && termsNode.isObject()) {
                    termsNode.fields().forEachRemaining(entry -> {
                        terms.put(entry.getKey(), entry.getValue().asText());
                    });
                }

                // Parse roles
                Set<String> roles = new HashSet<>();
                JsonNode rolesNode = empNode.get("roles");
                if (rolesNode != null && rolesNode.isArray()) {
                    for (JsonNode roleNode : rolesNode) {
                        roles.add(roleNode.asText());
                    }
                }

                // Parse dates
                LocalDate startDate = LocalDate.parse(empNode.get("startOfEmployment").asText());
                boolean isActive = empNode.get("isActive").asBoolean();
                LocalDate creationDate = LocalDate.parse(empNode.get("creationDate").asText());
                LocalDate updateDate = LocalDate.parse(empNode.get("updateDate").asText());

                Employee employee = new Employee(
                    israeliId, firstName, lastName, salary, terms, roles,
                    startDate, isActive, creationDate, updateDate
                );

                employees.add(employee);
            }
        }

        return employees;
    }

    /**
     * Load shifts from a JSON file
     */
    private Set<Shift> loadShifts(EmployeeController employeeController) throws IOException {
        // String filePath = dataDirectory + "/shifts.json";
        // JsonNode rootNode = jsonMapper.readTree(new File(filePath));

        // for JAR
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("shifts.json");
        JsonNode rootNode = jsonMapper.readTree(inputStream);

        Set<Shift> shifts = new HashSet<>();

        if (rootNode.isArray()) {
            for (JsonNode shiftNode : rootNode) {
                long id = shiftNode.get("id").asLong();
                ShiftType shiftType = ShiftType.valueOf(shiftNode.get("shiftType").asText());
                LocalDate shiftDate = LocalDate.parse(shiftNode.get("shiftDate").asText());
                boolean isAssignedShitManager = shiftNode.get("isAssignedShitManager").asBoolean();
                boolean isOpen = shiftNode.get("isOpen").asBoolean();
                // Get start and end hours directly from the JSON
                LocalTime startHours = LocalTime.parse(shiftNode.get("startHour").asText());
                LocalTime endHours = LocalTime.parse(shiftNode.get("endHour").asText());

                LocalDate updateDate = LocalDate.parse(shiftNode.get("updateDate").asText());

                // Parse roles required
                Map<String, Integer> rolesRequired = new HashMap<>();
                JsonNode rolesRequiredNode = shiftNode.get("rolesRequired");
                if (rolesRequiredNode != null && rolesRequiredNode.isObject()) {
                    rolesRequiredNode.fields().forEachRemaining(entry -> {
                        rolesRequired.put(entry.getKey(), entry.getValue().asInt());
                    });
                }

                // Parse assigned employees
                Map<String, Set<Long>> assignedEmployees = new HashMap<>();
                JsonNode assignedEmployeesNode = shiftNode.get("assignedEmployees");
                if (assignedEmployeesNode != null && assignedEmployeesNode.isObject()) {
                    assignedEmployeesNode.fields().forEachRemaining(entry -> {
                        String role = entry.getKey();
                        Set<Long> employees = new HashSet<>();

                        if (entry.getValue().isArray()) {
                            for (JsonNode empIdNode : entry.getValue()) {
                                long empId = empIdNode.asLong();
                                Employee emp = employeeController.getEmployeeByIsraeliId(empId);
                                if (emp != null) {
                                    employees.add(emp.getIsraeliId());
                                }
                            }
                        }

                        assignedEmployees.put(role, employees);
                    });
                }

                // Parse available employees
                Set<Long> availableEmployees = new HashSet<>();
                JsonNode availableEmployeesNode = shiftNode.get("AvailableEmployees");
                if (availableEmployeesNode != null && availableEmployeesNode.isArray()) {
                    for (JsonNode empIdNode : availableEmployeesNode) {
                        long empId = empIdNode.asLong();
                        Employee emp = employeeController.getEmployeeByIsraeliId(empId);
                        if (emp != null) {
                            availableEmployees.add(emp.getIsraeliId());
                        }
                    }
                }

                Shift shift = new Shift(
                    id, shiftType, shiftDate, rolesRequired, assignedEmployees,
                    availableEmployees, isAssignedShitManager, isOpen, startHours, endHours, updateDate
                );

                shifts.add(shift);
            }
        }

        return shifts;
    }

    /**
     * Helper class to return all controllers
     */
    public static class SystemInitData {
        private final AuthorisationController authController;
        private final EmployeeController employeeController;
        private final ShiftController shiftController;

        public SystemInitData(AuthorisationController authController, EmployeeController employeeController, ShiftController shiftController) {
            this.authController = authController;
            this.employeeController = employeeController;
            this.shiftController = shiftController;
        }

        public AuthorisationController getAuthController() {
            return authController;
        }

        public EmployeeController getEmployeeController() {
            return employeeController;
        }

        public ShiftController getShiftController() {
            return shiftController;
        }
    }
}
