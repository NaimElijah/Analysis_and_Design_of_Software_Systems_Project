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
        addShiftsToWeeklyShifts(shifts);
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
        boolean addedToWeekly = weeklyShifts.computeIfAbsent(week, k -> new HashSet<>()).add(newShift);
        boolean added = shifts.add(newShift);
        return addedToWeekly && added;
    }



    /**
     * Create shifts from the next Sunday after the given date for one full week (Sunday to Saturday).
     * @param doneBy employee who is creating the shifts
     * @param startDate the date to start from (shifts will begin from the next Sunday)
     * @param rolesRequired number of employees of each role required for the shifts
     * @return true if all shifts were created successfully, false otherwise
     */
    public boolean createWeeklyShifts(long doneBy, LocalDate startDate, Map<String, Integer> rolesRequired) {
        final String PERMISSION_REQUIRED = "CREATE_SHIFT";

        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to create weekly shifts");
        }

        if (startDate == null || rolesRequired == null) {
            throw new IllegalArgumentException("Start date and roles required cannot be null");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        if (rolesRequired.isEmpty()) {
            throw new IllegalArgumentException("Roles required cannot be empty");
        }

        // Move to the next Sunday if not already Sunday
        if (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            int daysUntilSunday = (DayOfWeek.SUNDAY.getValue() - startDate.getDayOfWeek().getValue() + 7) % 7;
            startDate = startDate.plusDays(daysUntilSunday);
        }

        for (int day = 0; day < 7; day++) {
            LocalDate date = startDate.plusDays(day);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            // Morning shift for all days except Saturday
            if (dayOfWeek != DayOfWeek.SATURDAY) {
                if (AddNewShift(date, ShiftType.MORNING, rolesRequired)) return false;
            }

            // Evening shift for Sunday to Thursday and Saturday (not Friday)
            if (dayOfWeek != DayOfWeek.FRIDAY && dayOfWeek != DayOfWeek.SATURDAY) {
                if (AddNewShift(date, ShiftType.EVENING, rolesRequired)) return false;
            }
        }

        return true;
    }

    /**
     * create and add
     * @param date the date of the shift
     * @param type the type of the shift (morning or evening)
     * @param rolesRequired number of employees of each role required for the shift
     * @return true if the shift was added successfully, false otherwise
     */
    private boolean AddNewShift(LocalDate date, ShiftType type, Map<String, Integer> rolesRequired) {
        // Check if a shift of this type already exists for the date
        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType() == type)) {
            throw new RuntimeException(type + " shift already exists for " + date);
        }

        Set<Long> availableEmployees = new HashSet<>();
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        boolean isOpen = (type == ShiftType.MORNING && date.getDayOfWeek() != DayOfWeek.SATURDAY)
                || (type == ShiftType.EVENING && date.getDayOfWeek() != DayOfWeek.FRIDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY);

        Shift shift = new Shift(shiftIdCounter++, type, date, rolesRequired, assignedEmployees, availableEmployees, false, isOpen, LocalDate.now());

        boolean added = shifts.add(shift);
        if (!added) return true;

        Week week = Week.from(date);
        weeklyShifts.computeIfAbsent(week, k -> new HashSet<>()).add(shift);

        return false;
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
    public Set<Shift> getAllShiftsByDate(long doneBy, LocalDate date) {
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
        String PERMISSION_REQUIRED = "ROLE_REQUIRED";
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


    public Set<Shift> getShiftsByWeek(long doneBy,Week week) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        if (!empCon.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to get shifts by week");
        }
        return weeklyShifts.get(week);
    }

    public void addShiftsToWeeklyShifts(Set<Shift> shifts) {
        if (shifts == null || shifts.isEmpty()) {
            throw new IllegalArgumentException("Shifts cannot be null or empty");
        }

        for (Shift shift : shifts) {
            if (shift == null) {
                throw new IllegalArgumentException("Shift cannot be null");
            }
            if (shift.getShiftDate() == null) {
                throw new IllegalArgumentException("Shift date cannot be null");
            }
            Week week = Week.from(shift.getShiftDate());
            weeklyShifts.computeIfAbsent(week, k -> new HashSet<>()).add(shift);
        }
    }
}
