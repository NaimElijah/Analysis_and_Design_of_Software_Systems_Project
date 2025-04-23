package ServiceLayer;

import DomainLayer.Shift;
import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class ShiftSL {
    private final Shift shift;

    // Constructor
    public ShiftSL(Shift shift) {
        this.shift = shift;
    }

    // Getters
    public long getId() {
        return shift.getId();
    }

    public String getShiftType() {
        return shift.getShiftType();
    }

    public LocalDate getShiftDate() {
        return shift.getShiftDate();
    }

    public Map<String, Integer> getRolesRequired() {
        return shift.getRolesRequired();
    }

    public Map<String, Set<Long>> getAssignedEmployees() {
        return shift.getAssignedEmployees();
    }

    public Set<Long> getAvailableEmployees() {
        return shift.getAvailableEmployees();
    }

    public boolean isAssignedShiftManager() {
        return shift.isAssignedShiftManager();
    }

    public boolean isOpen() {
        return shift.isOpen();
    }
    public LocalDate getCreateDate() {
        return shift.getCreateDate();
    }
    public LocalDate getUpdateDate() {
        return shift.getUpdateDate();
    }

    // Setters
    public void setShiftType(ShiftType shiftType) {
        shift.setShiftType(shiftType);
    }

    public void setShiftDate(LocalDate shiftDate) {
        shift.setShiftDate(shiftDate);
    }
    public void setRolesRequired(Map<String, Integer> rolesRequired) {
        shift.setRolesRequired(rolesRequired);
    }
    public void setAssignedEmployees(Map<String, Set<Long>> assignedEmployees) {
        shift.setAssignedEmployees(assignedEmployees);
    }
    public void setAvailableEmployees(Set<Long> availableEmployees) {
        shift.setAvailableEmployees(availableEmployees);
    }
    public void setAssignedShiftManager(boolean assignedShiftManager) {
        shift.setAssignedShiftManager(assignedShiftManager);
    }
    public void setOpen(boolean open) {
        shift.setOpen(open);
    }

    public void setUpdateDate(LocalDate updateDate) {
        shift.setUpdateDate(updateDate);
    }






}
