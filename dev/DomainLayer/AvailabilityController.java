package DomainLayer;

import DomainLayer.exception.UnauthorizedPermissionException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;

public class AvailabilityController {
    private final EmployeeController employeeController;
    
    public AvailabilityController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }

    public boolean markAvailable(Shift shift, long doneBy) {
        String PERMISSION = "UPDATE_AVAILABLE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to update availability");
        }
        if (shift == null){
            throw new IllegalArgumentException("Shift cannot be null");
        }
        if (doneBy <= 0){
            throw new IllegalArgumentException("employeeID cannot be less than 0");
        }
        if (shift.getAvailableEmployees() != null && shift.getAvailableEmployees().contains(doneBy)) {
            throw new IllegalArgumentException("Employee already exists in available employees");
        }
        Set<Long> availableEmployees = shift.getAvailableEmployees();
        if (availableEmployees == null) {
            availableEmployees = new HashSet<>();
        }
        availableEmployees.add(doneBy);
        shift.setAvailableEmployees(availableEmployees);
        return true;
    }

    public boolean removeAvailability(Shift shift, long doneBy) {
        String PERMISSION = "UPDATE_AVAILABLE";
        if(!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to update availability");
        }
        if(shift == null){
            throw new IllegalArgumentException("Shift cannot be null");
        }
        if(doneBy <= 0){
            throw new IllegalArgumentException("employeeID cannot be less than 0");
        }
        Set<Long> availableEmployees = shift.getAvailableEmployees();
        if (!availableEmployees.contains(doneBy)) {
            throw new IllegalArgumentException("Employee not found in available employees");
        }
        availableEmployees.remove(doneBy);
        shift.setAvailableEmployees(availableEmployees);
        return true;
    }

    public boolean isAvailable(Shift shift, long doneBy) {
        String PERMISSION = "UPDATE_AVAILABLE";
        if (!employeeController.isEmployeeAuthorised(doneBy, PERMISSION)) {
            throw new UnauthorizedPermissionException("User does not have permission to check availability");
        }
        Set<Long> availableEmployees = shift.getAvailableEmployees();
        return availableEmployees.contains(doneBy);
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
                    .put(shift.getShiftType().toString(), isAvailable(shift, doneBy));
        }
        return availability;
    }

}
