package DomainLayer;

import DomainLayer.enums.ShiftType;


import java.time.DayOfWeek;
import java.util.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class ShiftController {
    private final Set<Shift> shifts;
    private final AuthorisationController authorizationController;
    private long shiftIdCounter = 0;

    public ShiftController(Set<Shift> shifts, AuthorisationController authorizationController) {
        this.shifts = shifts;
        this.authorizationController = authorizationController;
    }

    /**
     * add a shift to the system
     *
     * @param shiftType              morning or evening
     * @param date                   date of the shift
     * @param rolesRequired          number of employees of each role required for the shift
     * @param assignedEmployees      employees assigned to the shift
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @param isOpen                 true if the place is open
     * @param updateDate             the date of the last update
     * @return true if the shift was created successfully, false otherwise
     */
    public boolean createShift(String shiftType, LocalDate date, Map<String, Integer> rolesRequired, Map<String, Set<Employee>> assignedEmployees, Set<Employee> availableEmployees, boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        String PERMISSION_REQUIRED = "CREATE_SHIFT";
        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(shiftType))) {
            throw new RuntimeException("Shift already exists");
        }
        if (shiftType == null || date == null || rolesRequired == null || assignedEmployees == null) {
            throw new IllegalArgumentException("Shift type, date, roles required, and assigned employees cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        Shift newShift = new Shift(shiftIdCounter, shiftType, date, rolesRequired, assignedEmployees, availableEmployees, isAssignedShiftManager, isOpen, updateDate);
        shiftIdCounter++;
        return shifts.add(newShift);
    }

    /**
     * add shifts for the next week to the system
     *
     * @param StartDate              date of the shift
     * @param rolesRequired     number of employees of each role required for the shift
     * @return true if the shift was created successfully, false otherwise
     */
    public boolean createWeeklyShifts(LocalDate StartDate, Map<String, Integer> rolesRequired) {
        String PERMISSION_REQUIRED = "CREATE_SHIFT";
        if (StartDate == null || rolesRequired == null) {
            throw new IllegalArgumentException("Start date and roles required cannot be null");
        }
        if (StartDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (rolesRequired.isEmpty()) {
            throw new IllegalArgumentException("Roles required cannot be empty");
        }
        Shift newShift = null;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                Set<Employee> availableEmployees = new HashSet<>();   //TODO check if this the right way to implement
                LocalDate date = StartDate.plusDays(i);
                if (j == 0) {
                    if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY)) {
                        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(ShiftType.MORNING))) {
                            throw new RuntimeException("Shift already exists");
                        }
                        Map<String, Set<Employee>> assignedEmployees = new HashMap<>();
                        newShift = new Shift(shiftIdCounter, "MORNING", date, rolesRequired, assignedEmployees, availableEmployees, false, false, LocalDate.now());
                    }
                } else {
                    if (!(date.getDayOfWeek() == DayOfWeek.FRIDAY)) {
                        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(ShiftType.EVENING))) {
                            throw new RuntimeException("Shift already exists");
                        }
                        Map<String, Set<Employee>> assignedEmployees = new HashMap<>();
                        newShift = new Shift(shiftIdCounter, "EVENING", date, rolesRequired, assignedEmployees, availableEmployees, false, false, LocalDate.now());
                    }
                }
                shiftIdCounter++;
                boolean added = shifts.add(newShift);
                if (!added) return false;
            }
        }
        return true;
    }

    /**
     * remove a shift from the system
     *
     * @param shiftId id of the shift to remove
     * @return true if the shift was removed successfully, false otherwise
     */
    public boolean removeShift(long shiftId) {
        String PERMISSION_REQUIRED = "REMOVE_SHIFT";
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToRemove = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToRemove == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shifts.remove(shiftToRemove);
        return true;
    }

    /**
     * update a shift in the system
     *
     * @param shiftId                id of the shift to update
     * @param shiftType              morning or evening
     * @param date                   date of the shift
     * @param rolesRequired          number of employees of each role required for the shift
     * @param assignedEmployees      employees assigned to the shift
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @param isOpen                 true if the place is open
     * @param updateDate             the date of the last update
     * @return true if the shift was updated successfully, false otherwise
     */
    public boolean updateShift(long shiftId, String shiftType, LocalDate date, Map<String, Integer> rolesRequired, Map<String, Set<Employee>> assignedEmployees, boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (shiftType == null || date == null || rolesRequired == null || assignedEmployees == null) {
            throw new IllegalArgumentException("Shift type, date, roles required, and assigned employees cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        if (rolesRequired.isEmpty()) {
            throw new IllegalArgumentException("Roles required cannot be empty");
        }
        if (assignedEmployees.isEmpty()) {
            throw new IllegalArgumentException("Assigned employees cannot be empty");
        }
        if (updateDate == null) {
            throw new IllegalArgumentException("Update date cannot be null");
        }
        if (updateDate.isBefore(date)) {
            throw new IllegalArgumentException("Update date cannot be before the shift date");
        }
        if (updateDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Update date cannot be in the future");
        }
        if (isAssignedShiftManager && !assignedEmployees.containsKey("Shift Manager")) {
            throw new IllegalArgumentException("Shift manager must be assigned if isAssignedShiftManager is true");
        }
        if (isOpen && assignedEmployees.isEmpty()) {
            throw new IllegalArgumentException("At least one employee must be assigned if isOpen is true");
        }
        if (isOpen && rolesRequired.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be required if isOpen is true");
        }
        if (isAssignedShiftManager && rolesRequired.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be required if isAssignedShiftManager is true");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setShiftType(shiftType);
        shiftToUpdate.setShiftDate(date);
        shiftToUpdate.setRolesRequired(rolesRequired);
        shiftToUpdate.setAssignedEmployees(assignedEmployees);
        shiftToUpdate.setAssignedShiftManager(isAssignedShiftManager);
        shiftToUpdate.setOpen(isOpen);
        shiftToUpdate.setUpdateDate(updateDate);
        return true;
    }

    /**
     * get a shift from the system
     *
     * @param shiftId id of the shift to get
     * @return the shift if it exists, null otherwise
     */
    public Shift getShiftByID(long shiftId) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        return shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
    }

    /**
     * get all shifts from the system
     *
     * @return all shifts
     */
    public Set<Shift> getAllShifts() {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (shifts.isEmpty()) {
            throw new RuntimeException("No shifts found");
        }
        return shifts;
    }

    /**
     * get all shifts for a specific date
     *
     * @param date date of the shifts
     * @return all shifts for the date
     */
    public Set<Shift> getShiftsByDate(LocalDate date) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return shifts.stream().filter(shift -> shift.getShiftDate().equals(date)).collect(Collectors.toSet());
    }

    /**
     * get all shifts for a specific employee
     *
     * @param employee employee to get shifts for
     * @return all shifts for the employee
     */
    public Set<Shift> getShiftsByEmployee(Employee employee) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        return shifts.stream().filter(shift -> shift.getAssignedEmployees().values().stream().anyMatch(employees -> employees.contains(employee))).collect(Collectors.toSet());
    }

    /**
     * get shift by date and shift type
     *
     * @param date      date of the shift
     * @param shiftType morning or evening
     * @return the shift if it exists, null otherwise
     */
    public Shift getshift(LocalDate date, String shiftType) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (date == null || shiftType == null) {
            throw new IllegalArgumentException("Date and shift type cannot be null");
        }
        return shifts.stream().filter(shift -> shift.getShiftDate().equals(date) && shift.getShiftType().equals(shiftType)).findFirst().orElse(null);
    }

    /**
     * update the roles required for a shift
     *
     * @param shiftId       id of the shift to update
     * @param rolesRequired number of employees of each role required for the shift
     * @return true if the roles were updated successfully, false otherwise
     */
    public boolean updateRolesRequired(long shiftId, Map<String, Integer> rolesRequired) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (rolesRequired == null) {
            throw new IllegalArgumentException("Roles required cannot be null");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setRolesRequired(rolesRequired);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * update the assigned employees for a shift
     *
     * @param shiftId           id of the shift to update
     * @param assignedEmployees employees assigned to the shift
     * @return true if the assigned employees were updated successfully, false otherwise
     */
    public boolean updateAssignedEmployees(long shiftId, Map<String, Set<Employee>> assignedEmployees) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (assignedEmployees == null) {
            throw new IllegalArgumentException("Assigned employees cannot be null");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setAssignedEmployees(assignedEmployees);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * update the shift manager for a shift
     *
     * @param shiftId                id of the shift to update
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @return true if the shift manager was updated successfully, false otherwise
     */
    public boolean updateShiftManager(long shiftId, boolean isAssignedShiftManager) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setAssignedShiftManager(isAssignedShiftManager);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * update the open status for a shift
     *
     * @param shiftId id of the shift to update
     * @param isOpen  true if the place is open, false otherwise
     * @return true if the open status was updated successfully, false otherwise
     */
    public boolean updateOpenStatus(long shiftId, boolean isOpen) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setOpen(isOpen);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * update the available employees set
     *
     * @param shiftId   id of the shift to update
     * @param employees set of employees to update
     * @return true id the set was updated successfully
     */
    public boolean updateShiftAvailableEmployees(long shiftId, Set<Employee> employees) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (employees == null) {
            throw new IllegalArgumentException("Employees set cannot be null");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        shiftToUpdate.setAvailableEmployees(employees);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * add available employee to the set of the available employees
     *
     * @param shiftId  the id of the relevant shift
     * @param employee the employee to add
     * @return true if added successfully
     */
    public boolean addAvailableEmployee(long shiftId, Employee employee) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (shiftToUpdate.getAssignedEmployees().values().stream().anyMatch(employees -> employees.contains(employee))) {
            throw new IllegalArgumentException("Employee is already assigned to a role in the shift");
        }
        if (shiftToUpdate.getAvailableEmployees() != null && shiftToUpdate.getAvailableEmployees().contains(employee)) {
            throw new IllegalArgumentException("Employee already exists in available employees");
        }
        if (shiftToUpdate.getRolesRequired().values().stream().anyMatch(role -> role <= shiftToUpdate.getAssignedEmployees().values().stream().flatMap(Set::stream).count())) {
            throw new IllegalArgumentException("No more employees required for this role in the shift");
        }
        if (shiftToUpdate.getAvailableEmployees() == null) {
            throw new IllegalArgumentException("Available employees set is null");
        }
        if (shiftToUpdate.getAvailableEmployees().contains(employee)) {
            throw new IllegalArgumentException("Employee already exists in available employees");
        }
        Set<Employee> AvailableEmployees = shiftToUpdate.getAvailableEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return AvailableEmployees.add(employee);
    }

    /**
     * remove available employee to the set of the available employees
     *
     * @param shiftId  the id of the relevant shift
     * @param employee the employee to add
     * @return true if removed successfully
     */
    public boolean removeAvailableEmployee(long shiftId, Employee employee) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (!shiftToUpdate.getAvailableEmployees().contains(employee)) {
            throw new IllegalArgumentException("Employee not found in available employees");
        }
        if (shiftToUpdate.getAssignedEmployees().values().stream().anyMatch(employees -> employees.contains(employee))) {
            throw new IllegalArgumentException("Employee is already assigned to a role in the shift");
        }
        Set<Employee> AvailableEmployees = shiftToUpdate.getAvailableEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return AvailableEmployees.remove(employee);
    }

    /**
     * Assigns an employee to a specific role in a shift.
     *
     * @param shiftId  the ID of the shift
     * @param role     the role to assign the employee to
     * @param employee the employee to assign
     * @return true if the employee was added (i.e., was not already assigned), false otherwise
     * @throws RuntimeException if the shift does not exist
     */
    public boolean addAssignedEmployee(long shiftId, String role, Employee employee) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (!shiftToUpdate.getRolesRequired().containsKey(role)) {
            throw new IllegalArgumentException("Role not found in shift");
        }
        if (shiftToUpdate.getAssignedEmployees().get(role) != null && shiftToUpdate.getAssignedEmployees().get(role).contains(employee)) {
            throw new IllegalArgumentException("Employee already assigned to this role in the shift");
        }
        if (shiftToUpdate.getAvailableEmployees() != null && !(shiftToUpdate.getAvailableEmployees().contains(employee))) {
            throw new IllegalArgumentException("Employee is not available for this shift");
        }
        if (shiftToUpdate.getRolesRequired().get(role) <= shiftToUpdate.getAssignedEmployees().get(role).size()) {
            throw new IllegalArgumentException("No more employees required for this role in the shift");
        }
        Map<String, Set<Employee>> availableEmployees = shiftToUpdate.getAssignedEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return availableEmployees.computeIfAbsent(role, k -> new HashSet<>()).add(employee);
    }

    /**
     * Removes an assigned employee from a specific role in a shift.
     *
     * @param shiftId  the ID of the shift
     * @param role     the role to remove the employee from
     * @param employee the employee to remove
     * @return true if the employee was removed, false if they were not assigned
     * @throws RuntimeException if the shift does not exist
     */
    public boolean removeAssignedEmployee(long shiftId, String role, Employee employee) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (!shiftToUpdate.getRolesRequired().containsKey(role)) {
            throw new IllegalArgumentException("Role not found in shift");
        }
        if (!shiftToUpdate.getAssignedEmployees().get(role).contains(employee)) {
            throw new IllegalArgumentException("Employee not assigned to this role in the shift");
        }
        Map<String, Set<Employee>> availableEmployees = shiftToUpdate.getAssignedEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return availableEmployees.computeIfAbsent(role, k -> new HashSet<>()).remove(employee);
    }

    /**
     * Adds a required number of employees for a specific role in a shift.
     * Does not overwrite if the role is already present.
     *
     * @param shiftId      the ID of the shift
     * @param role         the role to add the requirement for
     * @param roleRequired the number of employees required for the role
     * @return true if the role was added (was not already defined), false otherwise
     * @throws RuntimeException if the shift does not exist
     */
    public boolean addRoleRequired(long shiftId, String role, Integer roleRequired) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (shiftToUpdate == null) {
            throw new IllegalArgumentException("Shift not found");
        }
        if (shiftToUpdate.getRolesRequired().containsKey(role)) {
            throw new IllegalArgumentException("Role already exists in the shift");
        }
        Map<String, Integer> requiredRoles = shiftToUpdate.getRolesRequired();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return requiredRoles.putIfAbsent(role, roleRequired) == null;
    }

    /**
     * Removes a role requirement from a shift.
     *
     * @param shiftId the ID of the shift
     * @param role    the role to remove
     * @return true if the role was removed, false if it was not present
     * @throws RuntimeException if the shift does not exist
     */
    public boolean removeRoleRequired(long shiftId, String role) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (!shiftToUpdate.getRolesRequired().containsKey(role)) {
            throw new IllegalArgumentException("Role not found in the shift");
        }
        if (shiftToUpdate.getAssignedEmployees().containsKey(role) && !shiftToUpdate.getAssignedEmployees().get(role).isEmpty()) {
            throw new IllegalArgumentException("Cannot remove role that has assigned employees");
        }
        Map<String, Integer> requiredRoles = shiftToUpdate.getRolesRequired();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return requiredRoles.remove(role) != null;
    }


}


