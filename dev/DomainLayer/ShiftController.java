package DomainLayer;

import DomainLayer.enums.ShiftType;
import DomainLayer.exception.UnauthorizedPermissionException;
import Util.Week;


import java.time.DayOfWeek;
import java.util.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class ShiftController {
    private final Set<Shift> shifts;
    private final Map<Week, Set<Shift>> weeklyShifts = new HashMap<>();
    private final AuthorisationController authorizationController;
    private final EmployeeController empCon;
    private long shiftIdCounter = 0;

    public ShiftController(Set<Shift> shifts, AuthorisationController authorizationController, EmployeeController employeeController) {
        this.shifts = shifts;
        this.authorizationController = authorizationController;
        this.empCon = employeeController;
    }

    /**
     * add a shift to the system
     * @param doneBy                 employee who is creating the shift
     * @param shiftType              morning or evening
     * @param date                   date of the shift
     * @param rolesRequired          number of employees of each role required for the shift
     * @param assignedEmployees      employees assigned to the shift
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @param isOpen                 true if the place is open
     * @param updateDate             the date of the last update
     * @return true if the shift was created successfully, false otherwise
     */
    public boolean createShift(long doneBy,ShiftType shiftType, LocalDate date, Map<String, Integer> rolesRequired, Map<String, Set<Long>> assignedEmployees, Set<Long> availableEmployees, boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        String PERMISSION_REQUIRED = "CREATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to create shift");
        }
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
        Week week = Week.from(date);
        weeklyShifts.computeIfAbsent(week, k -> new HashSet<>()).add(newShift);
        return shifts.add(newShift);
    }



    /**
     * add shifts for the next week to the system
     * @param doneBy                 employee who is creating the shift
     * @param StartDate              date of the shift
     * @param rolesRequired     number of employees of each role required for the shift
     * @return true if the shift was created successfully, false otherwise
     */
    public boolean createWeeklyShifts(long doneBy, LocalDate StartDate, Map<String, Integer> rolesRequired) {
        String PERMISSION_REQUIRED = "CREATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to create weekly shifts");
        }
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
                Set<Long> availableEmployees = new HashSet<>();   //TODO check if this the right way to implement
                LocalDate date = StartDate.plusDays(i);
                if (j == 0) {
                    if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY)) {
                        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(ShiftType.MORNING))) {
                            throw new RuntimeException("Shift already exists");
                        }
                        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
                        newShift = new Shift(shiftIdCounter, ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, false, LocalDate.now());
                    }
                } else {
                    if (!(date.getDayOfWeek() == DayOfWeek.FRIDAY)) {
                        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(ShiftType.EVENING))) {
                            throw new RuntimeException("Shift already exists");
                        }
                        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
                        newShift = new Shift(shiftIdCounter, ShiftType.EVENING, date, rolesRequired, assignedEmployees, availableEmployees, false, false, LocalDate.now());
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
     * @param doneBy   employee who is removing the shift
     * @param shiftId id of the shift to remove
     * @return true if the shift was removed successfully, false otherwise
     */
    public boolean removeShiftByID(long doneBy, long shiftId) {
        String PERMISSION_REQUIRED = "REMOVE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to remove shift");
        }
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

    public boolean removeShift(long doneBy, LocalDate date,ShiftType shiftType) {
        String PERMISSION_REQUIRED = "REMOVE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to delete shift");
        }
        Shift shiftToRemove = shifts.stream()
                .filter(shift -> shift.getShiftDate().equals(date) && shift.getShiftType().equals(shiftType))
                .findFirst()
                .orElse(null);
        if (shiftToRemove == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shifts.remove(shiftToRemove);
        return true;
    }

    /**
     * update a shift in the system
     * @param doneBy                 employee who is updating the shift
     * @param shiftId                id of the shift to update
     * @param shiftType              morning or evening
     * @param date                   date of the shift
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @param isOpen                 true if the place is open
     * @param updateDate             the date of the last update
     * @return true if the shift was updated successfully, false otherwise
     */
    public boolean updateShift(long doneBy, long shiftId,ShiftType shiftType, LocalDate date, boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shift");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (shiftType == null || date == null ) {
            throw new IllegalArgumentException("Shift type, date cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
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
        if (isAssignedShiftManager) {
            throw new IllegalArgumentException("Shift manager must be assigned if isAssignedShiftManager is true");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        shiftToUpdate.setShiftType(shiftType);
        shiftToUpdate.setShiftDate(date);
        shiftToUpdate.setAssignedShiftManager(isAssignedShiftManager);
        shiftToUpdate.setOpen(isOpen);
        shiftToUpdate.setUpdateDate(updateDate);
        return true;
    }

    /**
     * get a shift from the system
     * @param doneBy employee who is requesting the shift
     * @param shiftId id of the shift to get
     * @return the shift if it exists, null otherwise
     */
    public Shift getShiftByID(long doneBy, long shiftId) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shift");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        return shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
    }

    /**
     * get all shifts from the system
     * @param doneBy employee who is requesting the shifts
     * @return all shifts
     */
    public Set<Shift> getAllShifts(long doneBy) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get all shifts");
        }
        if (shifts.isEmpty()) {
            throw new RuntimeException("No shifts found");
        }
        return shifts;
    }

    /**
     * get all shifts for a specific date
     * @param doneBy employee who is requesting the shifts
     * @param date date of the shifts
     * @return all shifts for the date
     */
    public Set<Shift> getShiftsByDate(long doneBy, LocalDate date) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by date");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return shifts.stream().filter(shift -> shift.getShiftDate().equals(date)).collect(Collectors.toSet());
    }

    /**
     * get all shifts for a specific employee
     * @param doneBy employee who is requesting the shifts
     * @param employeeID employee to get shifts for
     * @return all shifts for the employee
     */
    public Set<Shift> getShiftsByEmployee(long doneBy, long employeeID) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by employee");
        }
        if (employeeID <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        return shifts.stream().filter(shift -> shift.getAssignedEmployees().values().stream().anyMatch(employees -> employees.contains(employeeID))).collect(Collectors.toSet());
    }

    /**
     * get shift by date and shift type
     * @param doneBy    employee who is requesting the shift
     * @param date      date of the shift
     * @param shiftType morning or evening
     * @return the shift if it exists, null otherwise
     */
    public Shift getshift(long doneBy, LocalDate date,ShiftType shiftType) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by date and type");
        }
        if (date == null || shiftType == null) {
            throw new IllegalArgumentException("Date and shift type cannot be null");
        }
        return shifts.stream().filter(shift -> shift.getShiftDate().equals(date) && shift.getShiftType().equals(shiftType)).findFirst().orElse(null);
    }

    /**
     * update the roles required for a shift
     * @param doneBy        employee who is updating the shift
     * @param shiftId       id of the shift to update
     * @param rolesRequired number of employees of each role required for the shift
     * @return true if the roles were updated successfully, false otherwise
     */
    public boolean updateRolesRequired(long doneBy, long shiftId, String role, Integer rolesRequired) {
        String PERMISSION_REQUIRED = "ROLES_REQUIRED";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shifts (roles required)");
        }
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
        Map <String, Integer> rolesRequiredMap = shiftToUpdate.getRolesRequired();
        rolesRequiredMap.put(role, rolesRequired);
        shiftToUpdate.setRolesRequired(rolesRequiredMap);
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * update the assigned employees for a shift
     * @param doneBy             employee who is updating the shift
     * @param shiftId           id of the shift to update
     * @param assignedEmployees employees assigned to the shift
     * @return true if the assigned employees were updated successfully, false otherwise
     */
    public boolean updateAssignedEmployees(long doneBy, long shiftId, Map<String, Set<Long>> assignedEmployees) {
        String PERMISSION_REQUIRED = "ASSIGN_EMPLOYEE";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shifts (assigned employees)");
        }
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
     * @param doneBy                employee who is updating the shift
     * @param shiftId                id of the shift to update
     * @param isAssignedShiftManager true if the shift has a shift manager assigned, false otherwise
     * @return true if the shift manager was updated successfully, false otherwise
     */
    public boolean updateShiftManager(long doneBy, long shiftId, boolean isAssignedShiftManager) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shifts");
        }
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
     * @param doneBy employee who is updating the shift
     * @param shiftId id of the shift to update
     * @param isOpen  true if the place is open, false otherwise
     * @return true if the open status was updated successfully, false otherwise
     */
    public boolean updateOpenStatus(long doneBy, long shiftId, boolean isOpen) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update shifts");
        }
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
     * @param doneBy     employee who is updating the shift
     * @param shiftId   id of the shift to update
     * @param employees set of employees to update
     * @return true id the set was updated successfully
     */
    public boolean updateShiftAvailableEmployees(long doneBy, long shiftId, Set<Long> employees) {
        String PERMISSION_REQUIRED = "AVAILABLE_EMPLOYEES";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update available employees");
        }
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
     * @param doneBy   employee who is adding the shift
     * @param shiftId  the id of the relevant shift
     * @param employeeID the employee to add
     * @return true if added successfully
     */
    public boolean addAvailableEmployee(long doneBy, long shiftId, Long employeeID) {
        String PERMISSION_REQUIRED = "AVAILABLE_EMPLOYEE";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to add available employees");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (employeeID <= 0) {
            throw new IllegalArgumentException("EmployeeID must be a positive number");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (shiftToUpdate.getAssignedEmployees().values().stream().anyMatch(employees -> employees.contains(employeeID))) {
            throw new IllegalArgumentException("Employee is already assigned to a role in the shift");
        }
        if (shiftToUpdate.getAvailableEmployees() != null && shiftToUpdate.getAvailableEmployees().contains(employeeID)) {
            throw new IllegalArgumentException("Employee already exists in available employees");
        }
        if (shiftToUpdate.getRolesRequired().values().stream().anyMatch(role -> role <= shiftToUpdate.getAssignedEmployees().values().stream().flatMap(Set::stream).count())) {
            throw new IllegalArgumentException("No more employees required for this role in the shift");
        }
        if (shiftToUpdate.getAvailableEmployees() == null) {
            throw new IllegalArgumentException("Available employees set is null");
        }
        if (shiftToUpdate.getAvailableEmployees().contains(employeeID)) {
            throw new IllegalArgumentException("Employee already exists in available employees");
        }
        Set<Long> AvailableEmployees = shiftToUpdate.getAvailableEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return AvailableEmployees.add(employeeID);
    }

    /**
     * remove available employee to the set of the available employees
     * @param doneBy   employee who is adding the shift
     * @param shiftId  the id of the relevant shift
     * @param employeeID the employee to add
     * @return true if removed successfully
     */
    public boolean removeAvailableEmployee(long doneBy, long shiftId, Long employeeID) {
        String PERMISSION_REQUIRED = "AVAILABLE_EMPLOYEE";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to remove available employees");
        }
        Shift shiftToUpdate = shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (employeeID == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (shiftId <= 0) {
            throw new IllegalArgumentException("Shift ID must be a positive number");
        }
        if (!shiftToUpdate.getAvailableEmployees().contains(employeeID)) {
            throw new IllegalArgumentException("Employee not found in available employees");
        }
        if (shiftToUpdate.getAssignedEmployees().values().stream().anyMatch(employees -> employees.contains(employeeID))) {
            throw new IllegalArgumentException("Employee is already assigned to a role in the shift");
        }
        Set<Long> AvailableEmployees = shiftToUpdate.getAvailableEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return AvailableEmployees.remove(employeeID);
    }

    /**
     * Assigns an employee to a specific role in a shift.
     * @param doneBy   employee who is assigning the shift
     * @param shiftId  the ID of the shift
     * @param role     the role to assign the employee to
     * @param employeeID the employee to assign
     * @return true if the employee was added (i.e., was not already assigned), false otherwise
     * @throws RuntimeException if the shift does not exist
     */
    public boolean addAssignedEmployee(long doneBy, long shiftId, String role, Long employeeID) {
        String PERMISSION_REQUIRED = "ASSIGN_EMPLOYEE";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to assign employees to shifts");
        }
        if (employeeID == null) {
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
        if (shiftToUpdate.getAssignedEmployees().get(role) != null && shiftToUpdate.getAssignedEmployees().get(role).contains(employeeID)) {
            throw new IllegalArgumentException("Employee already assigned to this role in the shift");
        }
        if (shiftToUpdate.getAvailableEmployees() != null && !(shiftToUpdate.getAvailableEmployees().contains(employeeID))) {
            throw new IllegalArgumentException("Employee is not available for this shift");
        }
        if (shiftToUpdate.getRolesRequired().get(role) <= shiftToUpdate.getAssignedEmployees().get(role).size()) {
            throw new IllegalArgumentException("No more employees required for this role in the shift");
        }
        Map<String, Set<Long>> availableEmployees = shiftToUpdate.getAssignedEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return availableEmployees.computeIfAbsent(role, k -> new HashSet<>()).add(employeeID);
    }

    /**
     * Removes an assigned employee from a specific role in a shift.
     * @param doneBy   employee who is removing the shift
     * @param shiftId  the ID of the shift
     * @param role     the role to remove the employee from
     * @param employeeID the employee to remove
     * @return true if the employee was removed, false if they were not assigned
     * @throws RuntimeException if the shift does not exist
     */
    public boolean removeAssignedEmployee(long doneBy, long shiftId, String role, Long employeeID) {
        String PERMISSION_REQUIRED = "ASSIGN_EMPLOYEE";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to remove employees from shifts");
        }
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }
        if (employeeID == null) {
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
        if (!shiftToUpdate.getAssignedEmployees().get(role).contains(employeeID)) {
            throw new IllegalArgumentException("Employee not assigned to this role in the shift");
        }
        Map<String, Set<Long>> availableEmployees = shiftToUpdate.getAssignedEmployees();
        shiftToUpdate.setUpdateDate(LocalDate.now());
        return availableEmployees.computeIfAbsent(role, k -> new HashSet<>()).remove(employeeID);
    }

    /**
     * Adds a required number of employees for a specific role in a shift.
     * Does not overwrite if the role is already present.
     * @param doneBy       employee who is adding the shift
     * @param shiftId      the ID of the shift
     * @param role         the role to add the requirement for
     * @param roleRequired the number of employees required for the role
     * @return true if the role was added (was not already defined), false otherwise
     * @throws RuntimeException if the shift does not exist
     */
    public boolean addRoleRequired(long doneBy, long shiftId, String role, Integer roleRequired) {
        String PERMISSION_REQUIRED = "ROLE_REQUIRED";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update Role required");
        }
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
     * @param doneBy   employee who is removing the shift
     * @param shiftId the ID of the shift
     * @param role    the role to remove
     * @return true if the role was removed, false if it was not present
     * @throws RuntimeException if the shift does not exist
     */
    public boolean removeRoleRequired(long doneBy, long shiftId, String role) {
        String PERMISSION_REQUIRED = "ROLE_REQUIRED";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update Role required");
        }
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

    public Set<String> getRoles(long doneBy) {
        String PERMISSION_REQUIRED = "GET_ROLES";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get all roles");
        }
        if (doneBy <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        return authorizationController.getAllRoles();
    }


    /**
     * get all shifts for a specific week
     * @param doneBy employee who is requesting the shifts
     * @param Date date of start of the week
     * @return all shifts for the week from the start date
     */
    public Set<Shift> getShiftsByWeek(long doneBy, LocalDate Date) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by week");
        }
        if (Date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (Date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        return shifts.stream()
                .filter(shift -> !shift.getShiftDate().isBefore(Date) && !shift.getShiftDate().isAfter(Date.plusDays(6)))
                .collect(Collectors.toSet());
    }

    public Set<Shift> getAvailabillityByEmployee (long doneBy, LocalDate Date) {
        String PERMISSION_REQUIRED = "GET_AVAILABILITY";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get availability by week");
        }
        if (Date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (Date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        return shifts.stream()
                .filter(shift -> shift.getAvailableEmployees() != null && shift.getAvailableEmployees().contains(doneBy))
                .collect(Collectors.toSet());
    }



}


