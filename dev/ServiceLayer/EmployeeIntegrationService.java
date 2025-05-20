package ServiceLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EmployeeIntegrationService {
    final private EmployeeService employeeService;
    final private ShiftService shiftService;

    /**
     * Constructs an instance of the EmployeeIntegrationService.
     *
     * @param employeeService An instance of EmployeeService used to handle operations related to employees.
     * @param shiftService An instance of ShiftService used to manage shift-related operations.
     */
    public EmployeeIntegrationService(EmployeeService employeeService, ShiftService shiftService) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
    }

    /**
     * Determines whether an employee with the given ID is currently active.
     *
     * @param employeeId The unique identifier of the employee whose status is to be checked.
     * @return true if the employee is active; false otherwise.
     */
    public boolean isActive(long employeeId) {
        return employeeService.isEmployeeActive(employeeId);
    }

    /**
     * Checks if the employee with the given ID has the specified role.
     *
     * @param employeeId The unique identifier of the employee.
     * @param role The name of the role to check for the employee.
     * @return true if the employee has the specified role; false otherwise.
     */
    public boolean hasRole(long employeeId, String role) {
        return employeeService.isEmployeeHaveRole(employeeId, role);
    }

    /**
     * Determines whether there is a driver assigned to a shift at the specified date, time, and branch location.
     *
     * @param dateTime The date and time to check for a driver's shift assignment.
     * @param branch The branch location for which the driver's shift assignment is being checked.
     * @return true if a driver is assigned to a shift at the specified date, time, and branch; false otherwise.
     */
    public boolean isDriverOnShiftAt(LocalDateTime dateTime, String branch) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        return shiftService.isAssignedRoleByDateTimeBranch(date, time,branch , "Driver");
    }

    /**
     * Determines whether there is a warehouseman assigned to a shift at the specified date, time, and branch location.
     *
     * @param dateTime The date and time to check for the warehouseman's shift assignment.
     * @param branch The branch location for which the warehouseman's shift assignment is being checked.
     * @return true if a warehouseman is assigned to a shift at the specified date, time, and branch; false otherwise.
     */
    public boolean isWarehousemanOnShiftAt(LocalDateTime dateTime, String branch) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        return shiftService.isAssignedRoleByDateTimeBranch(date, time,branch , "Warehouseman");
    }

    /**
     * Retrieves a string representation of all drivers in the system.
     * This method returns a string array containing all drover DTO serialized.
     * To extract the object from the string array, you can use the deserialization method.
     * --> Deserialization method -> EmployeeDTO::deserialize
     *
     * @return A string containing details of all drivers.
     */
    public String[] getAllDrivers() { // NOT TESTED and NOT READY FOR USE!
        return employeeService.getAllDrivers();
    }

}
