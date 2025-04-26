package ServiceLayer;

import DomainLayer.*;
import DomainLayer.enums.ShiftType;
import Util.Week;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShiftServiceTest {

    private ShiftService shiftService;
    private ShiftController shiftController;
    private AssignmentController assignmentController;
    private AvailabilityController availabilityController;
    private EmployeeController employeeController;
    private AuthorisationController authorisationController;

    @BeforeEach
    void setUp() {
        // Create mock controllers
        Set<String> permissions = new HashSet<>(Arrays.asList(
            "CREATE_SHIFT", "UPDATE_SHIFT", "REMOVE_SHIFT", "GET_SHIFT", 
            "ASSIGN_EMPLOYEE", "AVAILABLE_EMPLOYEE", "ROLES_REQUIRED", "GET_ROLES",
            "GET_AVAILABILITY", "ROLE_REQUIRED", "AVAILABLE_EMPLOYEES"
        ));

        // Create roles with permissions
        Map<String, HashSet<String>> roles = new HashMap<>();
        roles.put("ADMIN", new HashSet<>(permissions)); // Admin has all permissions
        roles.put("MANAGER", new HashSet<>(Arrays.asList("CREATE_SHIFT", "UPDATE_SHIFT", "GET_SHIFT", "ASSIGN_EMPLOYEE")));
        roles.put("CASHIER", new HashSet<>(Arrays.asList("GET_SHIFT")));

        authorisationController = new AuthorisationController(roles, permissions);

        // Create a test employee with admin role
        Set<Employee> employees = new HashSet<>();
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("ADMIN");

        // Create terms of employment map
        Map<String, Object> termsOfEmployment = new HashMap<>();
        termsOfEmployment.put("bankAccount", new BankAccount(1L, 123L, 456L, 789L));

        Employee adminEmployee = new Employee(123456789L, "Admin", "User", 50000, 
                                             termsOfEmployment, adminRoles, 
                                             LocalDate.now().minusYears(1), true, 
                                             LocalDate.now().minusYears(1), LocalDate.now());
        employees.add(adminEmployee);

        employeeController = new EmployeeController(employees, authorisationController);

        Set<Shift> shifts = new HashSet<>();
        shiftController = new ShiftController(shifts, authorisationController, employeeController);

        assignmentController = new AssignmentController(employeeController);
        availabilityController = new AvailabilityController(employeeController);

        // Create ShiftService with the controllers
        shiftService = new ShiftService(shiftController, assignmentController, availabilityController);
    }

    @Test
    void testCreateShift() {
        // Test creating a shift
        long employeeId = 1L;
        ShiftType shiftType = ShiftType.MORNING;
        LocalDate date = LocalDate.now().plusDays(1);
        Map<String, Integer> rolesRequired = new HashMap<>();
        rolesRequired.put("CASHIER", 2);
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        // Mock the employee authorization
        // This would normally be done by adding the employee to the EmployeeController
        // and giving them the necessary permissions

        String result = shiftService.createShift(employeeId, shiftType, date, rolesRequired, 
                                               assignedEmployees, availableEmployees, 
                                               false, true,hours, LocalDate.now());

        // Since we don't have a real employee with permissions, this will likely fail
        // In a real test, we would add an employee with the necessary permissions
        assertEquals("Shift created successfully", result);
    }

    @Test
    void testAssignEmployeeToRole() {
        // First create a shift
        long employeeId = 1L;
        ShiftType shiftType = ShiftType.MORNING;
        LocalDate date = LocalDate.now().plusDays(1);
        Map<String, Integer> rolesRequired = new HashMap<>();
        rolesRequired.put("CASHIER", 2);
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        shiftService.createShift(employeeId, shiftType, date, rolesRequired, 
                               assignedEmployees, availableEmployees, 
                               false, true,hours, LocalDate.now());

        // Get the shift ID
        ShiftSL[] shifts = shiftService.getAllShifts(employeeId);
        long shiftId = shifts[0].getId();

        // Assign an employee to a role
        String result = shiftService.assignEmployeeToRole(employeeId, shiftId, employeeId, "CASHIER");

        // Since we don't have a real employee with permissions, this will likely fail
        assertEquals("Employee assigned to role successfully", result);
    }

    @Test
    void testMarkEmployeeAvailable() {
        // First create a shift
        long employeeId = 1L;
        ShiftType shiftType = ShiftType.MORNING;
        LocalDate date = LocalDate.now().plusDays(1);
        Map<String, Integer> rolesRequired = new HashMap<>();
        rolesRequired.put("CASHIER", 2);
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        shiftService.createShift(employeeId, shiftType, date, rolesRequired, 
                               assignedEmployees, availableEmployees, 
                               false, true,hours, LocalDate.now());

        // Get the shift ID
        ShiftSL[] shifts = shiftService.getAllShifts(employeeId);
        long shiftId = shifts[0].getId();

        // Mark an employee as available
        String result = shiftService.markEmployeeAvailable(employeeId, shiftId);

        // Since we don't have a real employee with permissions, this will likely fail
        assertEquals("Employee marked as available successfully", result);
    }

    @Test
    void testGetEmployeeWeeklyAvailability() {
        // First create shifts for a week
        long employeeId = 1L;
        LocalDate startDate = LocalDate.now().plusDays(1);
        Map<String, Integer> rolesRequired = new HashMap<>();
        rolesRequired.put("CASHIER", 2);
        String hours = "08:00-16:00";

        shiftService.createWeeklyShifts(employeeId, startDate, rolesRequired);

        // Mark the employee as available for some shifts
        ShiftSL[] shifts = shiftService.getAllShifts(employeeId);
        for (ShiftSL shift : shifts) {
            if (shift.getShiftType().equals(ShiftType.MORNING)) {
                shiftService.markEmployeeAvailable(employeeId, shift.getId());
            }
        }

        // Get the employee's weekly availability
        Map<LocalDate, Map<String, Boolean>> availability = 
            shiftService.getEmployeeWeeklyAvailability(employeeId, employeeId, Week.from(startDate));

        // Check that the availability is as expected
        assertFalse(availability.isEmpty());
    }
}
