package DomainLayer;

import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class Shift {
    private long id;
    private ShiftType shiftType;
    private LocalDate shiftDate;
    private Map<String,Integer> rolesRequired;
    private Map<String, List<Employee>> assignedEmployees;
    private boolean isAssignedShitManager;
    private boolean isOpen;
    private LocalDate createDate;
    private LocalDate updateDate;

}
