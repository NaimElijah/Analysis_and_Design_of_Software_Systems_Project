# User Instructions for Employee Management

## Quick Start
The Employee Management system is accessed through the main menu. You need appropriate permissions to access different features. The system comes preloaded with an Admin user (ID: 123456789) who has all permissions.

## Overview
The Employee Management system allows you to manage all aspects of employee data, shifts, and assignments within the supermarket management system. Key features include:

- Managing employee personal and employment information
- Creating and assigning roles to employees
- Managing permissions for different roles
- Viewing and updating employee details
- Managing employee branch assignments
- Creating and managing shifts
- Assigning employees to shifts
- Managing employee availability for shifts

## Getting Started
1. From the main menu, you will see options based on your permissions:
   - **Employees** - For employee management
   - **Shifts** - For shift management
   - **Assignment Board** - For assigning employees to shifts
   - **Availability Board** - For managing employee availability
2. Select the desired option by entering the corresponding number and pressing Enter.
3. You will enter the selected module and see a list of available actions.
4. Enter the number corresponding to your desired action and press Enter.

## Employee Management Features

### Employee Information Management
- **View All Employees**: Displays a paginated list of all employees in the system with basic information including ID, name, branch, and status.
- **View Employee Details**: Enter an employee's Israeli ID to see their complete information including personal details, roles, and employment terms.
- **Create Employee**: Add a new employee to the system with the following information:
  - Israeli ID (required)
  - First Name (required)
  - Last Name (required)
  - Salary (required)
  - Terms of Employment (optional)
  - Start Date (required, in format DD-MM-YYYY)
  - Branch ID (required)

  *Requires CREATE_EMPLOYEE permission*

- **Update Employee**: Modify an existing employee's information including:
  - First Name
  - Last Name
  - Salary
  - Terms of Employment
  - Active Status

  *Requires UPDATE_EMPLOYEE permission*

- **Deactivate Employee**: Mark an employee as inactive in the system. Inactive employees cannot be assigned to shifts or access the system.

  *Requires DEACTIVATE_EMPLOYEE permission*

### Role Management
- **View All Roles**: See a paginated list of all roles defined in the system.
- **View Role Details**: Select a role from a list to see its complete information including associated permissions.
- **Add Role to Employee**: Assign a role to an employee. This grants the employee all permissions associated with that role.

  *Requires ROLE_PERMISSION permission*

- **Remove Role from Employee**: Remove a role from an employee. This revokes all permissions associated with that role unless the employee has another role with those permissions.

  *Requires ROLE_PERMISSION permission*

- **Create Role**: Create a new role in the system with a set of permissions.

  *Requires CREATE_ROLE permission*

- **Clone Role**: Create a new role by copying an existing role's permissions.

  *Requires CREATE_ROLE permission*

### Permission Management
- **View All Permissions**: See a paginated list of all permissions defined in the system.
- **Add Permission to Role**: Assign a permission to a role. All employees with this role will gain this permission.

  *Requires ADD_PERMISSION_TO_ROLE permission*

- **Remove Permission from Role**: Remove a permission from a role. All employees with only this role will lose this permission.

  *Requires REMOVE_PERMISSION_FROM_ROLE permission*

- **Create Permission**: Add a new permission type to the system.

  *Requires CREATE_PERMISSION permission*

## Shift Management Features

### Shift Information Management
- **View All Shifts**: Displays a paginated list of all shifts with basic information including date, type, status, and assignment status.
- **View Shift Details**: Select a shift to see its complete information including assigned employees, roles required, and available employees.
- **View My Shifts**: See a list of shifts to which you are assigned, including your roles in each shift.
- **Add Shift**: Create a new shift with the following information:
  - Date
  - Type (Morning or Evening)
  - Start and End Hours
  - Required Roles and Counts
  - Open/Closed Status

  *Requires CREATE_SHIFT permission*

- **Add Weekly Shifts**: Create morning and evening shifts for an entire week starting from a selected Sunday.

  *Requires CREATE_SHIFT permission*

- **Edit Shifts**: Modify an existing shift's information including date, type, hours, open status, and required roles.

  *Requires EDIT_SHIFT permission*

- **Delete Shifts**: Remove a shift from the system.

  *Requires REMOVE_SHIFT permission*

## Assignment Management Features

### Assignment Board
- **Assign From Upcoming Week**: View and assign employees to shifts in the upcoming week.
- **Assign By Specific Date**: Select a specific date to view and assign employees to shifts.
- **Assign From Previous Weeks**: View and assign employees to shifts from previous weeks.
- **Shift Management**: Create, view, and manage shifts.
- **View All Shifts**: See a list of all shifts in the system.
- **View Shift Details**: See detailed information about a specific shift.
- **Assign Employee to Shift**: Add an employee to a specific role in a shift.
- **Remove Employee from Shift**: Remove an employee from a shift assignment.
- **Modify Required Roles**: Change the roles and number of employees required for a shift.
- **View Employee Assignments**: See all shifts assigned to a specific employee.

