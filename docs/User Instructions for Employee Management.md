# User Instructions for Employee Management

## Quick Start
The Employee Management system is accessed through the "Employee Module Main Menu" by selecting option 1 (Employees). You need appropriate permissions to access different features. The system comes preloaded with an Admin user (ID: 123456789) who has all permissions.

## Overview
The Employee Management system allows you to manage all aspects of employee data within the supermarket management system. Key features include:
- Managing employee personal and employment information
- Creating and assigning roles to employees
- Managing permissions for different roles
- Viewing and updating employee details
- Managing employee branch assignments

## Getting Started
1. From the main menu, select **Employees** by entering the number `1` and pressing Enter.
2. You will enter the Employee Management System and see a list of employee-related actions.
3. Enter the number corresponding to your desired action and press Enter.

## Features

### Employee Information Management
- **View All Employees**: Displays a list of all employees in the system with basic information.
- **View Employee Details**: Enter an employee's Israeli ID to see their complete information including personal details, roles, and employment terms.
- **Create Employee**: Add a new employee to the system with the following information:
  - Israeli ID (required)
  - First Name (required)
  - Last Name (required)
  - Salary (required)
  - Terms of Employment (optional)
  - Roles (optional)
  - Start Date (required)
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
- **View All Roles**: See a list of all roles defined in the system.
- **View Role Details**: Enter a role name to see its complete information including associated permissions.
- **Add Role to Employee**: Assign a role to an employee. This grants the employee all permissions associated with that role.
  
  *Requires ROLE_PERMISSION permission*
  
- **Remove Role from Employee**: Remove a role from an employee. This revokes all permissions associated with that role unless the employee has another role with those permissions.
  
  *Requires ROLE_PERMISSION permission*
  
- **Create Role**: Create a new role in the system with a set of permissions.
  
  *Requires CREATE_ROLE permission*

### Permission Management
- **View All Permissions**: See a list of all permissions defined in the system.
- **Add Permission to Role**: Assign a permission to a role. All employees with this role will gain this permission.
  
  *Requires ADD_PERMISSION_TO_ROLE permission*
  
- **Remove Permission from Role**: Remove a permission from a role. All employees with only this role will lose this permission.
  
  *Requires REMOVE_PERMISSION_FROM_ROLE permission*
  
- **Create Permission**: Add a new permission type to the system.
  
  *Requires CREATE_PERMISSION permission*

### Branch Management
- **Update Employee Branch**: Change the branch assignment for an employee.
  
  *Requires UPDATE_EMPLOYEE permission*
  
- **View Employee Branch**: See which branch an employee is assigned to.

## Common Tasks

### Creating a New Employee
1. From the Employee Management menu, select **Create Employee**.
2. Enter the required information when prompted:
   - Israeli ID (must be a valid ID number)
   - First Name
   - Last Name
   - Salary (in local currency)
   - Terms of Employment (optional)
   - Roles (optional, can be added later)
   - Start Date (in format DD-MM-YYYY)
   - Branch ID
3. Confirm the information when prompted.
4. The system will display a success message if the employee was created successfully.

### Assigning a Role to an Employee
1. From the Employee Management menu, select **Add Role to Employee**.
2. Enter the Israeli ID of the employee.
3. Enter the name of the role to assign.
4. The system will display a success message if the role was assigned successfully.

### Updating Employee Information
1. From the Employee Management menu, select **Update Employee**.
2. Enter the Israeli ID of the employee to update.
3. Enter the new information for the fields you want to update. Press Enter without typing anything to keep the current value.
4. Confirm the changes when prompted.
5. The system will display a success message if the employee was updated successfully.

### Deactivating an Employee
1. From the Employee Management menu, select **Deactivate Employee**.
2. Enter the Israeli ID of the employee to deactivate.
3. Confirm the action when prompted.
4. The system will display a success message if the employee was deactivated successfully.

## Error Handling
- **Invalid Israeli ID**: Ensure you enter a valid Israeli ID number.
- **Employee Not Found**: Check that the employee exists in the system.
- **Role Not Found**: Check that the role exists in the system.
- **Permission Denied**: Ensure you have the required permissions for the action.
- **Invalid Input**: Check that you entered the correct format for dates, numbers, and other fields.

## Tips and Best Practices
- Always verify employee information before creating or updating records.
- Assign roles based on the employee's job responsibilities.
- Regularly review and update employee information to ensure accuracy.
- Use the "View Employee Details" feature to verify changes after updating employee information.
- When creating new roles, carefully consider which permissions to include.
- Deactivate employees who leave the company rather than deleting them to maintain historical records.

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