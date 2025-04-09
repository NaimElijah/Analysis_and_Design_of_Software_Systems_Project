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
    public boolean createShift(ShiftType shiftType, LocalDate date, Map<Role, Integer> rolesRequired, Map<Role, Set<Employee>> assignedEmployees, Set<Employee> availableEmployees, boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        String PERMISSION_REQUIRED = "CREATE_SHIFT";
        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(shiftType))) {
            throw new RuntimeException("Shift already exists");
        }
        Shift newShift = new Shift(shiftIdCounter, shiftType, date, rolesRequired, assignedEmployees, availableEmployees, isAssignedShiftManager, isOpen, updateDate);
        shiftIdCounter++;
        return shifts.add(newShift);
    }

    /**
     * add shifts for the next week to the system
     *
     * @param Date              date of the shift
     * @param rolesRequired     number of employees of each role required for the shift
     * @param assignedEmployees employees assigned to the shift
     * @return true if the shift was created successfully, false otherwise
     */
    public boolean createWeeklyShifts(LocalDate Date, Map<Role, Integer> rolesRequired, Map<Role, Set<Employee>> assignedEmployees) {
        String PERMISSION_REQUIRED = "CREATE_SHIFT";
        Shift newShift = null;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                Set<Employee> availableEmployees = new HashSet<>();   //TODO check if this the right way to implement
                LocalDate date = Date.plusDays(i);
                if (j == 0) {
                    if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY)) {
                        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(ShiftType.MORNING))) {
                            throw new RuntimeException("Shift already exists");
                        }
                        newShift = new Shift(shiftIdCounter, ShiftType.MORNING, date, rolesRequired, assignedEmployees, availableEmployees, false, false, LocalDate.now());
                    }
                } else {
                    if (!(date.getDayOfWeek() == DayOfWeek.FRIDAY)) {
                        if (shifts.stream().anyMatch(s -> s.getShiftDate().equals(date) && s.getShiftType().equals(ShiftType.EVENING))) {
                            throw new RuntimeException("Shift already exists");
                        }
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
     *
     * @param shiftId id of the shift to remove
     * @return true if the shift was removed successfully, false otherwise
     */
    public boolean removeShift(long shiftId) {
        String PERMISSION_REQUIRED = "REMOVE_SHIFT";
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
    public boolean updateShift(long shiftId, ShiftType shiftType, LocalDate date, Map<Role, Integer> rolesRequired, Map<Role, Set<Employee>> assignedEmployees, boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
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
        return shifts.stream().filter(shift -> shift.getId() == shiftId).findFirst().orElse(null);
    }

    /**
     * get all shifts from the system
     *
     * @return all shifts
     */
    public Set<Shift> getAllShifts() {
        String PERMISSION_REQUIRED = "GET_SHIFT";
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
        return shifts.stream().filter(shift -> shift.getAssignedEmployees().values().stream().anyMatch(employees -> employees.contains(employee))).collect(Collectors.toSet());
    }

    /**
     * get shift by date and shift type
     *
     * @param date      date of the shift
     * @param shiftType morning or evening
     * @return the shift if it exists, null otherwise
     */
    public Shift getshift(LocalDate date, ShiftType shiftType) {
        String PERMISSION_REQUIRED = "GET_SHIFT";
        return shifts.stream().filter(shift -> shift.getShiftDate().equals(date) && shift.getShiftType().equals(shiftType)).findFirst().orElse(null);
    }

    /**
     * update the roles required for a shift
     *
     * @param shiftId       id of the shift to update
     * @param rolesRequired number of employees of each role required for the shift
     * @return true if the roles were updated successfully, false otherwise
     */
    public boolean updateRolesRequired(long shiftId, Map<Role, Integer> rolesRequired) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
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
    public boolean updateAssignedEmployees(long shiftId, Map<Role, Set<Employee>> assignedEmployees) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
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
        Set<Employee> AvailableEmployees = shiftToUpdate.getAvailableEmployees();
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
        Set<Employee> AvailableEmployees = shiftToUpdate.getAvailableEmployees();
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
    public boolean addAssignedEmployee(long shiftId, Role role, Employee employee) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }

        Map<Role, Set<Employee>> availableEmployees = shiftToUpdate.getAssignedEmployees();
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
    public boolean removeAssignedEmployee(long shiftId, Role role, Employee employee) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }

        Map<Role, Set<Employee>> availableEmployees = shiftToUpdate.getAssignedEmployees();
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
    public boolean addRoleRequired(long shiftId, Role role, Integer roleRequired) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }

        Map<Role, Integer> requiredRoles = shiftToUpdate.getRolesRequired();
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
    public boolean removeRoleRequired(long shiftId, Role role) {
        String PERMISSION_REQUIRED = "UPDATE_SHIFT";
        Shift shiftToUpdate = shifts.stream()
                .filter(shift -> shift.getId() == shiftId)
                .findFirst()
                .orElse(null);

        if (shiftToUpdate == null) {
            throw new RuntimeException("Shift does not exist");
        }

        Map<Role, Integer> requiredRoles = shiftToUpdate.getRolesRequired();
        return requiredRoles.remove(role) != null;
    }


}


