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

    public String removeShift(long doneBy, LocalDate date, ShiftType shiftType) {
        try {
            boolean result = shiftController.removeShift(doneBy, date, shiftType);
            return result ? "Shift deleted successfully" : "Failed to delete shift";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateShift(long doneBy, long shiftId, ShiftType shiftType, LocalDate date,
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

            boolean result = assignmentController.assignEmployeeToRole(shift, employeeId, role);
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
    public String removeAssignment(long doneBy, long shiftId, long employeeId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return "Shift not found";
            }

            boolean result = assignmentController.removeAssignment(shift, employeeId);
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

            return assignmentController.isAssigned(shift, employeeId);
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Marks an employee as available for a shift
     * @param doneBy the employee making the change
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to mark as available
     * @return a message indicating success or failure
     */
    public String markEmployeeAvailable(long doneBy, long shiftId, long employeeId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return "Shift not found";
            }

            availabilityController.markAvailable(shift, employeeId);
            return "Employee marked as available successfully";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Removes an employee's availability for a shift
     * @param doneBy the employee making the change
     * @param shiftId the ID of the shift
     * @param employeeId the ID of the employee to remove availability for
     * @return a message indicating success or failure
     */
    public String removeEmployeeAvailability(long doneBy, long shiftId, long employeeId) {
        try {
            Shift shift = shiftController.getShiftByID(doneBy, shiftId);
            if (shift == null) {
                return "Shift not found";
            }

            availabilityController.removeAvailability(shift, employeeId);
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
     * @param startDate the start date of the week
     * @return a map of dates to shift types and availability
     */
    public Map<LocalDate, Map<String, Boolean>> getEmployeeWeeklyAvailability(long doneBy, long employeeId, LocalDate startDate) {
        try {
            Set<Shift> weekShifts = shiftController.getShiftsByWeek(doneBy, startDate);
            if (weekShifts.isEmpty()) {
                return new HashMap<>();
            }

            return availabilityController.getWeeklyAvailability(weekShifts.stream().toList(), employeeId);
        } catch (RuntimeException e) {
            return new HashMap<>();
        }
    }

    public Set<ShiftSL> getShiftsByWeek(long doneBy, Week week) {
        try {
            Set<ShiftSL> shiftSLS = new HashSet<>();
            Set<Shift> weekShifts = shiftController.getShiftsByWeek(doneBy, week);
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
