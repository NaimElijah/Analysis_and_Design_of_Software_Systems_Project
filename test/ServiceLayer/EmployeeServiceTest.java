package ServiceLayer;

import DTOs.EmployeeDTO;
import DTOs.RoleDTO;
import DomainLayer.EmployeeSubModule.AuthorisationController;
import DomainLayer.EmployeeSubModule.BankAccount;
import DomainLayer.EmployeeSubModule.Employee;
import DomainLayer.EmployeeSubModule.EmployeeController;
import ServiceLayer.EmployeeSubModule.EmployeeService;
import ServiceLayer.exception.AuthorizationException;
import ServiceLayer.exception.EmployeeNotFoundException;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService employeeService;
    private EmployeeController employeeController;
    private AuthorisationController authorisationController;
    private Employee adminEmployee;
    private Employee managerEmployee;
    private Employee regularEmployee;
    private final long ADMIN_ID = 123456789L;
    private final long MANAGER_ID = 234567891L;
    private final long REGULAR_EMPLOYEE_ID = 345678912L;
    private final long TEST_EMPLOYEE_ID = 987654321L;
    private final long INVALID_ID = 999999999L;

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
        roles.put("DRIVER", new HashSet<>(Arrays.asList("DRIVE_TRUCK")));

        authorisationController = new AuthorisationController(roles, permissions);

        // Create test employees with different roles
        Set<Employee> employees = new HashSet<>();

        // Admin employee
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("ADMIN");
        Map<String, Object> adminTerms = new HashMap<>();
        adminTerms.put("bankAccount", new BankAccount(1L, 123L, 456L, 789L));
        adminEmployee = new Employee(ADMIN_ID, "Admin", "User", 50000, 
                                     adminTerms, adminRoles, 
                                     LocalDate.now().minusYears(1), true, 
                                     LocalDate.now().minusYears(1), LocalDate.now(),
                                     "Headquarters");
        employees.add(adminEmployee);

        // Manager employee
        Set<String> managerRoles = new HashSet<>();
        managerRoles.add("MANAGER");
        Map<String, Object> managerTerms = new HashMap<>();
        managerTerms.put("bankAccount", new BankAccount(2L, 234L, 567L, 890L));
        managerEmployee = new Employee(MANAGER_ID, "Manager", "User", 40000, 
                                      managerTerms, managerRoles, 
                                      LocalDate.now().minusYears(1), true, 
                                      LocalDate.now().minusYears(1), LocalDate.now(),
                                      "Tel Aviv Branch");
        employees.add(managerEmployee);

        // Regular employee
        Set<String> regularRoles = new HashSet<>();
        regularRoles.add("EMPLOYEE");
        Map<String, Object> regularTerms = new HashMap<>();
        regularTerms.put("bankAccount", new BankAccount(3L, 345L, 678L, 901L));
        regularEmployee = new Employee(REGULAR_EMPLOYEE_ID, "Regular", "User", 30000, 
                                      regularTerms, regularRoles, 
                                      LocalDate.now().minusYears(1), true, 
                                      LocalDate.now().minusYears(1), LocalDate.now(),
                                      "Jerusalem Branch");
        employees.add(regularEmployee);

        employeeController = new EmployeeController(employees, authorisationController);
        employeeService = new EmployeeService(employeeController, authorisationController);
    }

    // Helper method to create a test employee
    private void createTestEmployee() {
        String firstName = "David";
        String lastName = "Ben-Gurion";
        long salary = 40000;
        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("bankAccount", new BankAccount(2L, 111L, 222L, 333L));
        LocalDate startOfEmployment = LocalDate.now();
        String branch = "Tel Aviv Branch";

        employeeService.createEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, firstName, lastName, 
                                     salary, termsOfEmployment, startOfEmployment, branch);
    }

    @Nested
    @DisplayName("Employee Management Tests")
    class EmployeeManagementTests {

        @Test
        @DisplayName("Create employee successfully")
        void createEmployee() {
            // Test data
            String firstName = "David";
            String lastName = "Ben-Gurion";
            long salary = 40000;
            Map<String, Object> termsOfEmployment = new HashMap<>();
            termsOfEmployment.put("bankAccount", new BankAccount(2L, 111L, 222L, 333L));
            LocalDate startOfEmployment = LocalDate.now();

            // Test creating an employee
            String branch = "Tel Aviv Branch";
            String result = employeeService.createEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, firstName, lastName, 
                                                         salary, termsOfEmployment, startOfEmployment, branch);

            assertEquals("Employee created successfully", result);

            // Verify the employee was created
            EmployeeDTO employee = employeeService.getEmployeeByIdAsDTO(TEST_EMPLOYEE_ID);
            assertNotNull(employee);
            assertEquals(firstName, employee.getFirstName());
            assertEquals(lastName, employee.getLastName());
            assertEquals(salary, employee.getSalary());
            assertEquals(branch, employee.getBranchId());
            assertTrue(employee.isActive());
        }
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
        String newBranch = "Jerusalem Branch";
        String result = employeeService.updateEmployee(ADMIN_ID, TEST_EMPLOYEE_ID, newFirstName, 
                                                     newLastName, newSalary, newTermsOfEmployment, true, newBranch);

        assertEquals("Employee updated successfully", result);

        // Verify the employee was updated
        EmployeeDTO employee = employeeService.getEmployeeByIdAsDTO(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertEquals(newFirstName, employee.getFirstName());
        assertEquals(newLastName, employee.getLastName());
        assertEquals(newSalary, employee.getSalary());
        assertEquals(newBranch, employee.getBranchId());
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
        EmployeeDTO employee = employeeService.getEmployeeByIdAsDTO(TEST_EMPLOYEE_ID);
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
            if (role.contains(roleName)) {
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
        EmployeeDTO employee = employeeService.getEmployeeByIdAsDTO(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertTrue(employee.getRoles().contains("TEST_ROLE"));
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
        EmployeeDTO employee = employeeService.getEmployeeByIdAsDTO(TEST_EMPLOYEE_ID);
        assertNotNull(employee);
        assertFalse(employee.getRoles().contains("TEST_ROLE"));
    }

    @Test
    void addPermissionToRole() {
        // First create a role
        employeeService.createRole(ADMIN_ID, "TEST_ROLE");

        // Test adding a permission to the role
        String result = employeeService.addPermissionToRole(ADMIN_ID, "TEST_ROLE", "CREATE_EMPLOYEE");

        assertTrue(result.contains("added successfully"));

        // Verify the permission was added
        RoleDTO roleDTO = employeeService.getRoleDetailsAsDTO("TEST_ROLE");
        assertNotNull(roleDTO);
        assertTrue(roleDTO.getPermissions().contains("CREATE_EMPLOYEE"));
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
        RoleDTO roleDTO = employeeService.getRoleDetailsAsDTO("TEST_ROLE");
        assertNotNull(roleDTO);
        assertFalse(roleDTO.getPermissions().contains("CREATE_EMPLOYEE"));
    }

    @Test
    void getEmployeeById() {
        // First create an employee
        createTestEmployee();

        // Test getting the employee by ID
        EmployeeDTO employee = employeeService.getEmployeeByIdAsDTO(TEST_EMPLOYEE_ID);

        assertNotNull(employee);
        assertEquals(TEST_EMPLOYEE_ID, employee.getIsraeliId());
        assertEquals("David", employee.getFirstName());
        assertEquals("Ben-Gurion", employee.getLastName());
    }

    @Test
    void getEmployeeByIdNotFound() {
        // Test getting a non-existent employee
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeByIdAsDTO(999999999L);
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
                                              LocalDate.now().minusYears(1), LocalDate.now(),
                                              "Branch Office");

        employeeController.createEmployee(ADMIN_ID, 555555555L, "Regular", "User", 30000, 
                                        termsOfEmployment, regularRoles, LocalDate.now().minusYears(1),
                                        "Branch Office");

        // Test creating an employee with unauthorized user
        assertThrows(AuthorizationException.class, () -> {
            employeeService.createEmployee(555555555L, 111111111L, "Test", "User", 
                                         25000, termsOfEmployment, LocalDate.now(), "Test Branch");
        });
    }

}
