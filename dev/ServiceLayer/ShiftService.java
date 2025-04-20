package ServiceLayer;

import DomainLayer.*;
import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class ShiftService {
    private final ShiftController shiftController;

    public ShiftService(ShiftController shiftController) {
        this.shiftController = shiftController;
    }

    public boolean createShift(String shiftType, LocalDate date,
                               Map<String, Integer> rolesRequired,
                               Map<String, Set<Employee>> assignedEmployees,
                               Set<Employee> availableEmployees,
                               boolean isAssignedShiftManager,
                               boolean isOpen, LocalDate updateDate) {
        try {
            return shiftController.createShift(shiftType, date, rolesRequired, assignedEmployees,
                    availableEmployees, isAssignedShiftManager, isOpen, updateDate);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean createWeeklyShifts(LocalDate date,
                                      Map<String, Integer> rolesRequired)
    {
        try {
            if (date == null || rolesRequired == null) {
                throw new IllegalArgumentException("Date and roles required cannot be null");
            }
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Date cannot be in the past");
            }
            return shiftController.createWeeklyShifts(date, rolesRequired);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean removeShift(long shiftId) {
        try {
            if (shiftId <= 0) {
                throw new IllegalArgumentException("Shift ID must be a positive number");
            }
            return shiftController.removeShift(shiftId);
        } catch (RuntimeException e) {
            return false;
        }
    }

    public String updateShift(long shiftId, String shiftType, LocalDate date,
                               Map<String, Integer> rolesRequired,
                               Map<String, Set<Employee>> assignedEmployees,
                               boolean isAssignedShiftManager, boolean isOpen, LocalDate updateDate) {
        try {
            boolean result = shiftController.updateShift(shiftId, shiftType, date, rolesRequired,
                    assignedEmployees, isAssignedShiftManager, isOpen, updateDate);
            if (result) {
                return "Shift updated successfully";
            }
            else {
                return "Failed to update shift";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL getShiftById(long shiftId) {
        try {
            ShiftSL shift = new ShiftSL(shiftController.getShiftByID(shiftId));
            if (shift.getId() != null) {
                return shift;
            }
            else
            {
                throw new RuntimeException("Shift with ID " + shiftId + " not found");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL[] getAllShifts() {
        try {
            ShiftSL[] shifts = new ShiftSL[shiftController.getAllShifts().size()];
            int i = 0;
            for (DomainLayer.Shift shift : shiftController.getAllShifts()) {
                shifts[i] = new ShiftSL(shift);
                i++;
            }
            return shifts;
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL[] getShiftsByDate(LocalDate date) {
        try {
            ShiftSL[] shifts = new ShiftSL[shiftController.getShiftsByDate(date).size()];
            int i = 0;
            for (DomainLayer.Shift shift : shiftController.getAllShifts()) {
                shifts[i] = new ShiftSL(shift);
                i++;
            }
            return shifts;
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL[] getShiftsByEmployee(Employee employee) {
        try {
            ShiftSL[] shifts = new ShiftSL[shiftController.getShiftsByEmployee(employee).size()];
            int i = 0;
            for (DomainLayer.Shift shift : shiftController.getAllShifts()) {
                shifts[i] = new ShiftSL(shift);
                i++;
            }
            return shifts;
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ShiftSL getShift(LocalDate date, String shiftType) {
        try {
            ShiftSL shift = new ShiftSL(shiftController.getshift(date, shiftType));
            if (shift.getId() != null) {
                return shift;
            }
            else
            {
                throw new RuntimeException("The " + shiftType + "Shift on date " + date + " not found");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateRolesRequired(long shiftId, Map<String, Integer> rolesRequired) {
        try {
            boolean result = shiftController.updateRolesRequired(shiftId, rolesRequired);
            if (result) {
                return "success to update roles required";
            }
            else {
                return ("Failed to update roles required");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateAssignedEmployees(long shiftId, Map<String, Set<Employee>> assignedEmployees) {
        try {
            boolean result = shiftController.updateAssignedEmployees(shiftId, assignedEmployees);
            if (result) {
                return "success to update assigned employees";
            }
            else {
                return ("Failed to update assigned employees");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateShiftManager(long shiftId, boolean isAssignedShiftManager) {
        try {
            boolean result = shiftController.updateShiftManager(shiftId, isAssignedShiftManager);
            if (result) {
                return "success to update shift manager";
            }
            else {
                return ("Failed to update shift manager");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateOpenStatus(long shiftId, boolean isOpen) {
        try {
            boolean result = shiftController.updateOpenStatus(shiftId, isOpen);
            if (result) {
                return "success to update open status";
            }
            else {
                return ("Failed to update open status");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateShiftAvailableEmployees(long shiftId, Set<Employee> employees) {
        try {
            boolean result = shiftController.updateShiftAvailableEmployees(shiftId, employees);
            if (result) {
                return "success to update available employees";
            }
            else {
                return ("Failed to update available employees");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String addAvailableEmployee(long shiftId, Employee employee) {
        try{
            boolean result = shiftController.addAvailableEmployee(shiftId, employee);
            if (result) {
                return "success to add employee to available employees";
            }
            else {
               return ("Failed to add employee to available employees");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeAvailableEmployee(long shiftId, Employee employee) {
        try{
            boolean result = shiftController.removeAvailableEmployee(shiftId, employee);
            if (result) {
                return "success to remove employee to available employees";
            }
            else {
                return ("Failed to remove employee to available employees");
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String addAssignedEmployee(long shiftId, String role, Employee employee) {
        try {
            boolean result = shiftController.addAssignedEmployee(shiftId, role, employee);
            if (result) {
                return "success to add employee to assigned employees";
            } else {
                return "Failed to add employee to assigned employees";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeAssignedEmployee(long shiftId, String role, Employee employee) {
        try{
            boolean result = shiftController.removeAssignedEmployee(shiftId, role, employee);
            if (result) {
                return "success to remove employee to assigned employees";
            }
            else {
                return ("Failed to remove employee to assigned employees");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String addRoleRequired(long shiftId, String role, Integer roleRequired) {
        try {
            boolean result = shiftController.addRoleRequired(shiftId, role, roleRequired);
            if (result) {
                return "success to add role to required roles";
            } else {
                return "Failed to add role to required roles";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeRoleRequired(long shiftId, String role) {
        try {
            boolean result = shiftController.removeRoleRequired(shiftId, role);
            if (result) {
                return "success to remove role to required roles";
            } else {
                return "Failed to remove role to required roles";
            }
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
