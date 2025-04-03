package employee.domain;

import employee.domain.enums.ShiftType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class Shift {
    private long id;
    private ShiftType shiftType;
    private LocalDate shiftDate;
    private Map<Role,Integer> rolesRequired;
    private Map<Role, List<Employee>> assignedEmployees;
    private boolean isAssignedShitManager;
    private boolean isOpen;
    private LocalDate createDate;
    private LocalDate updateDate;

}
