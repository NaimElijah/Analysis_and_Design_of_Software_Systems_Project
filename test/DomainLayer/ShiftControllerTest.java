package DomainLayer;

import DomainLayer.enums.ShiftType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShiftControllerTest {

    AuthorisationController authorisationController;
    ShiftController shiftController;
    EmployeeController employeeController;
    AssignmentController assignmentController;
    AvailabilityController availabilityController;

    final String PERMISSION_CREATE_SHIFT = "CREATE_SHIFT";
    final String PERMISSION_REMOVE_SHIFT = "REMOVE_SHIFT";
    final String PERMISSION_UPDATE_SHIFT = "UPDATE_SHIFT";
    final String PERMISSION_GET_SHIFT = "GET_SHIFT";
    final String PERMISSION_String_REQUIRED = "String_REQUIRED";
    final String PERMISSION_GET_StringS = "GET_StringS";
    final String PERMISSION_ASSIGN_EMPLOYEE = "ASSIGN_EMPLOYEE";
    final String PERMISSION_UPDATE_AVAILABLE = "UPDATE_AVAILABLE";

    final String ADMIN = "ADMIN";
    final String MANAGER = "MANAGER";
    final String CASHIER = "CASHIER";


    Set<String> permissions;
    Map<String, HashSet<String>> Strings;

    Employee shira;
    Employee cochava;

    Shift shift;


    @BeforeEach
    void setUp() {
        permissions = Set.of(
                PERMISSION_CREATE_SHIFT,
                PERMISSION_REMOVE_SHIFT, PERMISSION_UPDATE_SHIFT, PERMISSION_GET_SHIFT,
                PERMISSION_String_REQUIRED, PERMISSION_GET_StringS,
                PERMISSION_ASSIGN_EMPLOYEE, PERMISSION_UPDATE_AVAILABLE
        );
        Strings = Map.of(ADMIN, new HashSet<>(Set.of(PERMISSION_CREATE_SHIFT,
                        PERMISSION_REMOVE_SHIFT, PERMISSION_UPDATE_SHIFT, PERMISSION_GET_SHIFT,
                        PERMISSION_String_REQUIRED, PERMISSION_GET_StringS,
                        PERMISSION_ASSIGN_EMPLOYEE, PERMISSION_UPDATE_AVAILABLE)),
                CASHIER, new HashSet<>(Set.of(PERMISSION_UPDATE_AVAILABLE, PERMISSION_GET_SHIFT))
        );
        shira = new Employee(123456789, "Shira", "Shtinboch", 10000, null, Set.of(ADMIN), LocalDate.now().minusYears(5), true, LocalDate.now().minusYears(5), LocalDate.now());
        cochava = new Employee(123456788, "Cochava", "Shavit", 10000, null, Set.of(CASHIER), LocalDate.now().minusYears(5), true, LocalDate.now().minusYears(5), LocalDate.now());
        Set<Employee> employees = new HashSet<>();
        employees.add(shira);
        employees.add(cochava);

        shift = new Shift(1, ShiftType.MORNING, LocalDate.of(2020, 10, 2), new HashMap<>(), new HashMap<>(), new HashSet<>(), false, true, LocalDate.now());
        Set<Shift> shifts = new HashSet<>();
        shifts.add(shift);

        authorisationController = new AuthorisationController(Strings, permissions);
        employeeController = new EmployeeController(employees, authorisationController);
        assignmentController = new AssignmentController(employeeController);
        availabilityController = new AvailabilityController(employeeController);
        shiftController = new ShiftController(shifts, authorisationController, employeeController);
    }


    @Test
    void testCreateShiftSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        boolean created = shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        assertTrue(created, "Shift should be created successfully");
    }

    @Test
    void testCreateShiftWithDuplicate() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        // Create a shift first
        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Attempt to create the same shift again
        assertThrows(RuntimeException.class, () -> {
            shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());
        }, "Shift already exists");
    }

    @Test
    void testRemoveShiftByIDSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Remove the shift
        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        long shiftId = shifts.iterator().next().getId();

        boolean removed = shiftController.removeShiftByID(shira.getIsraeliId(), shiftId);
        assertTrue(removed, "Shift should be removed successfully");
    }

    @Test
    void testRemoveNonExistingShift() {
        // Attempt to remove a non-existing shift
        assertThrows(RuntimeException.class, () -> {
            shiftController.removeShift(shira.getIsraeliId(), LocalDate.of(2000, 1, 1), ShiftType.MORNING);
        }, "Shift does not exist");
    }

    @Test
    void testUpdateShiftSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Get the shift and update it
        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        long shiftId = shifts.iterator().next().getId();

        boolean updated = shiftController.updateShift(shira.getIsraeliId(), shiftId, ShiftType.EVENING, date, false, false, LocalDate.now());
        assertTrue(updated, "Shift should be updated successfully");
    }

    @Test
    void testUpdateNonExistingShift() {
        // Attempt to update a non-existing shift
        assertThrows(RuntimeException.class, () -> {
            shiftController.updateShift(shira.getIsraeliId(), 999L, ShiftType.EVENING, LocalDate.now(), false, false, LocalDate.now());
        }, "Shift does not exist");
    }

    @Test
    void testGetShiftById() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Get the shift by ID
        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        long shiftId = shifts.iterator().next().getId();

        Shift shift = shiftController.getShiftByID(shira.getIsraeliId(), shiftId);
        assertNotNull(shift, "Shift should be returned");
        assertEquals(shiftId, shift.getId(), "Shift ID should match");
    }

    @Test
    void testGetShiftByIdNotFound() {
        // Attempt to get a non-existing shift
        Shift shift = shiftController.getShiftByID(shira.getIsraeliId(), 999L);
        assertNull(shift, "Shift should not be found");
    }

    @Test
    void testAddAvailableEmployee() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());


        boolean added = availabilityController.markAvailable(shift, cochava.getIsraeliId());

        assertTrue(added, "Employee should be added to available employees");
    }

    @Test
    void testAddAvailableEmployeeNotFound() {
        // Attempt to add a non-existing employee
        assertThrows(RuntimeException.class, () -> {
            availabilityController.markAvailable(shift, 999L);
        }, "Employee does not exist");
    }

    @Test
    void testRemoveAvailableEmployee() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Add an employee to available employees
        availabilityController.markAvailable(shift, cochava.getIsraeliId());

        // Remove the employee
        boolean removed = availabilityController.removeAvailability(shift, cochava.getIsraeliId());
        assertTrue(removed, "Employee should be removed from available employees");
    }

    @Test
    void testRemoveNonExistingAvailableEmployee() {
        // Attempt to remove a non-existing employee
        assertThrows(RuntimeException.class, () -> {
            availabilityController.removeAvailability(shift, 999L);
        }, "Employee does not exist");
    }

    @Test
    void testIsAvailable() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Add an employee to available employees
        availabilityController.markAvailable(shift, cochava.getIsraeliId());

        // Check if the employee is available
        boolean isAvailable = availabilityController.isAvailable(shift, cochava.getIsraeliId());
        assertTrue(isAvailable, "Employee should be available");
    }

    @Test
    void testIsNotAvailable() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Check if a non-existing employee is available
        boolean isAvailable = availabilityController.isAvailable(shift, shira.getIsraeliId());
        assertFalse(isAvailable, "Employee should not be available");
    }

    @Test
    void testAddAssignedEmployee() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        StringsRequired.put("CASHIER", 1);

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        Shift shift = shiftController.getShiftByID(shira.getIsraeliId(), 1L);
        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        long shiftId = shifts.iterator().next().getId();

        boolean added = assignmentController.assignEmployeeToRole(shift, shira.getIsraeliId(), "CASHIER", cochava.getIsraeliId());
        assertTrue(added, "Employee should be assigned to the String in the shift");
    }

    @Test
    void testAddAssignedEmployeeNotFound() {
        // Attempt to add a non-existing employee
        assertThrows(RuntimeException.class, () -> {
            assignmentController.assignEmployeeToRole(shift, shira.getIsraeliId(), "CASHIER", 999L);
        }, "Employee does not exist");
    }

    @Test
    void testRemoveAssignedEmployee() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        StringsRequired.put("CASHIER", 1);

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        Shift shift = shiftController.getShiftByID(shira.getIsraeliId(), 1L);
        // Add an employee to assigned employees
        assignmentController.assignEmployeeToRole(shift, shira.getIsraeliId(), "CASHIER", cochava.getIsraeliId());

        // Remove the employee
        boolean removed = assignmentController.removeAssignment( shira.getIsraeliId(), shift, "CASHIER", cochava.getIsraeliId());
        assertTrue(removed, "Employee should be removed from the String in the shift");
    }

    @Test
    void testRemoveNonExistingAssignedEmployee() {
        // Attempt to remove a non-existing employee
        assertThrows(RuntimeException.class, () -> {
            assignmentController.removeAssignment(shira.getIsraeliId(),shift, "CASHIER", 999L);
        }, "Employee does not exist");
    }

    @Test
    void testIsAssigned() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        StringsRequired.put("CASHIER", 1);

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        Shift shift = shiftController.getShiftByID(shira.getIsraeliId(), 1L);
        // Add an employee to assigned employees
        assignmentController.assignEmployeeToRole(shift, shira.getIsraeliId(), "CASHIER", cochava.getIsraeliId());

        // Check if the employee is assigned
        boolean isAssigned = assignmentController.isAssigned(shira.getIsraeliId(), shift, cochava.getIsraeliId());
        assertTrue(isAssigned, "Employee should be assigned to the String in the shift");
    }

    @Test
    void testIsNotAssigned() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Check if a non-existing employee is assigned
        boolean isAssigned = assignmentController.isAssigned(shira.getIsraeliId(), shift, 999L);
        assertFalse(isAssigned, "Employee should not be assigned to the String in the shift");
    }

    @Test
    void testGetAllShifts() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Get all shifts
        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        assertFalse(shifts.isEmpty(), "Shifts should not be empty");
    }

    @Test
    void testGetAllShiftsByDate() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> StringsRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, StringsRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Get all shifts by date
        Set<Shift> shifts = shiftController.getAllShiftsByDate(shira.getIsraeliId(), date);
        assertFalse(shifts.isEmpty(), "Shifts should not be empty");
    }





}