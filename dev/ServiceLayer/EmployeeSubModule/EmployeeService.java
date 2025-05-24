package ServiceLayer.EmployeeSubModule;

import DTOs.EmployeeDTO;
import DTOs.RoleDTO;
import DomainLayer.EmployeeSubModule.AuthorisationController;
import DomainLayer.EmployeeSubModule.Branch;
import DomainLayer.EmployeeSubModule.EmployeeController;
import DomainLayer.EmployeeSubModule.Employee;
import DomainLayer.exception.InvalidInputException;
import DomainLayer.exception.UnauthorizedPermissionException;
import ServiceLayer.exception.AuthorizationException;
import ServiceLayer.exception.EmployeeNotFoundException;
import ServiceLayer.exception.ServiceException;
import ServiceLayer.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class EmployeeService {
    private final EmployeeController employeeController;
    private final AuthorisationController authorisationController;


    public EmployeeService(EmployeeController employeeController , AuthorisationController authorisationController) {
        this.employeeController = employeeController;
        this.authorisationController = authorisationController;
    }

    /**
     * Converts a domain Employee object to an EmployeeDTO
     * 
     * @param employee The domain Employee object to convert
     * @return The corresponding EmployeeDTO object
     */
    private EmployeeDTO convertToDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return new EmployeeDTO(
            employee.getIsraeliId(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getSalary(),
            employee.getTermsOfEmployment(),
            employee.getRoles(),
            employee.getStartOfEmployment(),
            employee.isActive(),
            employee.getCreationDate(),
            employee.getUpdateDate(),
            employee.getBranchId()
        );
    }

    /**
     * Converts a role name and permissions to a RoleDTO
     * 
     * @param roleName The name of the role
     * @param permissions The set of permissions for the role
     * @return The corresponding RoleDTO object
     */
    private RoleDTO convertToRoleDTO(String roleName, Set<String> permissions) {
        if (roleName == null) {
            return null;
        }

        return new RoleDTO(roleName, permissions);
    }

    /**
     * Serializes an EmployeeDTO to a string for transfer between layers
     * 
     * @param dto The EmployeeDTO to serialize
     * @return The serialized string representation
     */
    private String serializeEmployeeDTO(EmployeeDTO dto) {
        return dto.serialize();
    }

    /**
     * Deserializes a string to an EmployeeDTO
     * 
     * @param serialized The serialized string representation
     * @return The deserialized EmployeeDTO
     */
    private EmployeeDTO deserializeEmployeeDTO(String serialized) {
        return EmployeeDTO.deserialize(serialized);
    }

    /**
     * Serializes a RoleDTO to a string for transfer between layers
     * 
     * @param dto The RoleDTO to serialize
     * @return The serialized string representation
     */
    private String serializeRoleDTO(RoleDTO dto) {
        return dto.serialize();
    }

    /**
     * Deserializes a string to a RoleDTO
     * 
     * @param serialized The serialized string representation
     * @return The deserialized RoleDTO
     */
    private RoleDTO deserializeRoleDTO(String serialized) {
        return RoleDTO.deserialize(serialized);
    }

    /**
     * Checks if an employee is authorized to perform an action.
     *
     * @param israeliId - The Israeli ID of the employee to check
     * @param permission - The permission to check for
     * @return True if the employee is authorized, false otherwise
     * @throws ServiceException if an error occurs during authorization check
     */
    public boolean isEmployeeAuthorised(long israeliId, String permission) {
        try {
            return employeeController.isEmployeeAuthorised(israeliId, permission);
        } catch (UnauthorizedPermissionException e) {
            throw new AuthorizationException(israeliId, permission);
        } catch (Exception e) {
            throw new ServiceException("Error checking authorization: " + e.getMessage(), e);
        }
    }

    // ========================
    // Get All methods for PL
    // ========================
    /**
     * Gets all employees in the system.
     *
     * @return An array of serialized EmployeeDTO objects
     * @throws ServiceException if an error occurs while retrieving employees
     */
    public String[] getAllEmployees() {
        try {
            Map<Long, Employee> employeeMap = employeeController.getAllEmployees();
            String[] serializedEmployees = new String[employeeMap.size()];
            int i = 0;
            for (Employee employee : employeeMap.values()) {
                EmployeeDTO dto = convertToDTO(employee);
                serializedEmployees[i] = serializeEmployeeDTO(dto);
                i++;
            }
            return serializedEmployees;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all employees: " + e.getMessage(), e);
        }
    }

    /**
     * Gets all employees in the system as DTO objects.
     *
     * @return An array of EmployeeDTO objects
     * @throws ServiceException if an error occurs while retrieving employees
     */
    public EmployeeDTO[] getAllEmployeesAsDTO() {
        try {
            Map<Long, Employee> employeeMap = employeeController.getAllEmployees();
            EmployeeDTO[] employees = new EmployeeDTO[employeeMap.size()];
            int i = 0;
            for (Employee employee : employeeMap.values()) {
                employees[i] = convertToDTO(employee);
                i++;
            }
            return employees;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all employees: " + e.getMessage(), e);
        }
    }

    /**
     * Gets all roles in the system.
     *
     * @return An array of serialized RoleDTO objects
     * @throws ServiceException if an error occurs while retrieving roles
     */
    public String[] getAllRoles() {
        try {
            Map<String, String[]> rolesMap = authorisationController.getAllRolesWithPermissions();
            String[] serializedRoles = new String[rolesMap.size()];
            int i = 0;
            for (Map.Entry<String, String[]> entry : rolesMap.entrySet()) {
                String roleName = entry.getKey();
                Set<String> permissions = new HashSet<>();
                for (String permission : entry.getValue()) {
                    permissions.add(permission);
                }
                RoleDTO dto = convertToRoleDTO(roleName, permissions);
                serializedRoles[i] = serializeRoleDTO(dto);
                i++;
            }
            return serializedRoles;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all roles: " + e.getMessage(), e);
        }
    }

    /**
     * Gets all roles in the system as DTO objects.
     *
     * @return An array of RoleDTO objects
     * @throws ServiceException if an error occurs while retrieving roles
     */
    public RoleDTO[] getAllRolesAsDTO() {
        try {
            Map<String, String[]> rolesMap = authorisationController.getAllRolesWithPermissions();
            RoleDTO[] roles = new RoleDTO[rolesMap.size()];
            int i = 0;
            for (Map.Entry<String, String[]> entry : rolesMap.entrySet()) {
                String roleName = entry.getKey();
                Set<String> permissions = new HashSet<>();
                for (String permission : entry.getValue()) {
                    permissions.add(permission);
                }
                roles[i] = convertToRoleDTO(roleName, permissions);
                i++;
            }
            return roles;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all roles: " + e.getMessage(), e);
        }
    }

    /**
     * Gets all permissions in the system.
     *
     * @return An array of all permission names
     * @throws ServiceException if an error occurs while retrieving permissions
     */
    public String[] getAllPermissions() {
        try {
            Set<String> permissionsSet = authorisationController.getAllPermissions();
            String[] permissions = new String[permissionsSet.size()];
            int i = 0;
            for (String permission : permissionsSet) {
                permissions[i] = permission;
                i++;
            }
            return permissions;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all permissions: " + e.getMessage(), e);
        }
    }

    /**
     * Gets details of a specific role, including its permissions.
     *
     * @param roleName The name of the role to get details for
     * @return A serialized RoleDTO object
     * @throws ValidationException if the role name is invalid
     * @throws ServiceException if an error occurs while retrieving role details
     */
    public String getRoleDetails(String roleName) {
        try {
            // Validate input
            if (roleName == null || roleName.trim().isEmpty()) {
                throw new ValidationException("roleName", "Role name cannot be null or empty");
            }

            Map<String, HashSet<String>> roleDetails = authorisationController.getRoleDetails(roleName);
            if (roleDetails != null && !roleDetails.isEmpty()) {
                HashSet<String> permissions = roleDetails.get("permissions");
                RoleDTO dto = convertToRoleDTO(roleName, permissions);
                return serializeRoleDTO(dto);
            } else {
                throw new ValidationException("Role not found: " + roleName);
            }
        } catch (ValidationException e) {
            throw e; // Rethrow validation exceptions
        } catch (InvalidInputException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Error retrieving role details: " + e.getMessage(), e);
        }
    }

    /**
     * Gets details of a specific role as a DTO object.
     *
     * @param roleName The name of the role to get details for
     * @return A RoleDTO object
     * @throws ValidationException if the role name is invalid
     * @throws ServiceException if an error occurs while retrieving role details
     */
    public RoleDTO getRoleDetailsAsDTO(String roleName) {
        try {
            // Validate input
            if (roleName == null || roleName.trim().isEmpty()) {
                throw new ValidationException("roleName", "Role name cannot be null or empty");
            }

            Map<String, HashSet<String>> roleDetails = authorisationController.getRoleDetails(roleName);
            if (roleDetails != null && !roleDetails.isEmpty()) {
                HashSet<String> permissions = roleDetails.get(roleName);
                return convertToRoleDTO(roleName, permissions);
            } else {
                throw new ValidationException("Role not found: " + roleName);
            }
        } catch (ValidationException e) {
            throw e; // Rethrow validation exceptions
        } catch (InvalidInputException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Error retrieving role details: " + e.getMessage(), e);
        }
    }
    public RoleDTO getRoleDTO(String roleName) {
        try {
            return authorisationController.getRoleDTO(roleName);
        } catch (ValidationException e) {
            throw e; // Rethrow validation exceptions
        } catch (InvalidInputException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Error retrieving role details: " + e.getMessage(), e);
        }
    }

    // ========================
    // Employee related methods
    // ========================

    /**
     * Gets an employee by their Israeli ID.
     *
     * @param israeliId The Israeli ID of the employee to retrieve
     * @return A serialized EmployeeDTO object
     * @throws EmployeeNotFoundException if the employee with the given ID doesn't exist
     * @throws ServiceException if an error occurs while retrieving the employee
     */
    public String getEmployeeById(long israeliId) {
        try {
            // Validate input
            if (String.valueOf(israeliId).length() != 9) {
                throw new ValidationException("israeliId", "Israeli ID must be 9 digits");
            }

            Employee employee = employeeController.getEmployeeByIsraeliId(israeliId);
            if (employee == null) {
                throw new EmployeeNotFoundException(israeliId);
            }

            EmployeeDTO dto = convertToDTO(employee);
            return serializeEmployeeDTO(dto);
        } catch (ValidationException | EmployeeNotFoundException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error retrieving employee: " + e.getMessage(), e);
        }
    }

    /**
     * Gets an employee by their Israeli ID as a DTO object.
     *
     * @param israeliId The Israeli ID of the employee to retrieve
     * @return An EmployeeDTO object
     * @throws EmployeeNotFoundException if the employee with the given ID doesn't exist
     * @throws ServiceException if an error occurs while retrieving the employee
     */
    public EmployeeDTO getEmployeeByIdAsDTO(long israeliId) {
        try {
            // Validate input
            if (String.valueOf(israeliId).length() != 9) {
                throw new ValidationException("israeliId", "Israeli ID must be 9 digits");
            }

            Employee employee = employeeController.getEmployeeByIsraeliId(israeliId);
            if (employee == null) {
                throw new EmployeeNotFoundException(israeliId);
            }

            return convertToDTO(employee);
        } catch (ValidationException | EmployeeNotFoundException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error retrieving employee: " + e.getMessage(), e);
        }
    }

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
     * @param branchId         The branch that the employee is assigned to.
     * @return A message indicating whether the employee was created successfully or not.
     * @throws ValidationException if any input parameters are invalid
     * @throws AuthorizationException if the user doesn't have permission to create employees
     * @throws ServiceException if an unexpected error occurs
     */
    public String createEmployee(long doneBy, long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, LocalDate startOfEmployment, Long branchId) {
        try {
            boolean result = employeeController.createEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, null, startOfEmployment, branchId);

            if (result) {
                return "Employee created successfully"; // Employee created successfully
            } else {
                return "Failed to create employee"; // Failed to create employee
            }
        } catch (UnauthorizedPermissionException e) {
            throw new AuthorizationException(doneBy, "CREATE_EMPLOYEE");
        } catch (InvalidInputException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ValidationException | AuthorizationException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error creating employee: " + e.getMessage(), e);
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
     * @param active         Whether the employee is active or not.
     * @param branch         The new branch that the employee is assigned to.
     * @return A message indicating whether the employee was updated successfully or not.
     * @throws ValidationException if any input parameters are invalid
     * @throws EmployeeNotFoundException if the employee with the given ID doesn't exist
     * @throws AuthorizationException if the user doesn't have permission to update employees
     * @throws ServiceException if an unexpected error occurs
     */
    public String updateEmployee(long doneBy, long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, boolean active, String branch) {
        try {

            // Check if employee exists
            Employee employee = employeeController.getEmployeeByIsraeliId(israeliId);
            if (employee == null) {
                throw new EmployeeNotFoundException(israeliId);
            }

            boolean result = employeeController.updateEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, active);
            if (result) {
                return "Employee updated successfully";
            } else {
                return "Failed to update employee";
            }
        } catch (UnauthorizedPermissionException e) {
            throw new AuthorizationException(doneBy, "UPDATE_EMPLOYEE");
        } catch (InvalidInputException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ValidationException | EmployeeNotFoundException | AuthorizationException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error updating employee: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing employee without changing the branch.
     *
     * @param doneBy         The ID of the user who is updating the employee.
     * @param israeliId      The Israeli ID of the employee.
     * @param firstName      The new first name of the employee.
     * @param lastName       The new last name of the employee.
     * @param salary         The new salary of the employee.
     * @param termsOfEmployment The new terms of employment for the employee.
     * @param active         Whether the employee is active or not.
     * @return A message indicating whether the employee was updated successfully or not.
     * @throws ValidationException if any input parameters are invalid
     * @throws EmployeeNotFoundException if the employee with the given ID doesn't exist
     * @throws AuthorizationException if the user doesn't have permission to update employees
     * @throws ServiceException if an unexpected error occurs
     */
    public String updateEmployee(long doneBy, long israeliId, String firstName, String lastName, long salary, Map<String, Object> termsOfEmployment, boolean active) {
        try {
            // Check if employee exists
            Employee employee = employeeController.getEmployeeByIsraeliId(israeliId);
            if (employee == null) {
                throw new EmployeeNotFoundException(israeliId);
            }

            // Keep the existing branch
            long branch = employee.getBranchId();

            return updateEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, active);
        } catch (ValidationException | EmployeeNotFoundException | AuthorizationException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error updating employee: " + e.getMessage(), e);
        }
    }

    /**
     * Deactivates an employee.
     *
     * @param doneBy    The ID of the user who is deactivating the employee.
     * @param israeliId The Israeli ID of the employee to deactivate.
     * @return A message indicating whether the employee was deactivated successfully or not.
     * @throws ValidationException if any input parameters are invalid
     * @throws EmployeeNotFoundException if the employee with the given ID doesn't exist
     * @throws AuthorizationException if the user doesn't have permission to deactivate employees
     * @throws ServiceException if an unexpected error occurs
     */
    public String deactivateEmployee(long doneBy, long israeliId) {
        try {
            // Validate input parameters
            if (String.valueOf(israeliId).length() != 9) {
                throw new ValidationException("israeliId", "Israeli ID must be 9 digits");
            }

            // Check if employee exists
            Employee employee = employeeController.getEmployeeByIsraeliId(israeliId);
            if (employee == null) {
                throw new EmployeeNotFoundException(israeliId);
            }

            boolean result = employeeController.deactivateEmployee(doneBy, israeliId);
            if (result) {
                return "Employee deactivated successfully";
            } else {
                return "Failed to deactivate employee";
            }
        } catch (UnauthorizedPermissionException e) {
            throw new AuthorizationException(doneBy, "DEACTIVATE_EMPLOYEE");
        } catch (InvalidInputException e) {
            throw new ValidationException(e.getMessage(), e);
        } catch (ValidationException | EmployeeNotFoundException | AuthorizationException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error deactivating employee: " + e.getMessage(), e);
        }
    }

    public String getEmployeeFullNameByID(long israeliId) {
        try {
            // Validate input parameters
            if (String.valueOf(israeliId).length() != 9) {
                throw new ValidationException("israeliId", "Israeli ID must be 9 digits");
            }

            // Check if employee exists
            Employee employee = employeeController.getEmployeeByIsraeliId(israeliId);
            if (employee == null) {
                throw new EmployeeNotFoundException(israeliId);
            }

            return employee.getFullName();
        } catch (ValidationException | EmployeeNotFoundException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error retrieving employee full name: " + e.getMessage(), e);
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
    public String createRole(long doneBy, String roleName) {
        try {
            // Check if user has permission to create roles
            String PERMISSION_REQUIRED = "CREATE_ROLE";
            boolean isAuth = employeeController.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED);
            if (!isAuth) {
                return "Permission denied: Cannot create roles";
            }

            // Create role with empty permissions set
            boolean success = authorisationController.createRole(doneBy, roleName, new HashSet<>());

            if (success) {
                return "Role '" + roleName + "' created successfully";
            } else {
                return "Failed to create role: Role may already exist";
            }
        } catch (RuntimeException e) {
            return "Error creating role: " + e.getMessage();
        }
    }

    /**
     * Creates a new role with specified permissions.
     *
     * @param doneBy      The ID of the user who is creating the role
     * @param roleName    The name of the role to be created
     * @param permissions Set of permission names to assign to the role
     * @return A message indicating whether the role was created successfully
     */
    public String createRoleWithPermissions(long doneBy, String roleName, Set<String> permissions) {
        try {
            // Check if user has permission to create roles
            String PERMISSION_REQUIRED = "CREATE_ROLE";
            boolean isAuth = employeeController.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED);
            if (!isAuth) {
                return "Permission denied: Cannot create roles";
            }

            // Validation is now handled in the domain layer
            boolean success = authorisationController.createRole(doneBy, roleName, permissions);

            if (success) {
                return "Role '" + roleName + "' created successfully with " + permissions.size() + " permissions";
            } else {
                return "Failed to create role: Role may already exist";
            }
        } catch (UnauthorizedPermissionException e) {
            return "Permission denied: " + e.getMessage();
        } catch (InvalidInputException e) {
            return "Error creating role: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
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
            // Validation is now handled in the domain layer
            boolean success = employeeController.addRoleToEmployee(doneBy, israeliId, roleName);
            if (success) {
                return "Role '" + roleName + "' added successfully to employee with ID " + israeliId;
            } else {
                return "Failed to add role";
            }
        } catch (UnauthorizedPermissionException e) {
            return "Permission denied: " + e.getMessage();
        } catch (InvalidInputException e) {
            return "Error adding role: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
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
            // Validation is now handled in the domain layer
            boolean success = employeeController.removeRoleFromEmployee(doneBy, israeliId, roleName);
            if (success) {
                return "Role '" + roleName + "' removed successfully from employee with ID " + israeliId;
            } else {
                return "Failed to remove role";
            }
        } catch (UnauthorizedPermissionException e) {
            return "Permission denied: " + e.getMessage();
        } catch (InvalidInputException e) {
            return "Error removing role: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
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
        String PERMISSION_REQUIRED = "ADD_PERMISSION_TO_ROLE";
        try {
            // Check authorization
            boolean isAuth = employeeController.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED);
            if (!isAuth) {
                return "Permission denied: Cannot add permissions to roles";
            }

            // Validation is now handled in the domain layer
            boolean success = authorisationController.addPermissionToRole(roleName, permissionName);

            if (success) {
                return "Permission '" + permissionName + "' added successfully to role '" + roleName + "'";
            } else {
                return "Failed to add permission: Permission is already assigned to this role";
            }
        } catch (InvalidInputException e) {
            return "Error adding permission: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
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
        String PERMISSION_REQUIRED = "REMOVE_PERMISSION_FROM_ROLE";
        try {
            // Check authorization
            boolean isAuth = employeeController.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED);
            if (!isAuth) {
                return "Permission denied: Cannot remove permissions from roles";
            }

            // Validation is now handled in the domain layer
            boolean success = authorisationController.removePermissionFromRole(roleName, permissionName);

            if (success) {
                return "Permission '" + permissionName + "' removed successfully from role '" + roleName + "'";
            } else {
                return "Failed to remove permission: Permission is not assigned to this role";
            }
        } catch (InvalidInputException e) {
            return "Error removing permission: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    /**
     * Creates a new permission.
     *
     * @param doneBy         The ID of the user who is creating the permission.
     * @param permissionName The name of the new permission.
     * @return A message indicating whether the permission was created successfully.
     */
    public String createPermission(long doneBy, String permissionName) {
        try {
            // Check if user has permission to create permissions
            String PERMISSION_REQUIRED = "CREATE_PERMISSION";
            boolean isAuth = employeeController.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED);
            if (!isAuth) {
                return "Permission denied: Cannot create permissions";
            }

            // Validation is now handled in the domain layer
            boolean success = authorisationController.createPermission(permissionName);

            if (success) {
                return "Permission '" + permissionName + "' created successfully";
            } else {
                return "Failed to create permission: Permission may already exist";
            }
        } catch (UnauthorizedPermissionException e) {
            return "Permission denied: " + e.getMessage();
        } catch (InvalidInputException e) {
            return "Error creating permission: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    /**
     * Clones an existing role to create a new role with the same permissions.
     *
     * @param doneBy          The ID of the user who is cloning the role
     * @param existingRoleName The name of the role to clone
     * @param newRoleName     The name of the new role to create
     * @return A message indicating whether the role was cloned successfully
     */
    public String cloneRole(long doneBy, String existingRoleName, String newRoleName) {
        try {
            // Check if user has permission to create roles
            String PERMISSION_REQUIRED = "CREATE_ROLE";
            boolean isAuth = employeeController.isEmployeeAuthorised(doneBy, PERMISSION_REQUIRED);
            if (!isAuth) {
                return "Permission denied: Cannot create roles";
            }

            // Get existing role's permissions
            Map<String, HashSet<String>> roleDetails = authorisationController.getRoleDetails(existingRoleName);
            if (roleDetails.isEmpty()) {
                return "Source role not found: " + existingRoleName;
            }

            HashSet<String> permissions = roleDetails.get(existingRoleName);

            // Create new role with same permissions
            // Validation of the new role name and permissions is handled in the domain layer
            boolean success = authorisationController.createRole(doneBy, newRoleName, permissions);

            if (success) {
                return "Role '" + newRoleName + "' cloned successfully from '" + existingRoleName + "'";
            } else {
                return "Failed to clone role: Target role may already exist";
            }
        } catch (UnauthorizedPermissionException e) {
            return "Permission denied: " + e.getMessage();
        } catch (InvalidInputException e) {
            return "Error cloning role: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
    }
    public String updateEmployeeBranch(long israeliId, long branchId) {
        try {
            // Validate input parameters
            if (String.valueOf(israeliId).length() != 9) {
                throw new ValidationException("israeliId", "Israeli ID must be 9 digits");
            }

            // Check if employee exists
            Employee employee = employeeController.getEmployeeByIsraeliId(israeliId);
            if (employee == null) {
                throw new EmployeeNotFoundException(israeliId);
            }

            boolean result = employeeController.updateEmployeeBranch(israeliId, branchId);
            if (result) {
                return "Success Employee branch updated successfully";
            } else {
                return "Failed to update employee branch";
            }
        } catch (ValidationException | EmployeeNotFoundException e) {
            throw e; // Rethrow specific exceptions
        } catch (Exception e) {
            throw new ServiceException("Error updating employee branch: " + e.getMessage(), e);
        }
    }
    public String getEmployeeBranchName(long israeliId) {
        try {
            return employeeController.getEmployeeBranchName(israeliId);
        } catch (Exception e) {
            throw new ServiceException("Error retrieving employee branch name: " + e.getMessage(), e);
        }
    }


    // ===========================
    // Functions for integration with Transport module
    // ===========================

    /**
     * Checks if the employee with the given Israeli ID has the specified permission.
     *
     * @param israeliId The unique identifier of the employee.
     * @param permission The permission to check for the specified employee.
     * @return true if the employee has the given permission, false otherwise.
     * @throws AuthorizationException If there is an issue with authorization.
     * @throws ServiceException If a general error occurs while checking permissions.
     */
    public boolean hasPermission(long israeliId, String permission) {
        try {
            return employeeController.hasPermission(israeliId, permission);
        } catch (UnauthorizedPermissionException e) {
            throw new AuthorizationException(israeliId, permission);
        } catch (Exception e) {
            throw new ServiceException("Error checking authorization: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if the employee with the given ID is currently active.
     *
     * @param employeeId The unique identifier of the employee to be checked.
     * @return true if the employee is active; false otherwise.
     * @throws ServiceException if there is an error while checking the employee's status.
     */
    public boolean isEmployeeActive(long employeeId) {
        try{
            return employeeController.isEmployeeActive(employeeId);
        } catch (Exception e) {
            throw new ServiceException("Error checking employee status: " + e.getMessage(), e);
        }
    }
    /**
     * Checks if an employee, identified by their Israeli ID, has a specified role.
     *
     * @param israeliId the unique Israeli ID of the employee
     * @param roleName the name of the role to check for the employee
     * @return true if the employee has the specified role, otherwise false
     * @throws ServiceException if an error occurs during the check
     */
    public boolean isEmployeeHaveRole(long israeliId, String roleName) {
        try{
            return employeeController.isEmployeeHaveRole(israeliId, roleName);
        } catch (Exception e) {
            throw new ServiceException("Error checking employee role: " + e.getMessage(), e);
        }
    }

    public String[] getAllDrivers() {
        try {
            return employeeController.getAllDrivers();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all drivers: " + e.getMessage(), e);
        }
    }

    public boolean isBranchExists(long branch) {
        try {
            return employeeController.isBranchExists(branch);
        } catch (Exception e) {
            throw new ServiceException("Error checking if branch exists: " + e.getMessage(), e);
        }
    }

    public boolean isBranch(String address, int areaCode) {
        try {
            return employeeController.isBranch(address, areaCode);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
