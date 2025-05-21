package ServiceLayer;

import ServiceLayer.EmployeeSubModule.EmployeeService;
import ServiceLayer.EmployeeSubModule.ShiftService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * The EmployeeIntegrationService class provides an abstraction layer for integrating and managing
 * operations related to employees and their shifts. This class collaborates with EmployeeService
 * and ShiftService to execute functionality such as checking employee status, roles, and shift assignments.
 */
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


    public boolean isDriverOnShiftAt(LocalDateTime dateTime, String address, int areaCode) {
        // TODO: Add Driver ID to the method signature, func needs to check if his on shift
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        return shiftService.isAssignedRoleByDateTimeBranch(date, time,"Driver", address, areaCode);
    }


    public boolean isWarehousemanOnShiftAt(LocalDateTime dateTime, String address, int areaCode) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        // TODO: upadte this and the above method to use the new branch object
        return shiftService.isAssignedRoleByDateTimeBranch(date, time,"Warehouseman", address, areaCode);
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

    /**
     * Checks if an employee with the given ID is authorized to perform an action with the specified permission.
     *
     * @param doneBy The unique identifier of the employee whose authorization is being checked.
     * @param permission The specific permission to check for the employee.
     * @return true if the employee is authorized to perform the action, false otherwise.
     */
    public boolean isEmployeeAuthorised(long doneBy, String permission) {
        return employeeService.isEmployeeAuthorised(doneBy, permission);
    }

}
