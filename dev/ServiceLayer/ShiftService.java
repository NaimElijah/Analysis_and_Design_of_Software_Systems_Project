package ServiceLayer;

import DTOs.ShiftDTO;
import DomainLayer.EmployeeSubModule.AssignmentController;
import DomainLayer.EmployeeSubModule.AvailabilityController;
import DomainLayer.EmployeeSubModule.ShiftController;
import DomainLayer.enums.ShiftType;
import Util.Week;

import java.time.LocalDate;
import java.time.LocalTime;
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
    // ========================
    // DTO related methods
    // ========================
    private String serializeShiftDTO(ShiftDTO dto) {
        try {
            return dto.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error serializing ShiftDTO: " + e.getMessage());
        }
    }
    private ShiftDTO deserializeShiftDTO(String json) {
        try {
            return ShiftDTO.deserialize(json);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing ShiftDTO: " + e.getMessage());
        }
    }
    public ShiftDTO getShiftDTO(long doneBy, long shiftId) {
        try {
            String str = shiftController.getShiftByID(doneBy, shiftId);
            ShiftDTO shift = deserializeShiftDTO(str);

            if (shift.getId() >= 0) {
                return shift;
            } else {
                throw new RuntimeException("Shift with ID " + shiftId + " not found");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================
    // Shift related methods
    // ========================

    public String createShift(long doneBy, ShiftType shiftType, LocalDate date,
                              Map<String, Integer> rolesRequired,
                              Map<String, Set<Long>> assignedEmployees,
                              Set<Long> availableEmployees,
                              boolean isAssignedShiftManager,
                              boolean isOpen,LocalTime startHour , LocalTime endHour, LocalDate updateDate) {
        try {
            boolean result = shiftController.createShift(doneBy, shiftType, date, rolesRequired, assignedEmployees,
                    availableEmployees, isAssignedShiftManager, isOpen ,startHour , endHour, updateDate);
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
                              boolean isAssignedShiftManager, boolean isOpen,LocalTime startHour , LocalTime endHour, LocalDate updateDate) {
        try {
            boolean result = shiftController.updateShift(doneBy, shiftId, shiftType, date, isAssignedShiftManager, isOpen,startHour,endHour, updateDate);
            if (result) {
                return "Shift updated successfully";
            } else {
                return "Failed to update shift";
            }
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getShiftById(long doneBy, long shiftId) {
        try {
            String shift = shiftController.getShiftByID(doneBy, shiftId);
            return shift;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAllShifts(long doneBy) {
        try {
            String domainShifts = shiftController.getAllShifts(doneBy);
            return domainShifts;
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getAllShiftsByDateBranch(long doneBy, LocalDate date, String branch) {
        try {
            String shifts = shiftController.getAllShiftsByDateAndBranch(doneBy, date, branch);
            return shifts;
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getShiftsByEmployee(long doneBy, long employeeID) {
        try {
            return shiftController.getShiftsByEmployee(doneBy, employeeID);
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getShift(long doneBy, LocalDate date, ShiftType shiftType) {
        try {
            String shift = shiftController.getshift(doneBy, date, shiftType);
            return shift;
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
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

    public String getRoles(long doneBy) {
        try{
            String roles = shiftController.getRoles(doneBy);
            return roles;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
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
            boolean result = assignmentController.assignEmployeeToRole( shiftId, doneBy, role,employeeId);
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
            boolean result = assignmentController.removeAssignment(doneBy, shiftId, role, employeeId);
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
            return assignmentController.isAssigned(doneBy, shiftId, employeeId);
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
            availabilityController.markAvailable(shiftId, doneBy);
            return "Employee marked as available successfully";
        } catch (RuntimeException e) {
            return  e.getMessage();
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
            availabilityController.removeAvailability(shiftId, doneBy);
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
            return availabilityController.isAvailable(shiftId, employeeId);
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
            return availabilityController.getWeeklyAvailability(week, employeeId);
        } catch (RuntimeException e) {
            return new HashMap<>();
        }
    }

    public Set<ShiftDTO> getShiftsByWeek(long doneBy, Week week) {
        try {
            Set<ShiftDTO> shiftDTOs = new HashSet<>();
            String str = shiftController.getShiftsByWeek(doneBy, week);
            if (str == null || str.isEmpty()) {
                return shiftDTOs;
            }
            for(String shiftStr : str.split("\n")) {
                ShiftDTO shiftDTO = deserializeShiftDTO(shiftStr);
                shiftDTOs.add(shiftDTO);
            }
            return shiftDTOs;
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String isAssignedManager(long doneBy, long shiftId){
        try{
            assignmentController.isAssignedManager(doneBy, shiftId);
                return "success to check if assigned manager";
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<Set<Long>> getUnassignedManager(long doneBy, long shiftId){
        try {
            return assignmentController.getUnassignedEmployees(doneBy, shiftId);
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if an employee is assigned to a shift at a specific date, time, and branch.
     *
     * @param doneBy the ID of the user performing the check
     * @param date the date of the shift
     * @param hour the time of the shift
     * @param employeeId the ID of the employee to check
     * @param branch the branch where the shift is located
     * @return true if the employee is assigned to the shift, false otherwise
     */
    public boolean isAssignedByDateTimeBranch(long doneBy, LocalDate date, LocalTime hour, long employeeId, String branch) {
        try {
            return assignmentController.isAssignedEmployeeByDateTimeBranch(doneBy, date, hour, employeeId, branch);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public String getShiftByEmployee(long doneBy, long employeeId) {
        try {
            return shiftController.getShiftByEmployee(doneBy, employeeId);
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
    /**
     * Checks whether a specific role is assigned at a given date, time, and branch location.
     *
     * @param date The date to verify the role assignment.
     * @param hour The time to verify the role assignment.
     * @param role The role to check within the given shift.
     * @param branch The branch location where the verification is performed.
     * @return true if the specified role is assigned at the specified date, time, and branch; false otherwise.
     * @throws RuntimeException if an error occurs during the operation.
     */
    public boolean isAssignedRoleByDateTimeBranch( LocalDate date, LocalTime hour, String role, String branch) {
        try {
            return assignmentController.isAssignedRoleByDateTimeBranch( date, hour, role, branch);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAllShiftsByDate(long doneBy, LocalDate date) {
        try {
            String shifts = shiftController.getAllShiftsByDate(doneBy, date);
            return shifts;
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}
