package ServiceLayer;

import DomainLayer.*;
import ServiceLayer.exception.AuthorizationException;
import ServiceLayer.exception.EmployeeNotFoundException;
import ServiceLayer.exception.ValidationException;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService employeeService;
    private EmployeeController employeeController;
    private AuthorisationController authorisationController;
    private Employee adminEmployee;
    private final long ADMIN_ID = 123456789L;
    private final long TEST_EMPLOYEE_ID = 987654321L;

    @BeforeEach
    void setUp() {
        // Create permissions
        Set<String> permissions = new HashSet<>(Arrays.asList(
            "CREATE_EMPLOYEE", "UPDATE_EMPLOYEE", "DEACTIVATE_EMPLOYEE", 
            "CREATE_ROLE", "ROLE_PERMISSION", "ADD_PERMISSION_TO_ROLE", 
            "REMOVE_PERMISSION_FROM_ROLE", "CREATE_PERMISSION", "EDIT_EMPLOYEE"
        ));

        // Create roles with permissions
        Map<String, HashSet<String>> roles = new HashMap<>();
        roles.put("ADMIN", new HashSet<>(permissions)); // Admin has all permissions
        roles.put("MANAGER", new HashSet<>(Arrays.asList("CREATE_EMPLOYEE", "UPDATE_EMPLOYEE")));
        roles.put("EMPLOYEE", new HashSet<>(Collections.emptyList()));

        authorisationController = new AuthorisationController(roles, permissions);

        // Create a test employee with admin role
        Set<Employee> employees = new HashSet<>();
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("ADMIN");

        // Create terms of employment map
        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("bankAccount", new BankAccount(1L, 123L, 456L, 789L));

        adminEmployee = new Employee(ADMIN_ID, "Admin", "User", 50000, 
                                     termsOfEmployment, adminRoles, 
                                     LocalDate.now().minusYears(1), true, 
                                     LocalDate.now().minusYears(1), LocalDate.now());
        employees.add(adminEmployee);

        employeeController = new EmployeeController(employees, authorisationController);
        employeeService = new EmployeeService(employeeController, authorisationController);
    }

    @Test
    void createEmployee() {
        // Test data
        String firstName = "David";
        String lastName = "Ben-Gurion";
        long salary = 40000;
        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("bankAccount", new BankAccount(2L, 111L, 222L, 333L));
        LocalDate startOfEmployment = LocalDate.now();

        // Test creating an employee
        String result = employeeService.createEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, firstName, lastName, 
                                                     salary, termsOfEmployment, startOfEmployment);

        assertEquals("Employee created successfully", result);

        // Verify the employee was created
        EmployeeSL employee = employeeService.getEmployeeById(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(salary, employee.getSalary());
        assertTrue(employee.isActive());
    }

    @Test
    void updateEmployee() {
        // First create an employee
        createTestEmployee();

        // Test data for update
        String newFirstName = "Pola";
        String newLastName = "Ben-Gurion";
        long newSalary = 45000;
        Map<String, Object> newTermsOfEmployment = new HashMap<>();
        newTermsOfEmployment.put("bankAccount", new BankAccount(3L, 444L, 555L, 666L));

        // Test updating the employee
        String result = employeeService.updateEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, newFirstName, 
                                                     newLastName, newSalary, newTermsOfEmployment, true);

        assertEquals("Employee updated successfully", result);

        // Verify the employee was updated
        EmployeeSL employee = employeeService.getEmployeeById(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertEquals(newFirstName, employee.getFirstName());
        assertEquals(newLastName, employee.getLastName());
        assertEquals(newSalary, employee.getSalary());
        assertTrue(employee.isActive());
    }

    @Test
    void deactivateEmployee() {
        // First create an employee
        createTestEmployee();

        // Test deactivating the employee
        String result = employeeService.deactivateEmployee(ADMIN_ID, TEST_EMPLOYEE_ID);

        assertEquals("Employee deactivated successfully", result);

        // Verify the employee was deactivated
        EmployeeSL employee = employeeService.getEmployeeById(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertFalse(employee.isActive());
    }

    @Test
    void createRole() {
        // Test data
        String roleName = "TEST_ROLE";

        // Test creating a role
        String result = employeeService.createRole(ADMIN_ID, roleName);

        assertTrue(result.contains("created successfully"));

        // Verify the role was created
        String[] roles = employeeService.getAllRoles();
        boolean roleFound = false;
        for (String role : roles) {
            if (role.equals(roleName)) {
                roleFound = true;
                break;
            }
        }
        assertTrue(roleFound);
    }

    @Test
    void addRoleToEmployee() {
        // First create an employee and a role
        createTestEmployee();
        employeeService.createRole(ADMIN_ID, "TEST_ROLE");

        // Test adding the role to the employee
        String result = employeeService.addRoleToEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, "TEST_ROLE");

        assertTrue(result.contains("added successfully"));

        // Verify the role was added
        EmployeeSL employee = employeeService.getEmployeeById(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertTrue(employee.hasRole("TEST_ROLE"));
    }

    @Test
    void removeRoleFromEmployee() {
        // First create an employee, a role, and add the role to the employee
        createTestEmployee();
        employeeService.createRole(ADMIN_ID, "TEST_ROLE");
        employeeService.addRoleToEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, "TEST_ROLE");

        // Test removing the role from the employee
        String result = employeeService.removeRoleFromEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, "TEST_ROLE");

        assertTrue(result.contains("removed successfully"));

        // Verify the role was removed
        EmployeeSL employee = employeeService.getEmployeeById(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertFalse(employee.hasRole("TEST_ROLE"));
    }

    @Test
    void addPermissionToRole() {
        // First create a role
        employeeService.createRole(ADMIN_ID, "TEST_ROLE");

        // Test adding a permission to the role
        String result = employeeService.addPermissionToRole(ADMIN_ID, "TEST_ROLE", "CREATE_EMPLOYEE");

        assertTrue(result.contains("added successfully"));

        // Verify the permission was added
        Map<String, HashSet<String>> roleDetails = employeeService.getRoleDetails("TEST_ROLE");
        assertNotNull(roleDetails);
        assertTrue(roleDetails.get("TEST_ROLE").contains("CREATE_EMPLOYEE"));
    }

    @Test
    void removePermissionFromRole() {
        // First create a role and add a permission to it
        employeeService.createRole(ADMIN_ID, "TEST_ROLE");
        employeeService.addPermissionToRole(ADMIN_ID, "TEST_ROLE", "CREATE_EMPLOYEE");

        // Test removing the permission from the role
        String result = employeeService.removePermissionFromRole(ADMIN_ID, "TEST_ROLE", "CREATE_EMPLOYEE");

        assertTrue(result.contains("removed successfully"));

        // Verify the permission was removed
        Map<String, HashSet<String>> roleDetails = employeeService.getRoleDetails("TEST_ROLE");
        assertNotNull(roleDetails);
        assertFalse(roleDetails.get("TEST_ROLE").contains("CREATE_EMPLOYEE"));
    }

    @Test
    void getEmployeeById() {
        // First create an employee
        createTestEmployee();

        // Test getting the employee by ID
        EmployeeSL employee = employeeService.getEmployeeById(TEST_EMPLOYEE_ID);

        assertNotNull(employee);
        assertEquals(TEST_EMPLOYEE_ID, employee.getId());
        assertEquals("David", employee.getFirstName());
        assertEquals("Ben-Gurion", employee.getLastName());
    }

    @Test
    void getEmployeeByIdNotFound() {
        // Test getting a non-existent employee
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(999999999L);
        });
    }

    @Test
    void createEmployeeUnauthorized() {
        // Create an employee without admin role
        Set<String> regularRoles = new HashSet<>();
        regularRoles.add("EMPLOYEE");

        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("bankAccount", new BankAccount(4L, 777L, 888L, 999L));

        Employee regularEmployee = new Employee(555555555L, "Regular", "User", 30000, 
                                              termsOfEmployment, regularRoles, 
                                              LocalDate.now().minusYears(1), true, 
                                              LocalDate.now().minusYears(1), LocalDate.now());

        employeeController.createEmployee(ADMIN_ID, 555555555L, "Regular", "User", 30000, 
                                        termsOfEmployment, regularRoles, LocalDate.now().minusYears(1));

        // Test creating an employee with unauthorized user
        assertThrows(AuthorizationException.class, () -> {
            employeeService.createEmployee(555555555L, 111111111L, "Test", "User", 
                                         25000, termsOfEmployment, LocalDate.now());
        });
    }

    // Helper method to create a test employee
    private void createTestEmployee() {
        String firstName = "David";
        String lastName = "Ben-Gurion";
        long salary = 40000;
        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("bankAccount", new BankAccount(2L, 111L, 222L, 333L));
        LocalDate startOfEmployment = LocalDate.now();

        employeeService.createEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, firstName, lastName, 
                                     salary, termsOfEmployment, startOfEmployment);
    }
}
