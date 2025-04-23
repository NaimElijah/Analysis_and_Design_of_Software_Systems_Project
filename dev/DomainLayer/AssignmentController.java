package DomainLayer;

import java.util.HashSet;
import java.util.Set;

public class AssignmentController {
    private final AuthorisationController authorizationController;

    public AssignmentController(AuthorisationController authorizationController) {
        this.authorizationController = authorizationController;
    }
    public boolean assignEmployeeToRole(Shift shift, long doneBy, String role) {
        String PERMISSION = "ASSIGN_EMPLOYEE";
        if (!authorizationController.hasPermission(doneBy, PERMISSION)) {
            return false;
        }
        if (!shift.getRolesRequired().containsKey(role)) return false;

        Set<Long> assigned = shift.getAssignedEmployees()
                .computeIfAbsent(role, k -> new HashSet<>());

        int required = shift.getRolesRequired().get(role);
        if (assigned.size() >= required) return false;

        return assigned.add(doneBy);
    }

    public boolean removeAssignment(Shift shift, long doneBy) {
        for (Set<Long> assigned : shift.getAssignedEmployees().values()) {
            assigned.remove(doneBy);
        }
        return true;
    }

    public boolean isAssigned(Shift shift, long doneBy) {
        return shift.getAssignedEmployees().values().stream()
                .anyMatch(set -> set.contains(doneBy));
    }
}
