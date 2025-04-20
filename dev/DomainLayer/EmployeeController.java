package DomainLayer;

import DomainLayer.exception.InvalidInputException;
import DomainLayer.exception.UnauthorizedPermissionException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmployeeController {
    private final Set<Employee> employees;
    private final AuthorisationController authorisationController;

    public EmployeeController(Set<Employee> employees , AuthorisationController authorisationController) {
        this.employees = new HashSet<>(employees);
        this.authorisationController = authorisationController;
    }

    /**
     * Retrieves an employee by their Israeli ID.
     * @param israeliId - The Israeli ID of the employee to retrieve
     * @return The employee with the given Israeli ID, or null if not found
     */
    public Employee getEmployeeByIsraeliId(long israeliId) {
        return employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
    }

    /**
     * Creates a new employee.
     *
     * @param doneBy - The user who created the employee - for auditing purposes and permissions
     * @param israeliId - The Israeli ID of the new employee
     * @param firstName - The first name of the new employee
     * @param lastName - The last name of the new employee
     * @param salary - The salary of the new employee
     * @param termsOfEmployment - The terms of employment of the new employee
     * @param roles - The roles of the new employee
     * @param startOfEmployment - The start of employment date of the new employee
     * @return True if the employee was created successfully
     * @throws UnauthorizedPermissionException if the user does not have permission
     * @throws InvalidInputException if any input is invalid
     */
    public boolean createEmployee(long doneBy, long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, Set<String> roles, LocalDate startOfEmployment) {
        // Permission handling
        String PERMISSION_REQUIRED = "CREATE_EMPLOYEE";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to create employee");
        }

        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new InvalidInputException("Employee with ID " + doneBy + " not found");
        }

        // Check if employee already exists
        if (employees.stream().anyMatch(e -> e.getIsraeliId() == israeliId)) {
            throw new InvalidInputException("Employee with ID " + israeliId + " already exists");
        }

        // Validate input
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidInputException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidInputException("Last name cannot be null or empty");
        }
        if (salary <= 0) {
            throw new InvalidInputException("Salary must be greater than zero");
        }
        if (termsOfEmployment == null) {
            throw new InvalidInputException("Terms of employment cannot be null");
        }
        if (startOfEmployment == null) {
            throw new InvalidInputException("Start of employment date cannot be null");
        }
        if (String.valueOf(israeliId).length() != 9) {
            throw new InvalidInputException("Israeli ID must be 9 digits");
        }
        if (roles == null) {
            throw new InvalidInputException("Roles cannot be null");
        }

        // Create new employee
        Employee newEmployee = new Employee(israeliId, firstName, lastName, salary, termsOfEmployment, roles, startOfEmployment, true, LocalDate.now(), LocalDate.now());
        employees.add(newEmployee);
        return true;
    }

    /**
     * Updates an existing employee.
     *
     * @param doneBy - The user who updated the employee - for auditing purposes and permissions
     * @param israeliId - The Israeli ID of the employee to update
     * @param firstName - The new first name
     * @param lastName - The new last name
     * @param salary - The new salary
     * @param termsOfEmployment - The new terms of employment
     * @param active - The new active status
     * @return True if the employee was updated successfully
     * @throws UnauthorizedPermissionException if the user does not have permission
     * @throws InvalidInputException if any input is invalid or if the employee does not exist
     */
    public boolean updateEmployee(long doneBy, long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, boolean active) {
        // Permission handling
        String PERMISSION_REQUIRED = "UPDATE_EMPLOYEE";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to update employee");
        }

        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new InvalidInputException("Employee with ID " + doneBy + " not found");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new InvalidInputException("Employee with ID " + israeliId + " not found");
        }

        // Check if employee is active
        if (!employee.isActive()) {
            throw new InvalidInputException("Employee with ID " + israeliId + " is not active - cannot update information");
        }

        // Validate input
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidInputException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidInputException("Last name cannot be null or empty");
        }
        if (salary <= 0) {
            throw new InvalidInputException("Salary must be greater than zero");
        }
        if (termsOfEmployment == null) {
            throw new InvalidInputException("Terms of employment cannot be null");
        }
        if (String.valueOf(israeliId).length() != 9) {
            throw new InvalidInputException("Israeli ID must be 9 digits");
        }

        // Update employee details
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setSalary(salary);
        employee.setTermsOfEmployment(termsOfEmployment);
        employee.setActive(active);
        employee.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * Deletes an existing employee.
     *
     * @param doneBy - The user who deleted the employee - for auditing purposes and permissions
     * @param israeliId - The Israeli ID of the employee to delete
     * @return True if the employee was deleted successfully
     * @throws UnauthorizedPermissionException if the user does not have permission
     * @throws InvalidInputException if the employee does not exist or is not active
     */
    public boolean deleteEmployee(long doneBy, long israeliId) {
        // Check if the user has permission to delete an employee
        String PERMISSION_REQUIRED = "DELETE_EMPLOYEE";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to delete employee");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new InvalidInputException("Employee with ID " + israeliId + " not found");
        }

        // Check if employee is active
        if (!employee.isActive()) {
            throw new InvalidInputException("Employee with ID " + israeliId + " is not active - cannot delete");
        }

        // Delete employee
        employees.remove(employee);
        return true;
    }

    /**
     * Checks if an employee is authorized to perform an action.
     *
     * @param israeliId - The Israeli ID of the employee to check
     * @param permission - The permission to check for
     * @return True if the employee is authorized, false otherwise
     * @throws InvalidInputException if the employee does not exist
     * @throws UnauthorizedPermissionException if the employee does not have the permission
     */
    public boolean isEmployeeAuthorised(long israeliId, String permission) {
        // Check if employee exists
        Employee employee = getEmployeeByIsraeliId(israeliId);
        if (employee == null) {
            throw new InvalidInputException("Employee with ID " + israeliId + " not found");
        }

        // Check if employee has the required permission
        return authorisationController.hasPermission(employee, permission);
    }

    /**
     * Adds a role to an employee.
     *
     * @param doneBy - The user who is adding the role - for auditing purposes and permissions
     * @param israeliId - The Israeli ID of the employee to add the role to
     * @param roleName - The name of the role to add
     * @return True if the role was added successfully
     * @throws UnauthorizedPermissionException if the user does not have permission
     * @throws InvalidInputException if any input is invalid or if the employee already has the role
     */
    public boolean addRoleToEmployee(long doneBy, long israeliId, String roleName) {
        // Permission handling
        String PERMISSION_REQUIRED = "ROLE_PERMISSION";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to add role to employee");
        }

        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new InvalidInputException("Employee with ID " + doneBy + " not found");
        }

        // Validate input
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new InvalidInputException("Role name cannot be null or empty");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new InvalidInputException("Employee with ID " + israeliId + " not found");
        }

        // Check if role exists
        if (!authorisationController.isRoleExists(roleName)) {
            throw new InvalidInputException("Role '" + roleName + "' does not exist");
        }

        // Check if employee already has the role
        if (employee.getRoles().contains(roleName)) {
            throw new InvalidInputException("Employee with ID " + israeliId + " already has role '" + roleName + "'");
        }

        // Add role to employee
        employee.getRoles().add(roleName);
        employee.setUpdateDate(LocalDate.now());
        return true;
    }
    /**
     * Removes a role from an employee.
     *
     * @param doneBy - The user who is removing the role - for auditing purposes and permissions
     * @param israeliId - The Israeli ID of the employee to remove the role from
     * @param roleName - The name of the role to remove
     * @return True if the role was removed successfully
     * @throws UnauthorizedPermissionException if the user does not have permission
     * @throws InvalidInputException if any input is invalid
     */
    public boolean removeRoleFromEmployee(long doneBy, long israeliId, String roleName) {
        // Permission handling
        String PERMISSION_REQUIRED = "ROLE_PERMISSION";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to remove role from employee");
        }

        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new InvalidInputException("Employee with ID " + doneBy + " not found");
        }

        // Validate input
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new InvalidInputException("Role name cannot be null or empty");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new InvalidInputException("Employee with ID " + israeliId + " not found");
        }

        // Check if role exists
        if (!authorisationController.isRoleExists(roleName)) {
            throw new InvalidInputException("Role '" + roleName + "' does not exist");
        }

        // Check if employee has the role
        if (!employee.getRoles().contains(roleName)) {
            throw new InvalidInputException("Employee with ID " + israeliId + " does not have role '" + roleName + "'");
        }

        // Remove role from employee
        employee.getRoles().remove(roleName);
        employee.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * Deactivates an employee.
     *
     * @param doneBy - The user who is deactivating the employee - for auditing purposes and permissions
     * @param israeliId - The Israeli ID of the employee to deactivate
     * @return True if the employee was deactivated successfully
     * @throws UnauthorizedPermissionException if the user does not have permission
     * @throws InvalidInputException if the employee does not exist
     */
    public boolean deactivateEmployee(long doneBy, long israeliId) {
        // Permission handling
        String PERMISSION_REQUIRED = "EDIT_EMPLOYEE";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new UnauthorizedPermissionException("User does not have permission to deactivate employee");
        }

        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new InvalidInputException("Employee with ID " + doneBy + " not found");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new InvalidInputException("Employee with ID " + israeliId + " not found");
        }

        // Check if employee is already inactive
        if (!employee.isActive()) {
            throw new InvalidInputException("Employee with ID " + israeliId + " is already inactive");
        }

        // Deactivate employee
        employee.setActive(false);
        employee.setUpdateDate(LocalDate.now());
        return true;
    }

    /**
     * Gets all employees in the system.
     *
     * @return A map of all employees, with Israeli ID as the key and Employee object as the value
     */
    public Map<Long, Employee> getAllEmployees() {
        Map<Long, Employee> employeesMap = new HashMap<>();
        for (Employee employee : employees) {
            employeesMap.put(employee.getIsraeliId(), employee);
        }
        return employeesMap;
    }
}
