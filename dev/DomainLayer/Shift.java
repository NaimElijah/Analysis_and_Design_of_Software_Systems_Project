package DomainLayer;

import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Shift {
    private long id;
    private String shiftType;
    private LocalDate shiftDate;
    private Map<String,Integer> rolesRequired;
    private Map<String, Set<Employee>> assignedEmployees;
    private Set<Employee> AvailableEmployees;
    private boolean isAssignedShitManager;
    private boolean isOpen;
    private LocalDate createDate;
    private LocalDate updateDate;

    public Shift(long id, String shiftType, LocalDate shiftDate, Map<String, Integer> roleRequired, Map<String, Set<Employee>> assignedEmployees, Set<Employee> AvailableEmployees, boolean isAssignedShitManager, boolean isOpen, LocalDate updateDate) {
        this.id = id;
        this.shiftType = shiftType;
        this.shiftDate = shiftDate;
        this.rolesRequired = rolesRequired;
        this.assignedEmployees = assignedEmployees;
        this.AvailableEmployees = AvailableEmployees;
        this.isAssignedShitManager = isAssignedShitManager;
        this.isOpen = isOpen;
        this.createDate = LocalDate.now();
        this.updateDate = updateDate;
    }

    public long getId() {
        return id;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        if (!"MORNING".equals(shiftType) && !"EVENING".equals(shiftType)) {
            throw new IllegalArgumentException("Shift type must be either MORNING or EVENING");
        }
        this.shiftType = shiftType;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Map<String, Integer> getRolesRequired() {
        return rolesRequired;
    }

    public void setRolesRequired(Map<String, Integer> rolesRequired) {
        this.rolesRequired = rolesRequired;
    }

    public Map<String, Set<Employee>> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(Map<String, Set<Employee>> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public Set<Employee> getAvailableEmployees() {
        return AvailableEmployees;
    }

    public void setAvailableEmployees(Set<Employee> availableEmployees) {
        AvailableEmployees = availableEmployees;
    }

    public boolean isAssignedShiftManager() {
        return isAssignedShitManager;
    }

    public void setAssignedShiftManager(boolean assignedShitManager) {
        isAssignedShitManager = assignedShitManager;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}



