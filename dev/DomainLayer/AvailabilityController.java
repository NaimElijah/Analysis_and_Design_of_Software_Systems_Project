package DomainLayer;

import DomainLayer.exception.UnauthorizedPermissionException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AvailabilityController {
    private final EmployeeController employeeController;
    
    public AvailabilityController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }
    public void markAvailable(Shift shift, long doneBy) {
        String PERMISSION = "UPDATE_AVAILABLE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to update availability");
        }
        shift.getAvailableEmployees().add(doneBy);
    }

    public void removeAvailability(Shift shift, long doneBy) {
        String PERMISSION = "UPDATE_AVAILABLE";
        if(!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to update availability");
        }
        shift.getAvailableEmployees().remove(doneBy);
    }

    public boolean isAvailable(Shift shift, long doneBy) {
        String PERMISSION = "UPDATE_AVAILABLE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to check availability");
        }
        return shift.getAvailableEmployees().contains(doneBy);
    }

    public Map<LocalDate, Map<String, Boolean>> getWeeklyAvailability(List<Shift> weekShifts, long doneBy) {
        String PERMISSION = "UPDATE_AVAILABLE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to check availability");
        }
        Map<LocalDate, Map<String, Boolean>> availability = new TreeMap<>();
        for (Shift shift : weekShifts) {
            availability
                    .computeIfAbsent(shift.getShiftDate(), k -> new HashMap<>())
                    .put(shift.getShiftType(), isAvailable(shift, doneBy));
        }
        return availability;
    }

}
