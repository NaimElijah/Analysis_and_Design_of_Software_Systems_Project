package DomainLayer;

import DTOs.ShiftDTO;
import DomainLayer.EmployeeSubModule.*;
import DomainLayer.enums.ShiftType;
import Util.config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    final String hoursString = "08:00-16:00";
    final LocalTime startHour = LocalTime.of(8, 0);
    final LocalTime endHour = LocalTime.of(16, 0);

    /**
     * Helper method to parse hours string in format "HH:MM-HH:MM" into start and end LocalTime objects
     */
    private LocalTime[] parseHoursString(String hoursString) {
        String[] parts = hoursString.split("-");
        LocalTime start = LocalTime.parse(parts[0].trim());
        LocalTime end = LocalTime.parse(parts[1].trim());
        return new LocalTime[] { start, end };
    }

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

        // Use the predefined startHour and endHour
        shift = new Shift(1, ShiftType.MORNING, LocalDate.of(2020, 10, 2), new HashMap<>(), new HashMap<>(), new HashSet<>(), false, true, startHour, endHour, LocalDate.now());
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
        // Add Shift Manager role with count 1
        rolesRequired.put(config.ROLE_SHIFT_MANAGER, 1);

        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        // Use the predefined startHour and endHour
        boolean created = shiftController.createShift(
            shira.getIsraeliId(), 
            ShiftType.MORNING, 
            date, 
            rolesRequired, 
            assignedEmployees, 
            availableEmployees, 
            false, 
            true, 
            startHour, 
            endHour, 
            LocalDate.now()
        );
        assertTrue(created, "Shift should be created successfully");
    }

    @Test
    void testCreateShiftWithDuplicate() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        // Add Shift Manager role with count 1
        rolesRequired.put(config.ROLE_SHIFT_MANAGER, 1);

        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        // Use the predefined startHour and endHour
        shiftController.createShift(
            shira.getIsraeliId(), 
            ShiftType.MORNING, 
            date, 
            rolesRequired, 
            assignedEmployees, 
            availableEmployees, 
            false, 
            true, 
            startHour, 
            endHour, 
            LocalDate.now()
        );

        assertThrows(RuntimeException.class, () -> {
            shiftController.createShift(
                shira.getIsraeliId(), 
                ShiftType.MORNING, 
                date, 
                rolesRequired, 
                assignedEmployees, 
                availableEmployees, 
                false, 
                true, 
                startHour, 
                endHour, 
                LocalDate.now()
            );
        }, "Shift already exists");
    }

    @Test
    void testRemoveShiftByIDSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        // Add Shift Manager role with count 1
        rolesRequired.put(config.ROLE_SHIFT_MANAGER, 1);

        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        // Use the predefined startHour and endHour
        shiftController.createShift(
            shira.getIsraeliId(), 
            ShiftType.MORNING, 
            date, 
            rolesRequired, 
            assignedEmployees, 
            availableEmployees, 
            false, 
            true, 
            startHour, 
            endHour, 
            LocalDate.now()
        );

        String serializedShifts = shiftController.getAllShifts(shira.getIsraeliId());
        List<Shift> shiftsList = shiftController.deserializeArrayShifts(serializedShifts);
        long shiftId = shiftsList.get(0).getId();

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
        // Add Shift Manager role with count 1
        rolesRequired.put(config.ROLE_SHIFT_MANAGER, 1);

        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        // Use the predefined startHour and endHour
        shiftController.createShift(
            shira.getIsraeliId(), 
            ShiftType.MORNING, 
            date, 
            rolesRequired, 
            assignedEmployees, 
            availableEmployees, 
            false, 
            true, 
            startHour, 
            endHour, 
            LocalDate.now()
        );

        // Get the serialized shifts and deserialize them
        String serializedShifts = shiftController.getAllShifts(shira.getIsraeliId());
        List<Shift> shiftsList = shiftController.deserializeArrayShifts(serializedShifts);
        long shiftId = shiftsList.get(0).getId();

        // Update the shift with the same startHour and endHour
        boolean updated = shiftController.updateShift(
            shira.getIsraeliId(), 
            shiftId, 
            ShiftType.EVENING, 
            date, 
            false, 
            false, 
            startHour, 
            endHour, 
            LocalDate.now()
        );
        assertTrue(updated, "Shift should be updated successfully");
    }

    @Test
    void testUpdateNonExistingShift() {
        assertThrows(RuntimeException.class, () -> {
            shiftController.updateShift(shira.getIsraeliId(), 999L, ShiftType.EVENING, LocalDate.now(), false, false, startHour, endHour, LocalDate.now());
        }, "Shift does not exist");
    }

    @Test
    void testGetShiftById() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<String, Integer> rolesRequired = new HashMap<>();
        // Add Shift Manager role with count 1
        rolesRequired.put(config.ROLE_SHIFT_MANAGER, 1);

        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        shiftController.createShift(
            shira.getIsraeliId(), 
            ShiftType.MORNING, 
            date, 
            rolesRequired, 
            assignedEmployees, 
            availableEmployees, 
            false, 
            true, 
            startHour, 
            endHour, 
            LocalDate.now()
        );

        String serializedShifts = shiftController.getAllShifts(shira.getIsraeliId());
        List<Shift> shiftsList = shiftController.deserializeArrayShifts(serializedShifts);
        long shiftId = shiftsList.get(0).getId();

        String serializedShift = shiftController.getShiftByID(shira.getIsraeliId(), shiftId);
        assertNotNull(serializedShift, "Serialized shift should not be null");

        // Deserialize the shift to verify its ID
        ShiftDTO shiftDTO = ShiftDTO.deserialize(serializedShift);
        assertEquals(shiftId, shiftDTO.getId(), "Shift ID should match");
    }

    @Test
    void testGetShiftByIdNotFound() {
        try {
            shiftController.getShiftByID(shira.getIsraeliId(), 999L);
            fail("Expected RuntimeException but no exception was thrown");
        } catch (RuntimeException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("not found") || e.getMessage().contains("does not exist"), 
                      "Exception message should indicate shift not found");
        }
    }
}

// === (Other tests follow same fix pattern — I will continue in the next message because of length) ===
