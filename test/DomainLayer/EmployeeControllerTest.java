package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class EmployeeControllerTest {

    AuthorisationController authorisationController;
    EmployeeController employeeController;

    // Permissions
    final String CREATE_EMPLOYEE = "CREATE_EMPLOYEE";
    final String ADD_EMPLOYEE = "ADD_EMPLOYEE";
    final String UPDATE_EMPLOYEE = "UPDATE_EMPLOYEE";
    final String DELETE_EMPLOYEE = "DELETE_EMPLOYEE";
    final String CASHIER_PERMISSION = "CASHIER";

    // Roles
    final String ADMIN = "ADMIN";
    final String CASHIER_ROLE = "CASHIER";

    // Controller Attributes
    Set<String> permissions;
    Map<String, HashSet<String>> roles;

    // Employees
    Employee shira;
    Employee cochava;

    @BeforeEach
    void setUp() {

        permissions = Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE, UPDATE_EMPLOYEE, DELETE_EMPLOYEE);
        roles = Map.of(
                ADMIN, new HashSet<>(Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE, UPDATE_EMPLOYEE, DELETE_EMPLOYEE)),
                CASHIER_ROLE, new HashSet<>(Set.of(CASHIER_PERMISSION))
        );
        shira = new Employee(123456789,"Shira", "Shtinboch", 10000, null, Set.of(ADMIN), LocalDate.now().minusYears(5),true, LocalDate.now().minusYears(5), LocalDate.now());
        cochava = new Employee(123456788,"Cochava", "Shavit", 10000, null, Set.of(CASHIER_ROLE), LocalDate.now().minusYears(5),true, LocalDate.now().minusYears(5), LocalDate.now());

        authorisationController = new AuthorisationController(roles, permissions);
        Set<Employee> employees = new HashSet<>();
        employees.add(shira);
        employees.add(cochava);
        employeeController = new EmployeeController(employees, authorisationController);
    }

    @Test
    void getEmployeeByIsraeliId() {
        // Test for existing employee
        Employee employee = employeeController.getEmployeeByIsraeliId(shira.getIsraeliId());
        assertNotNull(employee);
        assertEquals(shira, employee);

        // Test for non-existing employee
        employee = employeeController.getEmployeeByIsraeliId(99999);
        assertNull(employee);
    }

    @Test
    void createEmployee() {
        // Test for creating a new employee
        Set<String> roles = Set.of(CASHIER_ROLE);
        LocalDate startOfEmployment = LocalDate.now();
        Map<String, Object> terms = Map.of("Days Off", 5000, "Contract Type", "Full Time");

        boolean result = employeeController.createEmployee(shira.getIsraeliId(), 12345, "Ramzi", "Abed-Ramzi", 5000, terms, roles, startOfEmployment);
        assertTrue(result);

        // Test for creating an existing employee
        assertThrows(RuntimeException.class, () -> {
            employeeController.createEmployee(shira.getIsraeliId(), shira.getIsraeliId(), "Shira", "Shtinboch", 10000, terms, roles, startOfEmployment);
        });

        // Test for invalid input
        assertThrows(RuntimeException.class, () -> {
            employeeController.createEmployee(shira.getIsraeliId(), 12345, "", "Employee", 5000, terms, roles, startOfEmployment);
        });
    }

    @Test
    void updateEmployee() {
        // Test for updating an existing employee
        Set<String> roles = Set.of(CASHIER_ROLE);
        Map<String, Object> terms = Map.of("Days Off", 5000, "Contract Type", "Full Time");
        boolean result = employeeController.updateEmployee(shira.getIsraeliId(), shira.getIsraeliId(), "Shira", "Shtinboch", 12000, terms, true);
        assertTrue(result);

        // Test for updating a non-existing employee
        assertThrows(RuntimeException.class, () -> {
            employeeController.updateEmployee(shira.getIsraeliId(), 99999, "Non-Existing", "Employee", 5000, terms, true);
        });

        // Test for invalid input
        assertThrows(RuntimeException.class, () -> {
            employeeController.updateEmployee(shira.getIsraeliId(), shira.getIsraeliId(), "", "Employee", 5000, terms, true);
        });
    }

    @Test
    void deleteEmployee() {
        // Test for deleting an existing employee
        boolean result = employeeController.deleteEmployee(shira.getIsraeliId(), shira.getIsraeliId());
        assertTrue(result);

        // Test for deleting a non-existing employee
        assertThrows(RuntimeException.class, () -> {
            employeeController.deleteEmployee(shira.getIsraeliId(), 99999);
        });

        // Test for invalid input
        assertThrows(RuntimeException.class, () -> {
            employeeController.deleteEmployee(shira.getIsraeliId(), shira.getIsraeliId());
        });
    }

    @Test
    void isEmployeeAuthorised() {
        // Test for authorisation
        boolean result = employeeController.isEmployeeAuthorised(shira.getIsraeliId(), CREATE_EMPLOYEE);
        assertTrue(result);

        // Test for non-authorisation
        result = employeeController.isEmployeeAuthorised(cochava.getIsraeliId(), CREATE_EMPLOYEE);
        assertFalse(result);

        // Test for invalid input
        assertThrows(RuntimeException.class, () -> {
            employeeController.isEmployeeAuthorised(99999, CREATE_EMPLOYEE);
        });
    }

    @Test
    void addRoleToEmployee() {
        // Test for adding a new role
        assertThrows(RuntimeException.class, () -> {
            employeeController.addRoleToEmployee(shira.getIsraeliId(), cochava.getIsraeliId(), "CASHIER");
        });

        // Test for adding an existing role
        assertThrows(RuntimeException.class, () -> {
            employeeController.addRoleToEmployee(shira.getIsraeliId(),cochava.getIsraeliId() ,"CASHIER");
        });

        // Test for invalid input
        assertThrows(RuntimeException.class, () -> {
            employeeController.addRoleToEmployee(shira.getIsraeliId(),cochava.getIsraeliId(), "cashier");
        });
    }

    @Test
    void removeRoleFromEmployee() {
        // Test for removing an existing role
        assertThrows(RuntimeException.class, () -> {
            employeeController.removeRoleFromEmployee(shira.getIsraeliId(), cochava.getIsraeliId(), "CASHIER");
        });

        // Test for removing a non-existing role
        assertThrows(RuntimeException.class, () -> {
            employeeController.removeRoleFromEmployee(shira.getIsraeliId(), cochava.getIsraeliId(), "NON_EXISTING_ROLE");
        });

        // Test for invalid input
        assertThrows(RuntimeException.class, () -> {
            employeeController.removeRoleFromEmployee(shira.getIsraeliId(), cochava.getIsraeliId(), "cashier");
        });
    }
}