## Availability Management Features

### Availability Board
- **Mark Availability**: Mark yourself as available or unavailable for shifts in the upcoming week.
- **Update Multiple Shifts**: Update your availability for multiple shifts at once.

## Common Tasks

### Creating a New Employee
1. From the main menu, select **Employees**.
2. From the Employee Management menu, select **Create Employee**.
3. Enter the required information when prompted:
   - Israeli ID (must be a valid ID number)
   - First Name
   - Last Name
   - Salary (in local currency)
   - Start Date (in format DD-MM-YYYY)
   - Branch ID
4. Enter key-value pairs for Terms of Employment (optional). Type 'done' when finished.
5. Confirm the information when prompted.
6. The system will display a success message if the employee was created successfully.

### Assigning a Role to an Employee
1. From the main menu, select **Employees**.
2. From the Employee Management menu, select **Add Role to Employee**.
3. Enter the Israeli ID of the employee.
4. Select a role from the numbered list of available roles.
5. Confirm the action when prompted.
6. The system will display a success message if the role was assigned successfully.

### Creating a New Shift
1. From the main menu, select **Shifts**.
2. From the Shift Management menu, select **Add Shift**.
3. Enter the required information when prompted:
   - Date (select from options or enter a specific date)
   - Shift Type (Morning or Evening)
   - Start and End Hours
   - Required number of employees for each role
   - Whether the shift is open for assignments
4. Confirm the information when prompted.
5. The system will display a success message if the shift was created successfully.

### Assigning an Employee to a Shift
1. From the main menu, select **Assignment Board**.
2. Select how you want to view shifts (upcoming week, specific date, or previous weeks).
3. Select a shift from the list.
4. Select **Assign Employee to Shift**.
5. Select a role from the list of required roles.
6. Select an employee from the list of available employees.
7. Confirm the assignment when prompted.
8. The system will display a success message if the employee was assigned successfully.

### Marking Availability for Shifts
1. From the main menu, select **Availability Board**.
2. View the list of shifts for the upcoming week.
3. Enter the number(s) of the shift(s) you want to update (comma-separated, or 'all' for all shifts).
4. Enter 'Y' to mark yourself as available or 'N' to mark yourself as unavailable.
5. The system will update your availability and refresh the display.

## System Permissions for Employee Management
| Permission | Description |
|------------|-------------|
| `CREATE_EMPLOYEE` | Allows creating new employees in the system with full details. |
| `UPDATE_EMPLOYEE` | Allows modifying existing employee information such as name, salary, and employment terms. |
| `EDIT_EMPLOYEE` | Allows editing employee information (alias for updating fields). |
| `VIEW_EMPLOYEE` | Allows viewing employee information without editing rights. |
| `DEACTIVATE_EMPLOYEE` | Allows marking an employee as inactive in the system. |
| `DELETE_EMPLOYEE` | Allows permanent removal of an employee record from the system. |
| `MANAGE_HR` | Provides general human resources management capabilities. |
| `CREATE_ROLE` | Allows creating new roles in the system and cloning existing ones. |
| `EDIT_ROLE` | Allows editing existing role details. |
| `ROLE_PERMISSION` | Allows assigning and removing roles from employees. |
| `ADD_PERMISSION_TO_ROLE` | Allows adding specific permissions to a role. |
| `REMOVE_PERMISSION_FROM_ROLE` | Allows removing specific permissions from a role. |
| `CREATE_PERMISSION` | Allows creating new permission types in the system. |
| `EDIT_PERMISSION` | Allows editing the properties of a permission. |
| `GET_ROLES` | Allows viewing all existing roles in the system. |
| `CREATE_SHIFT` | Allows creating new shifts in the system. |
| `UPDATE_SHIFT` | Allows modifying existing shift information. |
| `EDIT_SHIFT` | Allows editing shift information (alias for updating fields). |
| `REMOVE_SHIFT` | Allows deleting shifts from the system. |
| `GET_SHIFT` | Allows viewing shift information. |
| `MANAGE_SHIFT` | Provides general shift management capabilities. |
| `VIEW_SHIFT` | Allows viewing shift details without editing rights. |
| `ASSIGN_EMPLOYEE` | Allows assigning employees to shifts. |
| `UPDATE_AVAILABLE` | Allows marking availability for shifts. |
