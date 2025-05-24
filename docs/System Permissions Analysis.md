
# System Permissions Analysis

Based on the code review, I've compiled a comprehensive analysis of all system permissions in the Supermarket Management System, including where they are used and their purposes.

## Employee Management Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `CREATE_EMPLOYEE` | Allows creating new employees | EmployeeController, EmployeeCLI | createEmployee() |
| `UPDATE_EMPLOYEE` | Allows modifying existing employee information | EmployeeController, EmployeeCLI | updateEmployee() |
| `EDIT_EMPLOYEE` | Alias for updating employee fields | EmployeeController, EmployeeCLI | Various employee editing functions |
| `VIEW_EMPLOYEE` | Allows viewing employee information | EmployeeController, EmployeeCLI, MainCLI | getEmployeeById(), getAllEmployees() |
| `DEACTIVATE_EMPLOYEE` | Allows marking an employee as inactive | EmployeeController, EmployeeCLI | deactivateEmployee() |
| `DELETE_EMPLOYEE` | Allows permanent removal of employee records | EmployeeController, EmployeeCLI | deleteEmployee() |
| `MANAGE_HR` | Provides general HR management capabilities | EmployeeController, EmployeeCLI | Various HR management functions |

## Role and Permission Management

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `CREATE_ROLE` | Allows creating new roles | AuthorisationController, EmployeeCLI | createRole() |
| `EDIT_ROLE` | Allows modifying existing roles | AuthorisationController, EmployeeCLI | Various role editing functions |
| `ROLE_PERMISSION` | Allows assigning/removing roles from employees | EmployeeController, EmployeeCLI | addRoleToEmployee(), removeRoleFromEmployee() |
| `ADD_PERMISSION_TO_ROLE` | Allows adding permissions to roles | AuthorisationController, EmployeeCLI | addPermissionToRole() |
| `REMOVE_PERMISSION_FROM_ROLE` | Allows removing permissions from roles | AuthorisationController, EmployeeCLI | removePermissionFromRole() |
| `CREATE_PERMISSION` | Allows creating new permission types | AuthorisationController, EmployeeCLI | createPermission() |
| `EDIT_PERMISSION` | Allows modifying permission properties | AuthorisationController, EmployeeCLI | Not directly implemented in current code |
| `GET_ROLES` | Allows viewing all roles | AuthorisationController, EmployeeCLI | getAllRoles() |
| `ROLE_REQUIRED` | Indicates a role requirement | ShiftController | Various shift management functions |

## Shift Management Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `CREATE_SHIFT` | Allows creating new shifts | ShiftController, ShiftCLI | createShift(), createWeeklyShifts() |
| `UPDATE_SHIFT` | Allows modifying shift information | ShiftController, ShiftCLI | updateShift() |
| `EDIT_SHIFT` | Alias for updating shift fields | ShiftController, ShiftCLI | Various shift editing functions |
| `REMOVE_SHIFT` | Allows deleting shifts | ShiftController, ShiftCLI | removeShift() |
| `GET_SHIFT` | Allows viewing shift information | ShiftController, ShiftCLI | getShift(), getAllShifts() |
| `MANAGE_SHIFT` | Provides general shift management | ShiftController, ShiftCLI | Various shift management functions |
| `VIEW_SHIFT` | Allows viewing shift details | ShiftController, ShiftCLI, MainCLI | getShift(), getAllShifts() |

## Assignment Management Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `ASSIGN_EMPLOYEE` | Allows assigning employees to shifts | AssignmentController, AssignmentCLI, MainCLI | assignEmployeeToShift() |
| `ASSIGN_EMPLOYEE_TO_SHIFT` | Specific permission for shift assignments | AssignmentController, AssignmentCLI | assignEmployeeToShift() |

## Availability Management Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `UPDATE_AVAILABLE` | Allows marking availability for shifts | AvailabilityController, AvailabilityCLI, MainCLI | markEmployeeAvailable(), removeEmployeeAvailability() |

