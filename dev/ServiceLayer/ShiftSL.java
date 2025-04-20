package ServiceLayer;

import DomainLayer.Shift;

public class ShiftSL {
    private final Shift shift;

    // Constructor
    public ShiftSL(Shift shift) {
        this.shift = shift;
    }

    // Getters
    public String getId() {
        return String.valueOf(shift.getId());
    }

    public String getShiftType() {
        return shift.getShiftType();
    }

    public String getShiftDate() {
        return shift.getShiftDate().toString();
    }

    public String getRolesRequired() {
        return shift.getRolesRequired().toString();
    }

    public String getAssignedEmployees() {
        return shift.getAssignedEmployees().toString();
    }

    public String getAvailableEmployees() {
        return shift.getAvailableEmployees().toString();
    }

    public boolean isAssignedShiftManager() {
        return shift.isAssignedShiftManager();
    }

    public boolean isOpen() {
        return shift.isOpen();
    }
    public String getCreateDate() {
        return shift.getCreateDate().toString();
    }
    public String getUpdateDate() {
        return shift.getUpdateDate().toString();
    }

    // Setters
    public void setShiftType(String shiftType) {
        shift.setShiftType(shift.getShiftType());
    }

    public void setShiftDate(String shiftDate) {
        shift.setShiftDate(shift.getShiftDate());
    }
    public void setRolesRequired(String rolesRequired) {
        shift.setRolesRequired(shift.getRolesRequired());
    }
    public void setAssignedEmployees(String assignedEmployees) {
        shift.setAssignedEmployees(shift.getAssignedEmployees());
    }
    public void setAvailableEmployees(String availableEmployees) {
        shift.setAvailableEmployees(shift.getAvailableEmployees());
    }
    public void setAssignedShiftManager(boolean assignedShiftManager) {
        shift.setAssignedShiftManager(assignedShiftManager);
    }
    public void setOpen(boolean open) {
        shift.setOpen(open);
    }

    public void setUpdateDate(String updateDate) {
        shift.setUpdateDate(shift.getUpdateDate());
    }






}
