package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShiftControllerTest {

    private AuthorisationController authorisationController;
    private ShiftController shiftController;

    final String CREATE_SHIFT = "CREATE_SHIFT";
    final String UPDATE_SHIFT = "UPDATE_SHIFT";
    Role admin;
    Role cashier;
    Role shiftManager;
    
    Set<String> permissions;
    Set<Role> roles;
    Employee shira;
    Employee cochava;

    @BeforeEach
    void setUp() {
//        admin = new Role(0,"ADMIN", Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE, UPDATE_EMPLOYEE));
//        cashier = new Role(1,"CASHIER", Set.of(CASHIER));
//
//        permissions = Set.of(CREATE_EMPLOYEE, ADD_EMPLOYEE, UPDATE_EMPLOYEE);
//        roles = Set.of(admin, cashier);
//
//        shira = new Employee(0,0,"Shira", "Shtinboch", 10000, null, Set.of(admin), LocalDate.now().minusYears(5),true, LocalDate.now().minusYears(5), LocalDate.now());
//        cochava = new Employee(1,1,"Cochava", "Shavit", 10000, null, Set.of(cashier), LocalDate.now().minusYears(5),true, LocalDate.now().minusYears(5), LocalDate.now());
//
//        authorisationController = new AuthorisationController(roles, permissions);
//        Set<Employee> employees = new HashSet<>();
//        employees.add(shira);
//        employees.add(cochava);
//        shiftController = new ShiftController(employees, authorisationController);
        mockAuthorizationController = new AuthorisationController(); // You may need to mock this if it has more complex behavior
        Set<Shift> shifts = new HashSet<>();
        shiftController = new ShiftController(shifts, mockAuthorizationController);
    }

    @Test
    void testCreateShiftSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<Role, Integer> rolesRequired = new HashMap<>();
        Map<Role, Set<Employee>> assignedEmployees = new HashMap<>();
        Set<Employee> availableEmployees = new HashSet<>();

        boolean created = shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        assertTrue(created, "Shift should be created successfully");
    }

    @Test
    void testCreateShiftWithDuplicate() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<Role, Integer> rolesRequired = new HashMap<>();
        Map<Role, Set<Employee>> assignedEmployees = new HashMap<>();
        Set<Employee> availableEmployees = new HashSet<>();

        // Create a shift first
        shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Attempt to create the same shift again
        assertThrows(RuntimeException.class, () -> {
            shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());
        }, "Shift already exists");
    }

    @Test
    void testRemoveShiftSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<Role, Integer> rolesRequired = new HashMap<>();
        Map<Role, Set<Employee>> assignedEmployees = new HashMap<>();
        Set<Employee> availableEmployees = new HashSet<>();

        shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Remove the shift
        Set<Shift> shifts = shiftController.getAllShifts();
        long shiftId = shifts.iterator().next().getId();

        boolean removed = shiftController.removeShift(shiftId);
        assertTrue(removed, "Shift should be removed successfully");
    }

    @Test
    void testRemoveNonExistingShift() {
        // Attempt to remove a non-existing shift
        assertThrows(RuntimeException.class, () -> {
            shiftController.removeShift(999L);
        }, "Shift does not exist");
    }

    @Test
    void testUpdateShiftSuccessfully() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<Role, Integer> rolesRequired = new HashMap<>();
        Map<Role, Set<Employee>> assignedEmployees = new HashMap<>();
        Set<Employee> availableEmployees = new HashSet<>();

        shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Get the shift and update it
        Set<Shift> shifts = shiftController.getAllShifts();
        long shiftId = shifts.iterator().next().getId();

        boolean updated = shiftController.updateShift(shiftId, ShiftType.EVENING, date, rolesRequired, assignedEmployees, false, false, LocalDate.now());
        assertTrue(updated, "Shift should be updated successfully");
    }

    @Test
    void testUpdateNonExistingShift() {
        // Attempt to update a non-existing shift
        assertThrows(RuntimeException.class, () -> {
            shiftController.updateShift(999L, ShiftType.EVENING, LocalDate.now(), new HashMap<>(), new HashMap<>(), false, false, LocalDate.now());
        }, "Shift does not exist");
    }

    @Test
    void testGetShiftById() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<Role, Integer> rolesRequired = new HashMap<>();
        Map<Role, Set<Employee>> assignedEmployees = new HashMap<>();
        Set<Employee> availableEmployees = new HashSet<>();

        shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        // Get the shift by ID
        Set<Shift> shifts = shiftController.getAllShifts();
        long shiftId = shifts.iterator().next().getId();

        Shift shift = shiftController.getShiftByID(shiftId);
        assertNotNull(shift, "Shift should be returned");
        assertEquals(shiftId, shift.getId(), "Shift ID should match");
    }

    @Test
    void testGetShiftByIdNotFound() {
        // Attempt to get a non-existing shift
        Shift shift = shiftController.getShiftByID(999L);
        assertNull(shift, "Shift should not be found");
    }

    @Test
    void testAddAvailableEmployee() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<Role, Integer> rolesRequired = new HashMap<>();
        Map<Role, Set<Employee>> assignedEmployees = new HashMap<>();
        Set<Employee> availableEmployees = new HashSet<>();

        shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        Set<Shift> shifts = shiftController.getAllShifts();
        long shiftId = shifts.iterator().next().getId();

        Employee employee = new Employee(); // You would need to create an Employee instance with necessary properties
        boolean added = shiftController.addAvailableEmployee(shiftId, employee);

        assertTrue(added, "Employee should be added to available employees");
    }

    @Test
    void testAddAssignedEmployee() {
        LocalDate date = LocalDate.of(2025, 4, 9);
        Map<Role, Integer> rolesRequired = new HashMap<>();
        Map<Role, Set<Employee>> assignedEmployees = new HashMap<>();
        Set<Employee> availableEmployees = new HashSet<>();

        shiftController.createShift(ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, true, LocalDate.now());

        Set<Shift> shifts = shiftController.getAllShifts();
        long shiftId = shifts.iterator().next().getId();

        Role role = new Role(); // You would need to create a Role instance
        Employee employee = new Employee(); // You would need to create an Employee instance

        boolean added = shiftController.addAssignedEmployee(shiftId, role, employee);
        assertTrue(added, "Employee should be assigned to the role in the shift");
    }