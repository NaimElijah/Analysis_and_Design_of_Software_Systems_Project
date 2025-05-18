package DomainLayer.EmployeeSubModule;

import DomainLayer.exception.UnauthorizedPermissionException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AssignmentController {
    private final EmployeeController employeeController;
    private final ShiftController shiftController;

    public AssignmentController(EmployeeController employeeController, ShiftController shiftController) {
        this.employeeController = employeeController;
        this.shiftController = shiftController;
    }
    /**
     * Assigns an employee to a specified role within a designated shift.
     * Validates permissions for the action, checks if the role is available and not already full,
     * and updates the shift's assigned employees and roles accordingly.
     *
     * @param shiftId The ID of the shift where the employee is being assigned.
     * @param doneBy The ID of the user performing the assignment. This user must have the appropriate permissions.
     * @param role The role the employee should be assigned to within the shift.
     * @param employeeId The ID of the employee being assigned to the specified role.
     * @return true if the employee was successfully assigned to the role.
     * @throws UnauthorizedPermissionException if the user performing the action does not have permission to assign employees.
     * @throws IllegalArgumentException if the specified role does not exist in the shift or if the shift ID is invalid.
     * @throws IllegalStateException if the role is already full and cannot accommodate additional employees.
     */
    public boolean assignEmployeeToRole(long shiftId, long doneBy, String role, long employeeId) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        Shift shift = shiftController.getShiftByIdAsShift(doneBy, shiftId);
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        if (!shift.getRolesRequired().containsKey(role)){
            throw new IllegalArgumentException("Role does not exist in shift");
        }
        Map <String, Set<Long>> assignedEmployees = shift.getAssignedEmployees();
        if (!assignedEmployees.containsKey(role)) {
            assignedEmployees.put(role, new HashSet<>());
        }
        assignedEmployees.computeIfAbsent(role, k -> new HashSet<>());

        Set<Long> employeesInRole = assignedEmployees.get(role);
        int requiredCount = shift.getRolesRequired().get(role);

        if (employeesInRole.size() >= requiredCount) {
            throw new IllegalStateException("Role is already full");
        }
        if (Objects.equals(role, "SHIFT_MANAGER")) {
            shift.setAssignedShiftManager(true);
        }
        employeesInRole.add(employeeId);
        assignedEmployees.put(role, employeesInRole);
        shift.setAssignedEmployees(assignedEmployees);
        return true;
    }

    /**
     * Removes an employee from a specified role within a designated shift.
     * Validates permissions for the action and updates the shift's assigned employees accordingly.
     *
     * @param doneBy The ID of the user performing the action. This user must have the appropriate permissions.
     * @param shiftId The ID of the shift from which the employee is being removed.
     * @param role The role from which the employee should be removed within the shift.
     * @param employeeId The ID of the employee being removed from the specified role.
     * @return true if the employee was successfully removed from the role.
     * @throws UnauthorizedPermissionException if the user performing the action does not have the required permission.
     * @throws IllegalArgumentException if the specified shift ID or role is invalid.
     */
    public boolean removeAssignment(long doneBy, long shiftId, String role, long employeeId) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to remove employees");
        }
        Shift shift = shiftController.getShiftByIdAsShift(doneBy, shiftId);

        Map <String, Set<Long>> assignedEmployees = shift.getAssignedEmployees();
        Set<Long> employeesInRole = assignedEmployees.get(role);
        employeesInRole.remove(employeeId);
        assignedEmployees.put(role, employeesInRole);
        shift.setAssignedEmployees(assignedEmployees);
        return true;
    }

    /**
     * Checks whether a specific employee is assigned to any role within a given shift.
     * Verifies that the user performing the check has the required permission before retrieving and analyzing the shift data.
     *
     * @param doneBy The ID of the user initiating the check. This user must have the appropriate permissions.
     * @param shiftId The ID of the shift being checked for the employee's assignment.
     * @param employeeId The ID of the employee whose assignment status is being verified.
     * @return true if the employee is assigned to any role in the specified shift; false otherwise.
     * @throws UnauthorizedPermissionException if the user performing the check does not have permission to verify assignments.
     * @throws IllegalArgumentException if the specified shift ID is invalid or the shift cannot be retrieved.
     */
    public boolean isAssigned(long doneBy, long shiftId, long employeeId) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        Shift shift = shiftController.getShiftByIdAsShift(doneBy, shiftId);
        return shift.getAssignedEmployees().values().stream()
                .anyMatch(set -> set.contains(employeeId));
    }

    /**
     * Checks whether a Shift Manager is assigned to the specified shift.
     * Ensures the user performing the check is authorized to verify shift assignments.
     * Throws an exception if no Shift Manager is assigned to the provided shift.
     *
     * @param doneBy The ID of the user performing the check. This user must have the appropriate permissions.
     * @param shiftId The ID of the shift being checked for a Shift Manager assignment.
     * @return true if a Shift Manager is assigned to the specified shift.
     * @throws UnauthorizedPermissionException if the user performing the check does not have permission to verify shift assignments.
     * @throws RuntimeException if no Shift Manager is assigned to the specified shift.
     */
    public boolean isAssignedManager(long doneBy, long shiftId) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        Shift shift = shiftController.getShiftByIdAsShift(doneBy, shiftId);
        if (!shift.isAssignedShiftManager()){
            throw new RuntimeException("Shift Manager is not assigned to this shift");
        }
        return shift.isAssignedShiftManager();
    }

    public List<Set<Long>> getUnassignedEmployees(long doneBy, long shiftId){
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if(!employeeController.isEmployeeAuthorised(doneBy,PERMISSION)){
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        Shift shift = shiftController.getShiftByIdAsShift(doneBy, shiftId);
        List<Set<Long>> unAssignedEmployees = new ArrayList<>();
        Set<Long> employees = employeeController.getAllEmployees().keySet();
        employees.removeIf(employee -> isAssigned(doneBy, shiftId, employee)); // All the employees that are not assigned to the shi
        Set<Long> availableEmployees = shift.getAvailableEmployees();
        Set<Long> availableEmployeesAndUnassigned = new HashSet<>(availableEmployees);
        availableEmployeesAndUnassigned.retainAll(employees);
        employees.removeAll(availableEmployees);


        unAssignedEmployees.add(employees);
        unAssignedEmployees.add(availableEmployeesAndUnassigned);

        return unAssignedEmployees;
    }

    public boolean isAssignedByDate(long doneBy, LocalDate date, LocalTime hour, long employeeId) {
        String PERMISSION = "GET_ASSIGN";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        Shift shift = shiftController.getShiftbyDateAndTime(doneBy, date, hour);
        return shift.getAssignedEmployees().values().stream()
                .anyMatch(set -> set.contains(employeeId));
    }

}
