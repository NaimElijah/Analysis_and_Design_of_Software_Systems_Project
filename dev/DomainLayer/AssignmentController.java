package DomainLayer;

import DomainLayer.exception.UnauthorizedPermissionException;

import java.util.*;

public class AssignmentController {
    private final EmployeeController employeeController;

    public AssignmentController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }
    public boolean assignEmployeeToRole(Shift shift, long doneBy, String role, long employeeId) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
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

    public boolean removeAssignment(long doneBy, Shift shift, String role, long employeeId) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to remove employees");
        }
        Map <String, Set<Long>> assignedEmployees = shift.getAssignedEmployees();
        Set<Long> employeesInRole = assignedEmployees.get(role);
        employeesInRole.remove(employeeId);
        assignedEmployees.put(role, employeesInRole);
        shift.setAssignedEmployees(assignedEmployees);
        return true;
    }

    public boolean isAssigned(long doneBy, Shift shift, long employeeId) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        return shift.getAssignedEmployees().values().stream()
                .anyMatch(set -> set.contains(employeeId));
    }

    public boolean isAssignedManager(long doneBy, Shift shift) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        if (!shift.isAssignedShiftManager()){
            throw new RuntimeException("Shift Manager is not assigned to this shift");
        }
        return shift.isAssignedShiftManager();
    }

    public List<Set<Long>> getUnassignedEmployees(long doneBy, Shift shift){
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if(!employeeController.isEmployeeAuthorised(doneBy,PERMISSION)){
            throw new UnauthorizedPermissionException("User does not have permission to assign employees");
        }
        List<Set<Long>> unAssignedEmployees = new ArrayList<>();
        Set<Long> employees = employeeController.getAllEmployees().keySet();
        employees.removeIf(employee -> isAssigned(doneBy, shift, employee)); // All the employees that are not assigned to the shi
        Set<Long> availableEmployees = shift.getAvailableEmployees();
        Set<Long> availableEmployeesAndUnassigned = new HashSet<>(availableEmployees);
        availableEmployeesAndUnassigned.retainAll(employees);
        employees.removeAll(availableEmployees);


        unAssignedEmployees.add(employees);
        unAssignedEmployees.add(availableEmployeesAndUnassigned);

        return unAssignedEmployees;
    }


}
