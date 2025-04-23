package DomainLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AvailabilityController {
    public void markAvailable(Shift shift, long employeeId) {
        shift.getAvailableEmployees().add(employeeId);
    }

    public void removeAvailability(Shift shift, long employeeId) {
        shift.getAvailableEmployees().remove(employeeId);
    }

    public boolean isAvailable(Shift shift, long employeeId) {
        return shift.getAvailableEmployees().contains(employeeId);
    }

    public Map<LocalDate, Map<String, Boolean>> getWeeklyAvailability(List<Shift> weekShifts, long employeeId) {
        Map<LocalDate, Map<String, Boolean>> availability = new TreeMap<>();
        for (Shift shift : weekShifts) {
            availability
                    .computeIfAbsent(shift.getShiftDate(), k -> new HashMap<>())
                    .put(shift.getShiftType(), isAvailable(shift, employeeId));
        }
        return availability;
    }
}
