package ServiceLayer;

import DomainLayer.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class ShiftService {
    private final ShiftController shiftController;

    public ShiftService(ShiftController shiftController) {
        this.shiftController = shiftController;
    }

    public String createShift(long doneBy, String shiftType, LocalDate date,
                               Map<String, Integer> rolesRequired,
                               Map<String, Set<Long>> assignedEmployees,
                               Set<Long> availableEmployees,
                               boolean isAssignedShiftManager,
                               boolean isOpen, LocalDate updateDate) {
        try {
            boolean result = shiftController.createShift(doneBy, shiftType, date, rolesRequired, assignedEmployees,
                    availableEmployees, isAssignedShiftManager, isOpen, updateDate);
            return result ? "Shift created successfully" : "Failed to create shift";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String createWeeklyShifts(long doneBy, LocalDate date,
                                      Map<String, Integer> rolesRequired) {
        try {
            boolean result = shiftController.createWeeklyShifts(doneBy, date, rolesRequired);
            return result ? "Weekly shifts created successfully" : "Failed to create weekly shifts";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeShiftByID(long doneBy, long shiftId) {
        try {
            if (shiftId <= 0) {
                throw new IllegalArgumentException("Shift ID must be a positive number");
            }
            return shiftController.removeShiftByID(doneBy, shiftId);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public String removeShift(long doneBy, LocalDate date, String shiftType) {
        try {
            boolean result = shiftController.removeShift(doneBy, date, shiftType);
            return result ? "Shift deleted successfully" : "Failed to delete shift";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateShift(long doneBy, long shiftId, String shiftType, LocalDate date,
                              boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        try {
            boolean result = shiftController.updateShift(doneBy, shiftId, shiftType, date, isAssignedShiftManager, isOpen, updateDate);
            if (result) {
                return "Shift updated successfully";
            } else {
                return "Failed to update shift";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL getShiftById(long doneBy, long shiftId) {
        try {
            ShiftSL shift = new ShiftSL(shiftController.getShiftByID(doneBy, shiftId));
            if (shift.getId() >=0) {
                return shift;
            } else {
                throw new RuntimeException("Shift with ID " + shiftId + " not found");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL[] getAllShifts(long doneBy) {
        try {
            ShiftSL[] shifts = new ShiftSL[shiftController.getAllShifts(doneBy).size()];
            int i = 0;
            for (DomainLayer.Shift shift : shiftController.getAllShifts(doneBy)) {
                shifts[i] = new ShiftSL(shift);
                i++;
            }
            return shifts;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL[] getShiftsByDate(long doneBy, LocalDate date) {
        try {
            ShiftSL[] shifts = new ShiftSL[shiftController.getShiftsByDate(doneBy, date).size()];
            int i = 0;
            for (DomainLayer.Shift shift : shiftController.getShiftsByDate(doneBy, date)) {
                shifts[i] = new ShiftSL(shift);
                i++;
            }
            return shifts;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL[] getShiftsByEmployee(long doneBy, long employeeID) {
        try {
            ShiftSL[] shifts = new ShiftSL[shiftController.getShiftsByEmployee(doneBy, employeeID).size()];
            int i = 0;
            for (DomainLayer.Shift shift : shiftController.getShiftsByEmployee(doneBy, employeeID)) {
                shifts[i] = new ShiftSL(shift);
                i++;
            }
            return shifts;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL getShift(long doneBy, LocalDate date, String shiftType) {
        try {
            ShiftSL shift = new ShiftSL(shiftController.getshift(doneBy, date, shiftType));
            if (shift.getId() >= 0) {
                return shift;
            } else {
                throw new RuntimeException("The " + shiftType + " Shift on date " + date + " not found");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateRolesRequired(long doneBy, long shiftId, String role, Integer roleRequired) {
        try {
            boolean result = shiftController.updateRolesRequired(doneBy, shiftId, role, roleRequired);
            return result ? "success to update roles required" : "Failed to update roles required";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateAssignedEmployees(long doneBy, long shiftId, Map<String, Set<Long>> assignedEmployees) {
        try {
            boolean result = shiftController.updateAssignedEmployees(doneBy, shiftId, assignedEmployees);
            return result ? "success to update assigned employees" : "Failed to update assigned employees";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateShiftManager(long doneBy, long shiftId, boolean isAssignedShiftManager) {
        try {
            boolean result = shiftController.updateShiftManager(doneBy, shiftId, isAssignedShiftManager);
            return result ? "success to update shift manager" : "Failed to update shift manager";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateOpenStatus(long doneBy, long shiftId, boolean isOpen) {
        try {
            boolean result = shiftController.updateOpenStatus(doneBy, shiftId, isOpen);
            return result ? "success to update open status" : "Failed to update open status";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateShiftAvailableEmployees(long doneBy, long shiftId, Set<Long> employees) {
        try {
            boolean result = shiftController.updateShiftAvailableEmployees(doneBy, shiftId, employees);
            return result ? "success to update available employees" : "Failed to update available employees";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String addAvailableEmployee(long doneBy, long shiftId, long employeeID) {
        try {
            boolean result = shiftController.addAvailableEmployee(doneBy, shiftId, employeeID);
            return result ? "success to add employee to available employees" : "Failed to add employee to available employees";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeAvailableEmployee(long doneBy, long shiftId, long employeeID) {
        try {
            boolean result = shiftController.removeAvailableEmployee(doneBy, shiftId, employeeID);
            return result ? "success to remove employee to available employees" : "Failed to remove employee to available employees";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String addAssignedEmployee(long doneBy, long shiftId, String role, long employeeID) {
        try {
            boolean result = shiftController.addAssignedEmployee(doneBy, shiftId, role, employeeID);
            return result ? "success to add employee to assigned employees" : "Failed to add employee to assigned employees";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeAssignedEmployee(long doneBy, long shiftId, String role, long employeeID) {
        try {
            boolean result = shiftController.removeAssignedEmployee(doneBy, shiftId, role, employeeID);
            return result ? "success to remove employee to assigned employees" : "Failed to remove employee to assigned employees";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String addRoleRequired(long doneBy, long shiftId, String role, Integer roleRequired) {
        try {
            boolean result = shiftController.addRoleRequired(doneBy, shiftId, role, roleRequired);
            return result ? "success to add role to required roles" : "Failed to add role to required roles";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeRoleRequired(long doneBy, long shiftId, String role) {
        try {
            boolean result = shiftController.removeRoleRequired(doneBy, shiftId, role);
            return result ? "success to remove role to required roles" : "Failed to remove role to required roles";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> getRoles(long doneBy) {
        return shiftController.getRoles(doneBy);
    }
}
