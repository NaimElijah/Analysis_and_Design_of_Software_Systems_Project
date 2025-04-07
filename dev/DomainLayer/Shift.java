package DomainLayer;

import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Shift {
    private long id;
    private ShiftType shiftType;
    private LocalDate shiftDate;
    private Map<Role,Integer> rolesRequired;
    private Map<Role, Set<Employee>> assignedEmployees;
    private Set<Employee> AvailableEmployees;
    private boolean isAssignedShitManager;
    private boolean isOpen;
    private LocalDate createDate;
    private LocalDate updateDate;

    public Shift(long id, ShiftType shiftType, LocalDate shiftDate, Map<Role, Integer> rolesRequired, Map<Role, Set<Employee>> assignedEmployees, Set<Employee> AvailableEmployees, boolean isAssignedShitManager, boolean isOpen, LocalDate updateDate) {
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

    public void setId(long id) {
        this.id = id;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public Map<Role, Integer> getRolesRequired() {
        return rolesRequired;
    }

    public void setRolesRequired(Map<Role, Integer> rolesRequired) {
        this.rolesRequired = rolesRequired;
    }

    public Map<Role, Set<Employee>> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(Map<Role, Set<Employee>> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public Set<Employee> getAvailableEmployees() {
        return AvailableEmployees;
    }

    public void setAvailableEmployees(Set<Employee> availableEmployees) {
        AvailableEmployees = availableEmployees;
    }

    public boolean isAssignedShitManager() {
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



