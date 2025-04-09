package DomainLayer;

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
     * @param israeliId
     * @param firstName
     * @param lastName
     * @param salary
     * @param termsOfEmployment
     * @param roles
     * @param startOfEmployment
     * @return True if the employee was created successfully, false if the employee already exists
     */
    public boolean createEmployee(long doneBy ,long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, Set<String> roles, LocalDate startOfEmployment) {
        // Permission handling
        String PERMISSION_REQUIRED = "CREATE_EMPLOYEE";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new RuntimeException("User does not have permission to deactivate employee"); // User does not have permission
        }
        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new RuntimeException("Employee not found");
        }
        // Check if employee already exists
        if (employees.stream().anyMatch(e -> e.getIsraeliId() == israeliId)) {
            throw new RuntimeException("Employee already exists"); // Employee already exists
        }

        // Validate input
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            throw new RuntimeException("First name and last name are required"); // First name and last name are required
        }
        if (salary <= 0) {
            throw new RuntimeException("Salary must be greater than zero"); // Salary must be greater than zero
        }
        if (termsOfEmployment == null) {
            throw new RuntimeException("Terms of employment are required"); // Terms of employment are required
        }
        if (startOfEmployment == null) {
            throw new RuntimeException("Start of employment date is required"); // Start of employment date is required
        }
        if (String.valueOf(israeliId).length() != 9) {
            throw new RuntimeException("Not Valid Israeli ID number"); // Israeli ID must be 9 digits
        }
//        if (roles == null || roles.isEmpty()) { // Not sure if this is needed
//            throw new RuntimeException("Roles are required"); // Roles are required
//        }
        // Create new employee
        Employee newEmployee = new Employee(israeliId, firstName, lastName, salary, termsOfEmployment, roles, startOfEmployment, true, LocalDate.now(), LocalDate.now());
        employees.add(newEmployee);
        return true;
    }

    /**
     * Updates an existing employee.
     *
     * @param doneBy - The user who created the employee - for auditing purposes and permissions
     * @param israeliId
     * @param firstName
     * @param lastName
     * @param salary
     * @param termsOfEmployment
     * @param active
     * @return True if the employee was updated successfully, false if the employee does not exist
     */
    public boolean updateEmployee(long doneBy, long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, boolean active) {
        // // Permission handling
        String PERMISSION_REQUIRED = "UPDATE_EMPLOYEE";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new RuntimeException("User does not have permission to deactivate employee"); // User does not have permission
        }
        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Check if employee is active
        if (!employee.isActive()) {
            throw new RuntimeException("Employee is not active - cannot change info."); // Employee is not active
        }

        // Validate input
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            throw new RuntimeException("First name and last name are required"); // First name and last name are required
        }
        if (salary <= 0) {
            throw new RuntimeException("Salary must be greater than zero"); // Salary must be greater than zero
        }
        if (termsOfEmployment == null) {
            throw new RuntimeException("Terms of employment are required"); // Terms of employment are required
        }
        if (String.valueOf(israeliId).length() != 9) {
            throw new RuntimeException("Not Valid Israeli ID number"); // Israeli ID must be 9 digits
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
     * @param doneBy - The user who created the employee - for auditing purposes and permissions
     * @param israeliId - The Israeli ID of the employee to delete
     * @return True if the employee was deleted successfully, false if the employee does not exist
     */
    public boolean deleteEmployee(long doneBy, long israeliId) {
        // Check if the user has permission to delete an employee
        String PERMISSION_REQUIRED = "DELETE_EMPLOYEE"; // Permission required to delete an employee
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new RuntimeException("User does not have permission to deactivate employee"); // User does not have permission
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found"); // Employee not found
        }

        // Check if employee is active
        if (!employee.isActive()) {
            throw new RuntimeException("Employee is not active - cannot delete."); // Employee is not active
        }

        // Delete employee
        employees.remove(employee);
        return true;
    }

    public boolean isEmployeeAuthorised(long israeliId, String permission) {
        // Check if employee exists
        Employee employee = getEmployeeByIsraeliId(israeliId);
        if (employee == null) {
            throw new RuntimeException("Employee not found"); // Employee not found
        }

        // Check if employee has the required permission
        return authorisationController.HasPermission(employee, permission);
    }

    public boolean addRoleToEmployee(long doneBy, long israeliId, String roleName) {
        // Permission handling
        String PERMISSION_REQUIRED = "ROLE_PERMISSION";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new RuntimeException("User does not have permission to add role to employee"); // User does not have permission
        }
        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found"); // Employee not found
        }
        // Check if role exists
        if (!authorisationController.isRoleExists(roleName)) {
            throw new RuntimeException("Role not found"); // Role not found
        }
        // Check if employee already has the role
        if (employee.getRoles().stream().anyMatch(r -> r.equals(roleName))) {
            throw new RuntimeException("Employee already has this role"); // Employee already has this role
        }

        // add role to employee
        employee.getRoles().add(roleName);
        return true;
    }
    public boolean removeRoleFromEmployee(long doneBy, long israeliId, String roleName) {
        // Permission handling
        String PERMISSION_REQUIRED = "ROLE_PERMISSION";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new RuntimeException("User does not have permission to add role to employee"); // User does not have permission
        }
        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found"); // Employee not found
        }
        // Check if role exists
        if (!authorisationController.isRoleExists(roleName)) {
            throw new RuntimeException("Role not found"); // Role not found
        }

        // remove role from employee
        employee.getRoles().remove(roleName);
        return true;
    }

    public boolean deactivateEmployee(long doneBy, long israeliId) {
        // Permission handling
        String PERMISSION_REQUIRED = "EDIT_EMPLOYEE";
        if (!isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED)) {
            throw new RuntimeException("User does not have permission to add role to employee"); // User does not have permission
        }
        Employee doneByEmployee = getEmployeeByIsraeliId(doneBy);
        if (doneByEmployee == null) {
            throw new RuntimeException("Employee not found");
        }
        // Check if employee exists
        Employee employee = employees.stream().filter(e -> e.getIsraeliId() == israeliId).findFirst().orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found"); // Employee not found
        }

        // deactivate employee
        employee.setActive(false);
        return true;
    }

    public Map<Long, Employee> getAllEmployees() {
        Map<Long, Employee> employeesMap = new HashMap<Long, Employee>();
        for (Employee employee : employees) {
            employeesMap.put(employee.getIsraeliId(), employee);
        }
        return employeesMap;
    }
}
