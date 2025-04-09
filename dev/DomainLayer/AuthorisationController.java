package DomainLayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthorisationController {

    private final Map<String, HashSet<String>> roles; // Set of all roles
    private final Set<String> permissions; // Set of all permissions

    public AuthorisationController(Map<String, HashSet<String>> roles, Set<String> permissions) {
        this.roles = new HashMap<>(roles);
        this.permissions = new HashSet<>(permissions);
    }

    public boolean isRoleExists(String role) {
        return roles.containsKey(role);
    }

    /**
     * Checks if the employee has the required permission.
     *
     * @param employee           - The employee to check permissions for
     * @param permissionRequired - The permission to check for
     * @return true if the employee has the permission, false otherwise
     */
    public boolean HasPermission(Employee employee, String permissionRequired) {
        boolean has = employee.getRoles()
                .stream()
                .anyMatch(role -> roles.get(role).contains(permissionRequired));
        if (!has) {
            return false; // TODO: throw new UnauthorizedPermission("Employee does not have permission: " + permissionRequired);
        }
        return has;
    }

    /**
     * Checks if the employee has the required role.
     *
     * @param employee     - The employee to check role for
     * @param roleRequired - The role to check for
     * @return true if the employee has the role, false otherwise
     */
    boolean HasRole(Employee employee, String roleRequired) {
        return employee.getRoles().stream().anyMatch(role -> role.equals(roleRequired));
    }

    /**
     * Adds a role to the employee's roles.
     *
     * @param employee - The employee to add the role to
     * @param role     - The role to add
     * @return true if the role was added successfully, false if the role already exists
     */
    boolean AddRole(Employee employee, String role) {
        if (employee.getRoles().contains(role)) {
            return false; // Role already exists
        }
        employee.getRoles().add(role);
        return true;
    }

    /**
     * Removes a role from the employee's roles.
     *
     * @param employee     - The employee to remove the role from
     * @param roleRequired - The role to remove
     * @return true if the role was removed successfully, false if the role does not exist
     */
    public boolean removeRole(Employee employee, String roleRequired) {
        if (!employee.getRoles().contains(roleRequired)) {
            return false; // Role does not exist
        }
        employee.getRoles().remove(roleRequired);
        return true;
    }

    /**
     * Adds a permission to the role's permissions.
     *
     * @param role       - The role to add the permission to
     * @param permission - The permission to add
     * @return true if the permission was added successfully, false if the permission already exists
     */
    public boolean addPermissionToRole(String role, String permission) {
        if (roles.get(role).contains(permission)) {
            return false; // Permission already exists
        }
        roles.get(role).add(permission);
        return true;
    }

    /**
     * Removes a permission from the role's permissions.
     *
     * @param roleName       - The role to remove the permission from
     * @param permissionName - The permission to remove
     * @return true if the permission was removed successfully, false if the permission does not exist
     */
    public boolean removePermissionFromRole(String roleName, String permissionName) {
        if (!roles.get(roleName).contains(permissionName)) {
            return false; // Permission does not exist
        }
        roles.get(roleName).remove(permissionName);
        return true;
    }

    /**
     * Creates a new role with the specified name and permissions.
     *
     * @param roleName    - The name of the new role
     * @param permissions - The permissions to assign to the new role
     * @return true if the role was created successfully, false if the role already exists
     */
    public boolean createRole(long doneBy, String roleName, Set<String> permissions) {
        // Permission check is in the Service layer

        // Validate input
        if (roles.containsKey(roleName)) {
            return false; // Role already exists
        }
        roles.put(roleName.trim(), new HashSet<>(permissions));
        return true;
    }

    /**
     * Deletes a role from the system.
     *
     * @param roleToDelete - The role to delete
     * @return true if the role was deleted successfully, false if the role does not exist
     */
    boolean deleteRole(String roleToDelete) {

        if (!roles.containsKey(roleToDelete)) {
            return false; // Role does not exist
        }
        roles.remove(roleToDelete);
        return true;
    }

    /**
     * Creates a new permission with the specified name and description.
     *
     * @param permissionName - The name of the new permission
     * @return true if the permission was created successfully, false if the permission already exists
     */
    boolean CreatePermission(String permissionName) {
        // Validate input
        if (permissions.stream().anyMatch(permission -> permission.equals(permissionName))) {
            return false; // Permission already exists
        }
        permissionName.trim();
        permissions.add(permissionName);
        return true;
    }

    /**
     * Deletes a permission from the system.
     *
     * @param permission - The permission to delete
     * @return true if the permission was deleted successfully, false if the permission does not exist
     */
    boolean DeletePermission(String permission) {
        if (!permissions.contains(permission)) {
            return false; // Permission does not exist
        }
        permissions.remove(permission);
        return true;
    }

    public Map<String,String[]> getAllRoles() {
        Map<String,String[]> rolesMap = new HashMap<>();
        for (Map.Entry<String, HashSet<String>> entry : roles.entrySet()) {
            String roleName = entry.getKey();
            String[] permissionsArray = entry.getValue().toArray(new String[0]);
            rolesMap.put(roleName, permissionsArray);
        }
        return rolesMap;
    }

    public Set<String> getAllPermissions() {
        return new HashSet<>(permissions);
    }

    public Map<String, HashSet<String>> getRoleDetails(String roleName) {
        Map<String, HashSet<String>> roleDetails = new HashMap<>();
        if (roles.containsKey(roleName)) {
            roleDetails.put(roleName, roles.get(roleName));
        }
        return roleDetails;
    }
}
