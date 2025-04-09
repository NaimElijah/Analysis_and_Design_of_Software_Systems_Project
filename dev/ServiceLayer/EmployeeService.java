package ServiceLayer;

import DomainLayer.AuthorisationController;
import DomainLayer.EmployeeController;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;



public class EmployeeService {
    private final EmployeeController employeeController;
    private final AuthorisationController authorisationController;


    public EmployeeService(EmployeeController employeeController , AuthorisationController authorisationController) {
        this.employeeController = employeeController;
        this.authorisationController = authorisationController;
    }

    // ========================
    // Get All methods for PL
    // ========================
    public EmployeeSL[] getAllEmployees() {
        try {
            EmployeeSL[] employees = new EmployeeSL[employeeController.getAllEmployees().size()];
            int i = 0;
            for (DomainLayer.Employee employee : employeeController.getAllEmployees().values()) {
                employees[i] = new EmployeeSL(employee);
                i++;
            }
            return employees;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getAllRoles() {
        try {
            String[] roles = new String[authorisationController.getAllRoles().size()];
            int i = 0;
            for (String role : authorisationController.getAllRoles().keySet()) {
                roles[i] = role;
                i++;
            }
            return roles;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public String[] getAllPermissions() {
        try {
            String[] permissions = new String[authorisationController.getAllPermissions().size()];
            int i = 0;
            for (String permission : authorisationController.getAllPermissions()) {
                permissions[i] = permission;
                i++;
            }
            return permissions;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, HashSet<String>> getRoleDetails(String roleName) {
        try {
            Map<String, HashSet<String>> roleDetails = authorisationController.getRoleDetails(roleName);
            if (roleDetails != null) {
                return roleDetails;
            } else {
                throw new RuntimeException("Role not found");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================
    // Employee related methods
    // ========================

    /**
     * Creates a new employee.
     * NOTE: CreateEmployee with NO roles or permissions need to be added to the employee in another action!
     *
     * @param doneBy         The ID of the user who is creating the employee.
     * @param israeliId      The Israeli ID of the employee.
     * @param firstName      The first name of the employee.
     * @param lastName       The last name of the employee.
     * @param salary         The salary of the employee.
     * @param termsOfEmployment The terms of employment for the employee.
     * @param startOfEmployment The start date of employment for the employee.
     * @return A message indicating whether the employee was created successfully or not.
     */
    public String createEmployee(long doneBy , long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, LocalDate startOfEmployment){
        try {
            // Call the createEmployee method in EmployeeController
            boolean result = employeeController.createEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, null, startOfEmployment);
            if (result) {
                return "Employee created successfully"; // Employee created successfully
            } else {
                return "Failed to create employee"; // Failed to create employee
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Updates an existing employee.
     *
     * @param doneBy         The ID of the user who is updating the employee.
     * @param israeliId      The Israeli ID of the employee.
     * @param firstName      The new first name of the employee.
     * @param lastName       The new last name of the employee.
     * @param salary         The new salary of the employee.
     * @param termsOfEmployment The new terms of employment for the employee.
     * @return A message indicating whether the employee was updated successfully or not.
     */
    public String updateEmployee(long doneBy , long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, boolean active) {
        try {
            boolean result = employeeController.updateEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, active);
            if (result) {
                return "Employee updated successfully";
            } else {
                return "Failed to update employee";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String deactivateEmployee(long doneBy , long israeliId) {
        try {
            boolean result = employeeController.deactivateEmployee(doneBy, israeliId);
            if (result) {
                return "Employee deactivated successfully";
            } else {
                return "Failed to deactivate employee";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // ====================
    // Role related methods
    // ====================
    /**
     * Creates a new role.
     *
     * @param doneBy         The ID of the user who is creating the role.
     * @param roleName       The name of the role to be created.
     * @return A message indicating whether the role was created successfully or not.
     */
    public String CreateRole(long doneBy, String roleName) {
        try {
            // TODO: Move the validation to the controller
            String PERMISSION_REQUIRED = "ROLE_PERMISSION";
            boolean isAuth = employeeController.isEmployeeAuthorised(doneBy, roleName);
            authorisationController.createRole(doneBy,roleName, new HashSet<>());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "Failed to create role";
    }
    /**
     * Adds a role to an employee.
     *
     * @param doneBy         The ID of the user who is adding the role.
     * @param israeliId      The Israeli ID of the employee.
     * @param roleName       The name of the role to be added.
     * @return A message indicating whether the role was added successfully or not.
     */
    public String addRoleToEmployee(long doneBy, long israeliId, String roleName) {
        try {
            // TODO: Move the validation to the controller
            employeeController.addRoleToEmployee(doneBy, israeliId, roleName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "Failed to add role";
    }

    /**
     * Removes a role from an employee.
     *
     * @param doneBy         The ID of the user who is removing the role.
     * @param israeliId      The Israeli ID of the employee.
     * @param roleName       The name of the role to be removed.
     * @return A message indicating whether the role was removed successfully or not.
     */
    public String removeRoleFromEmployee(long doneBy, long israeliId, String roleName) {
        try {
            employeeController.removeRoleFromEmployee(doneBy, israeliId, roleName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "Failed to remove role";
    }

    // ===========================
    // Permission related methods
    // ===========================

    /**
     * Adds a permission to a role.
     *
     * @param doneBy         The ID of the user who is adding the permission.
     * @param roleName       The name of the role to which the permission will be added.
     * @param permissionName  The name of the permission to be added.
     * @return A message indicating whether the permission was added successfully or not.
     */
    public String addPermissionToRole(long doneBy, String roleName, String permissionName) {
        // TODO: Move the validation to the controller
        String PERMISSION_REQUIRED = "ADD_PERMISSION_TO_ROLE";
        try {
            boolean isAuth = employeeController.getEmployeeByIsraeliId(doneBy).getRoles().contains(PERMISSION_REQUIRED);
            if (!isAuth) {
                throw new RuntimeException("Permission denied");
            }
            roleName = roleName.trim();
            authorisationController.addPermissionToRole(roleName, permissionName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "Failed to add permission to role";
    }

    /**
     * Removes a permission from a role.
     *
     * @param doneBy         The ID of the user who is removing the permission.
     * @param roleName       The name of the role from which the permission will be removed.
     * @param permissionName  The name of the permission to be removed.
     * @return A message indicating whether the permission was removed successfully or not.
     */
    public String removePermissionFromRole(long doneBy, String roleName, String permissionName) {
        // TODO: Move the validation to the controller
        String PERMISSION_REQUIRED = "REMOVE_PERMISSION_FROM_ROLE";
        try {
            boolean isAuth = employeeController.getEmployeeByIsraeliId(doneBy).getRoles().contains(PERMISSION_REQUIRED);
            if (!isAuth) {
                throw new RuntimeException("Permission denied");
            }
            authorisationController.removePermissionFromRole(roleName, permissionName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "Failed to remove permission from role";
    }

    public EmployeeSL getEmployeeById(long israeliId) {
        try {
            EmployeeSL employee = new EmployeeSL(employeeController.getEmployeeByIsraeliId(israeliId));
            if (employee.getIsraeliId() != null) {
                return employee;
            } else {
                throw new RuntimeException("Employee not found");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
