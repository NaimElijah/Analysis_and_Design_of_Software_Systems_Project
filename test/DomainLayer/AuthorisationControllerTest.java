package DomainLayer;

import DomainLayer.exception.InvalidInputException;
import DomainLayer.exception.UnauthorizedPermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class AuthorisationControllerTest {

    AuthorisationController authorisationController;

    // Permissions
    final String CREATE_EMPLOYEE = "CREATE_EMPLOYEE";
    final String ADD_EMPLOYEE = "ADD_EMPLOYEE";
    final String UPDATE_EMPLOYEE = "UPDATE_EMPLOYEE";
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

        // Initialize permissions
        permissions = Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE, UPDATE_EMPLOYEE, CASHIER_PERMISSION);
        // Initialize roles
        roles = Map.of(
                ADMIN, new HashSet<>(Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE, UPDATE_EMPLOYEE)),
                CASHIER_ROLE, new HashSet<>(Set.of(CASHIER_PERMISSION))
        );
        // Initialize employees
        shira = new Employee(123456789,"Shira", "Shtinboch", 10000, null, Set.of(ADMIN), LocalDate.now().minusYears(5),true, LocalDate.now().minusYears(5), LocalDate.now());
        cochava = new Employee(123456788,"Cochava", "Shavit", 10000, null, Set.of(CASHIER_ROLE), LocalDate.now().minusYears(5),true, LocalDate.now().minusYears(5), LocalDate.now());
        // Initialize AuthorisationController
        authorisationController = new AuthorisationController(roles, permissions);
    }

    @Test
    void hasPermission() {
        assertTrue(authorisationController.hasPermission(shira, CREATE_EMPLOYEE)); // Shira has the ADMIN role which has the CREATE_EMPLOYEE permission

        // Test cases where employee doesn't have permission - should throw UnauthorizedPermission
        assertThrows(UnauthorizedPermissionException.class, () -> {
            authorisationController.hasPermission(shira, CASHIER_PERMISSION); // Shira does not have the CASHIER role
        });

        assertThrows(UnauthorizedPermissionException.class, () -> {
            authorisationController.hasPermission(cochava, CREATE_EMPLOYEE); // Cochava has the CASHIER role which does not have the CREATE_EMPLOYEE permission
        });

        // Test edge cases
        assertThrows(InvalidInputException.class, () -> {
            authorisationController.hasPermission(null, CREATE_EMPLOYEE);
        });

        assertThrows(InvalidInputException.class, () -> {
            authorisationController.hasPermission(shira, null);
        });

        assertThrows(InvalidInputException.class, () -> {
            authorisationController.hasPermission(shira, "");
        });
    }

    @Test
    void hasRole() {
        assertTrue(authorisationController.hasRole(shira, ADMIN)); // Shira has the ADMIN role
        assertFalse(authorisationController.hasRole(cochava, ADMIN)); // Cochava does not have the ADMIN role
        assertTrue(authorisationController.hasRole(cochava, CASHIER_ROLE)); // Cochava has the CASHIER role

        // Test edge cases
        assertThrows(InvalidInputException.class, () -> {
            authorisationController.hasRole(null, ADMIN);
        });

        assertThrows(InvalidInputException.class, () -> {
            authorisationController.hasRole(shira, null);
        });

        assertThrows(InvalidInputException.class, () -> {
            authorisationController.hasRole(shira, "");
        });
    }

    @Test
    void addRole() {
        assertTrue(authorisationController.addRole(cochava, ADMIN)); // Cochava does not have the ADMIN role
        assertFalse(authorisationController.addRole(cochava, CASHIER_ROLE)); // Cochava already has the CASHIER role
        assertTrue(authorisationController.addRole(shira, CASHIER_ROLE)); // Shira does not have the CASHIER role

        // Test edge cases
        assertThrows(InvalidInputException.class, () -> {
            authorisationController.addRole(null, ADMIN);
        });

        assertThrows(InvalidInputException.class, () -> {
            authorisationController.addRole(shira, null);
        });

        assertThrows(InvalidInputException.class, () -> {
            authorisationController.addRole(shira, "");
        });

        assertThrows(InvalidInputException.class, () -> {
            authorisationController.addRole(shira, "NON_EXISTENT_ROLE");
        });
    }

    @Test
    void removeRole() {
        assertTrue(authorisationController.removeRole(shira, ADMIN)); // Shira has the ADMIN role
        assertFalse(authorisationController.removeRole(cochava, ADMIN)); // Cochava does not have the ADMIN role
        assertTrue(authorisationController.removeRole(cochava, CASHIER_ROLE)); // Cochava has the CASHIER role
    }

    @Test
    void addPermissionToRole() {
        assertTrue(authorisationController.addPermissionToRole(ADMIN, CASHIER_PERMISSION)); // ADMIN role does not have the CASHIER permission
        assertFalse(authorisationController.addPermissionToRole(ADMIN, CREATE_EMPLOYEE)); // ADMIN role already has the CREATE_EMPLOYEE permission
    }

    @Test
    void addPermissionToRoleByName() {
        assertTrue(authorisationController.addPermissionToRole("ADMIN", CASHIER_PERMISSION)); // ADMIN role does not have the CASHIER permission
        assertFalse(authorisationController.addPermissionToRole("ADMIN", CREATE_EMPLOYEE)); // ADMIN role already has the CREATE_EMPLOYEE permission
    }

    @Test
    void removePermissionFromRole() {
        assertFalse(authorisationController.removePermissionFromRole("ADMIN", CASHIER_PERMISSION)); // ADMIN role doesn't have the CASHIER permission
        assertTrue(authorisationController.removePermissionFromRole("ADMIN", CREATE_EMPLOYEE)); // ADMIN role have the CREATE_EMPLOYEE permission
    }

    @Test
    void createRole() {
        assertTrue(authorisationController.createRole(999,"MANAGER", Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE))); // MANAGER role does not exist
        assertFalse(authorisationController.createRole(999,"ADMIN", Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE))); // ADMIN role already exists
    }

    @Test
    void deleteRole() {
        assertFalse(authorisationController.deleteRole("MANAGER")); // MANAGER role does not exist - cannot delete - False
        assertTrue(authorisationController.deleteRole("ADMIN")); // ADMIN role already exists
    }

    @Test
    void createPermission() {
        assertTrue(authorisationController.createPermission("MANAGER")); // MANAGER permission does not exist
        assertFalse(authorisationController.createPermission(CREATE_EMPLOYEE)); // CREATE_EMPLOYEE permission already exists
    }

    @Test
    void deletePermission() {
        assertFalse(authorisationController.deletePermission("MANAGER")); // MANAGER permission does not exist - cannot delete - False

        // CREATE_EMPLOYEE permission is in use by the ADMIN role, so it should throw an InvalidInput exception
        assertThrows(InvalidInputException.class, () -> {
            authorisationController.deletePermission(CREATE_EMPLOYEE);
        });

        // Create a new permission that is not in use by any role
        String NEW_PERMISSION = "NEW_PERMISSION";
        authorisationController.createPermission(NEW_PERMISSION);

        // Now we should be able to delete it
        assertTrue(authorisationController.deletePermission(NEW_PERMISSION));
    }
}
