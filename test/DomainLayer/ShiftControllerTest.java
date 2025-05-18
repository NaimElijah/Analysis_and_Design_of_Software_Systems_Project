package DomainLayer;

import DomainLayer.EmployeeSubModule.*;
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
    final String CASHIER = "CASHIER";

    Set<String> permissions;
    Map<String, HashSet<String>> Strings;

    Employee shira;
    Employee cochava;
    Shift shift;

    final String hours = "08:00-16:00";

    @BeforeEach
    void setUp() {
        permissions = Set.of(
                PERMISSION_CREATE_SHIFT,
                PERMISSION_REMOVE_SHIFT, PERMISSION_UPDATE_SHIFT, PERMISSION_GET_SHIFT,
                PERMISSION_String_REQUIRED, PERMISSION_GET_StringS,
                PERMISSION_ASSIGN_EMPLOYEE, PERMISSION_UPDATE_AVAILABLE
        );
        Strings = Map.of(ADMIN, new HashSet<>(Set.of(
                        PERMISSION_CREATE_SHIFT, PERMISSION_REMOVE_SHIFT, PERMISSION_UPDATE_SHIFT, PERMISSION_GET_SHIFT,
                        PERMISSION_String_REQUIRED, PERMISSION_GET_StringS,
                        PERMISSION_ASSIGN_EMPLOYEE, PERMISSION_UPDATE_AVAILABLE)),
                CASHIER, new HashSet<>(Set.of(PERMISSION_UPDATE_AVAILABLE, PERMISSION_GET_SHIFT))
        );

        shira = new Employee(123456789, "Shira", "Shtinboch", 10000, null, Set.of(ADMIN), LocalDate.now().minusYears(5), true, LocalDate.now().minusYears(5), LocalDate.now());
        cochava = new Employee(123456788, "Cochava", "Shavit", 10000, null, Set.of(CASHIER), LocalDate.now().minusYears(5), true, LocalDate.now().minusYears(5), LocalDate.now());

        Set<Employee> employees = new HashSet<>();
        employees.add(shira);
        employees.add(cochava);

        String hours = "08:00-16:00";
        shift = new Shift(1, ShiftType.MORNING, LocalDate.of(2020, 10, 2), new HashMap<>(), new HashMap<>(), new HashSet<>(), false, true, hours, LocalDate.now());
        Set<Shift> shifts = new HashSet<>();
        shifts.add(shift);

        authorisationController = new AuthorisationController(Strings, permissions);
        employeeController = new EmployeeController(employees, authorisationController);
        shiftController = new ShiftController(shifts, authorisationController, employeeController);
        assignmentController = new AssignmentController(employeeController, shiftController);
        availabilityController = new AvailabilityController(employeeController, shiftController);


    }

    @Test
    void testCreateShiftSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        boolean created = shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, hours, LocalDate.now());
        assertTrue(created, "Shift should be created successfully");
    }

    @Test
    void testCreateShiftWithDuplicate() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, hours, LocalDate.now());

        assertThrows(RuntimeException.class, () -> {
            shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, hours, LocalDate.now());
        }, "Shift already exists");
    }

    @Test
    void testRemoveShiftByIDSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, hours, LocalDate.now());

        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        long shiftId = shifts.iterator().next().getId();

        boolean removed = shiftController.removeShiftByID(shira.getIsraeliId(), shiftId);
        assertTrue(removed, "Shift should be removed successfully");
    }

    @Test
    void testRemoveNonExistingShift() {
        assertThrows(RuntimeException.class, () -> {
            shiftController.removeShift(shira.getIsraeliId(), LocalDate.of(2000, 1, 1), ShiftType.MORNING);
        }, "Shift does not exist");
    }

    @Test
    void testUpdateShiftSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, hours, LocalDate.now());

        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        long shiftId = shifts.iterator().next().getId();

        boolean updated = shiftController.updateShift(shira.getIsraeliId(), shiftId, ShiftType.EVENING, date, false, false, hours, LocalDate.now());
        assertTrue(updated, "Shift should be updated successfully");
    }

    @Test
    void testUpdateNonExistingShift() {
        assertThrows(RuntimeException.class, () -> {
            shiftController.updateShift(shira.getIsraeliId(), 999L, ShiftType.EVENING, LocalDate.now(), false, false, hours, LocalDate.now());
        }, "Shift does not exist");
    }

    @Test
    void testGetShiftById() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String hours = "08:00-16:00";

        shiftController.createShift(shira.getIsraeliId(), ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, hours, LocalDate.now());

        Set<Shift> shifts = shiftController.getAllShifts(shira.getIsraeliId());
        long shiftId = shifts.iterator().next().getId();

        Shift shift = shiftController.getShiftByID(shira.getIsraeliId(), shiftId);
        assertNotNull(shift, "Shift should be returned");
        assertEquals(shiftId, shift.getId(), "Shift ID should match");
    }

    @Test
    void testGetShiftByIdNotFound() {
        Shift shift = shiftController.getShiftByID(shira.getIsraeliId(), 999L);
        assertNull(shift, "Shift should not be found");
    }
}

// === (Other tests follow same fix pattern — I will continue in the next message because of length) ===