## Transport Management Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `CREATE_TRANSPORT` | Allows creating transport documents | TransportService | createTransport() |
| `EDIT_TRANSPORT` | Allows modifying transport details | TransportService | setTransportStatus(), setTransportTruck(), setTransportDriver(), addDestSite(), removeDestSite(), setSiteArrivalIndexInTransport(), changeAnItemsDocNum(), checkIfDriverDrivesThisItemsDoc(), addTransportProblem(), removeTransportProblem() |
| `DELETE_TRANSPORT` | Allows removing transport records | TransportService | deleteTransport() |
| `VIEW_TRANSPORT` | Allows viewing transport information | TransportService | showAllQueuedTransports(), showAllTransports() |
| `VIEW_RELEVANT_TRANSPORTS` | Allows viewing assigned transports | TransportService | showTransportsOfDriver() |
| `ADD_ITEM_TO_TRANSPORT` | Allows adding items to transport | TransportService | addItem() |
| `EDIT_ITEM_IN_TRANSPORT` | Allows modifying transport items | TransportService | Not directly implemented in current code |
| `DELETE_ITEM_FROM_TRANSPORT` | Allows removing items from transport | TransportService | removeItem() |
| `EDIT_TRANSPORT_ITEM_CONDITION` | Allows updating item condition | TransportService | setItemCond() |

## Site Management Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `ADD_SITE` | Allows adding new sites | SiteService | addSite() |
| `EDIT_SITE` | Allows modifying site information | SiteService | setSiteAddress(), setSiteContactName(), setSiteContactNum() |
| `DELETE_SITE` | Allows removing sites | SiteService | deleteSite() |
| `SHOW_SITES` | Allows viewing site information | SiteService | showAllSites() |

## Shipping Area Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `ADD_SHIPPING_AREA` | Allows creating shipping areas | SiteService | addShippingArea() |
| `EDIT_SHIPPING_AREA` | Allows modifying shipping areas | SiteService | setShippingAreaNum(), setShippingAreaName() |
| `DELETE_SHIPPING_AREA` | Allows removing shipping areas | SiteService | deleteShippingArea() |
| `SHOW_SHIPPING_AREAS` | Allows viewing shipping areas | SiteService | showAllShippingAreas() |

## Truck Management Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `ADD_TRUCK` | Allows adding new trucks | TruckService | addTruck() |
| `DELETE_TRUCK` | Allows removing trucks | TruckService | removeTruck() |
| `SHOW_TRUCKS` | Allows viewing truck information | TruckService | showTrucks() |

## Operational Permissions

| Permission | Purpose | Where Used | Functions |
|------------|---------|------------|-----------|
| `MANAGE_INVENTORY` | Allows inventory management | Assigned to roles (Stocker, Admin) | Not directly implemented in current code |
| `HANDLE_CASH` | Allows cash handling | Assigned to roles (Cashier, Admin) | Not directly implemented in current code |
| `DRIVE_VEHICLE` | Allows vehicle operation | Assigned to roles (Driver roles, Admin) | Not directly implemented in current code |
| `CLEAN_FACILITY` | Allows facility maintenance | Assigned to roles (Cleaner, Admin) | Not directly implemented in current code |
| `STOCK_SHELVES` | Allows shelf stocking | Assigned to roles (Stocker, Admin) | Not directly implemented in current code |

## Permission Implementation Details

The permission system is implemented through several layers:

1. **Domain Layer**:
    - `AuthorisationController` contains the core logic for checking permissions
    - `EmployeeController` uses the AuthorisationController to verify permissions before operations
    - The `hasPermission()` method checks if an employee has a specific permission through their assigned roles

2. **Service Layer**:
    - `EmployeeService` provides the `isEmployeeAuthorised()` method that wraps the domain layer functionality
    - `EmployeeIntegrationService` provides a simplified interface for other modules to check permissions

3. **Presentation Layer**:
    - CLI classes use the `hasPermission()` method to determine which menu options to display
    - Permission checks are performed before executing operations

4. **Database**:
    - Permissions are stored in the `Permissions` table
    - Roles are stored in the `Roles` table
    - Role-permission associations are stored in the `RolePermissions` table
    - Employee-role associations are stored in the `EmployeeRoles` table

The system follows the principle of role-based access control, where:
- Permissions represent granular actions
- Roles are collections of permissions
- Employees are assigned roles
- An employee has a permission if any of their roles includes that permission

This design allows for flexible permission management while maintaining security throughout the application.