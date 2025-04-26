package ServiceLayer;

import DomainLayer.*;
import DomainLayer.enums.ShiftType;
import Util.Week;

import java.time.LocalDate;
import java.util.*;

public class ShiftService {
    private final ShiftController shiftController;
    private final AssignmentController assignmentController;
    private final AvailabilityController availabilityController;

    public ShiftService(ShiftController shiftController, AssignmentController assignmentController, AvailabilityController availabilityController) {
        this.shiftController = shiftController;
        this.assignmentController = assignmentController;
        this.availabilityController = availabilityController;
    }

    public String createShift(long doneBy, ShiftType shiftType, LocalDate date,
                              Map<String, Integer> rolesRequired,
                              Map<String, Set<Long>> assignedEmployees,
                              Set<Long> availableEmployees,
                              boolean isAssignedShiftManager,
                              boolean isOpen,String hours, LocalDate updateDate) {
        try {
            boolean result = shiftController.createShift(doneBy, shiftType, date, rolesRequired, assignedEmployees,
                    availableEmployees, isAssignedShiftManager, isOpen,hours, updateDate);
            return result ? "Shift created successfully" : "Failed to create shift";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String createWeeklyShifts(long doneBy, LocalDate date,
                                      Map<String, Integer> rolesRequired) {
        try {
            boolean result = shiftController.createWeeklyShifts(doneBy, date, rolesRequired);
            return result ? "Weekly shifts created successfully" : "Failed to create weekly shifts";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removeShiftByID(long doneBy, long shiftId) {
        try {
            if (shiftId <= 0) {
                throw new IllegalArgumentException("Shift ID must be a positive number");
            }
            boolean result = shiftController.removeShiftByID(doneBy, shiftId);
            return result ? "Shift deleted successfully" : "Failed to delete shift";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removeShift(long doneBy, LocalDate date, ShiftType shiftType) {
        try {
            boolean result = shiftController.removeShift(doneBy, date, shiftType);
            return result ? "Shift deleted successfully" : "Failed to delete shift";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String updateShift(long doneBy, long shiftId, ShiftType shiftType, LocalDate date,
                              boolean isAssignedShiftManager, boolean isOpen,String hours, LocalDate updateDate) {
        try {
            boolean result = shiftController.updateShift(doneBy, shiftId, shiftType, date, isAssignedShiftManager, isOpen,hours, updateDate);
            if (result) {
                return "Shift updated successfully";
            } else {
                return "Failed to update shift";
            }
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
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

    public ShiftSL[] getAllShiftsByDate(long doneBy, LocalDate date) {
        try {
            ShiftSL[] shifts = new ShiftSL[shiftController.getAllShiftsByDate(doneBy, date).size()];
            int i = 0;
            for (DomainLayer.Shift shift : shiftController.getAllShiftsByDate(doneBy, date)) {
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

    public ShiftSL getShift(long doneBy, LocalDate date, ShiftType shiftType) {
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
            return "Error: " + e.getMessage();
        }
    }

    public String updateShiftManager(long doneBy, long shiftId, boolean isAssignedShiftManager) {
        try {
            boolean result = shiftController.updateShiftManager(doneBy, shiftId, isAssignedShiftManager);
            return result ? "success to update shift manager" : "Failed to update shift manager";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String updateOpenStatus(long doneBy, long shiftId, boolean isOpen) {
        try {
            boolean result = shiftController.updateOpenStatus(doneBy, shiftId, isOpen);
            return result ? "success to update open status" : "Failed to update open status";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }


    public String removeRoleRequired(long doneBy, long shiftId, String role) {
        try {
            boolean result = shiftController.removeRoleRequired(doneBy, shiftId, role);
            return result ? "success to remove role to required roles" : "Failed to remove role to required roles";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public Set<String> getRoles(long doneBy) {
        return shiftController.getRoles(doneBy);
    }

    /**
     * Assigns an employee to a role in a shift
     * @param doneBy the employee making the assignment
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to assign
     * @param role the role to assign the employee to
     * @return a message indicating success or failure
     */
    public String assignEmployeeToRole(long doneBy, long shiftId, long employeeId, String role) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return "Shift not found";
            }

            boolean result = assignmentController.assignEmployeeToRole( shift, doneBy, role,employeeId);
            return result ? "Employee assigned to role successfully" : "Failed to assign employee to role";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Removes an employee's assignment from a shift
     * @param doneBy the employee making the change
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to remove
     * @return a message indicating success or failure
     */
    public String removeAssignment(long doneBy, long shiftId, String role, long employeeId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return "Shift not found";
            }

            boolean result = assignmentController.removeAssignment(doneBy, shift, role, employeeId);
            return result ? "Employee assignment removed successfully" : "Failed to remove employee assignment";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Checks if an employee is assigned to a shift
     * @param doneBy the employee making the query
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to check
     * @return true if the employee is assigned, false otherwise
     */
    public boolean isEmployeeAssigned(long doneBy, long shiftId, long employeeId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return false;
            }

            return assignmentController.isAssigned(doneBy, shift, employeeId);
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Marks an employee as available for a shift
     * @param doneBy the employee making the change
     * @param shiftId the ID of the shift
     * @return a message indicating success or failure
     */
    public String markEmployeeAvailable(long doneBy, long shiftId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return "Shift not found";
            }

            availabilityController.markAvailable(shift, doneBy);
            return "Employee marked as available successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Removes an employee's availability for a shift
     * @param doneBy the employee making the change
     * @param shiftId the ID of the shift
     * @return a message indicating success or failure
     */
    public String removeEmployeeAvailability(long doneBy, long shiftId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return "Shift not found";
            }
            availabilityController.removeAvailability(shift, doneBy);
            return "Employee availability removed successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Checks if an employee is available for a shift
     * @param doneBy the employee making the query
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to check
     * @return true if the employee is available, false otherwise
     */
    public boolean isEmployeeAvailable(long doneBy, long shiftId, long employeeId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return false;
            }
            return availabilityController.isAvailable(shift, employeeId);
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Gets an employee's weekly availability
     * @param doneBy the employee making the query
     * @param employeeId the ID of the employee to get availability for
     * @param week the week of the shifts
     * @return a map of dates to shift types and availability
     */
    public Map<LocalDate, Map<String, Boolean>> getEmployeeWeeklyAvailability(long doneBy, long employeeId, Week week) {
        try {
            List<Shift> weekShifts = shiftController.getShiftsByWeek(doneBy, week);
            if (weekShifts.isEmpty()) {
                return new HashMap<>();
            }

            return availabilityController.getWeeklyAvailability(weekShifts, employeeId);
        } catch (RuntimeException e) {
            return new HashMap<>();
        }
    }

    public Set<ShiftSL> getShiftsByWeek(long doneBy, Week week) {
        try {
            Set<ShiftSL> shiftSLS = new HashSet<>();
            List<Shift> weekShifts = shiftController.getShiftsByWeek(doneBy, week);
            if (weekShifts == null) {
                return shiftSLS;
            }
            for (Shift shift : weekShifts) {
                shiftSLS.add(new ShiftSL(shift));
            }
            return shiftSLS;
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
