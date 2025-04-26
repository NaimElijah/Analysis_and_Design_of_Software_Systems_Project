# User Instructions for Employee Module

### Quick Start
The module entry point is from the "Employee Module Main Menu" screen where the user can navigate to the desired part.
The system is preloaded with an Admin user with all the system permissions, the user id is 123456789.
**NEED TO ADD MORE ONCE WE WILL UNITE ALL PARTS TO ONE CLI**

### Overview
The employee module is responsible for managing the employees, shifts, and system permissions.
 - The employee management supports viewing, editing, and creating employees.
 - The Assignment management supports viewing, editing, and creating shifts.
 - The Avalibilty ** PLACE HOLDER FOR ROTEM **

---

## Employee Management Instructions

### Overview
The Employee Management allows you to manage employees, roles, and permissions within the system. Different options are available based on user permissions.

### Getting Started
1. When you start the Employee Management, you'll see a main menu with numbered options.
2. Enter the number corresponding to your desired action and press Enter.

### Features

#### Employee Management
- **View All Employees**: See a list of all employees in the system
- **View Employee Details**: Enter an employee's Israeli ID to see their complete information
- **Create Employee**: Add a new employee to the system (requires CREATE_EMPLOYEE permission)
- **Update Employee**: Modify an existing employee's information (requires UPDATE_EMPLOYEE permission)
- **Deactivate Employee**: Deactivate an employee in the system (requires DEACTIVATE_EMPLOYEE permission)

#### Role Management
- **View All Roles**: See a list of all roles in the system
- **View Role Details**: Enter a role name to see its complete information
- **Create Role**: Create a new role in the system (requires CREATE_ROLE permission)
- **Clone Role**: Create a new role based on an existing one (requires CREATE_ROLE permission)
- **Add Role to Employee**: Assign a role to an employee (requires ROLE_PERMISSION)
- **Remove Role from Employee**: Remove a role from an employee (requires ROLE_PERMISSION permission)

#### Permission Management
- **View All Permissions**: See a list of all permissions in the system
- **Create Permission**: Add a new permission to the system (requires CREATE_PERMISSION permission)
- **Add Permission to Role**: Assign a permission to a role (requires ADD_PERMISSION_TO_ROLE permission)
- **Remove Permission from Role**: Remove a permission from a role (requires REMOVE_PERMISSION_FROM_ROLE permission)

---

## Assignment Management Instructions

### Overview
The Assignment Management allows you to manage shifts and employee assignments. Different options are available based on user permissions.

### Getting Started
1. When you enter the Assignment Management, you'll see the Assignment Management Menu with numbered options.
2. Enter the number corresponding to your desired action and press Enter.

### Features

#### Shift Viewing
- **View All Shifts**: See a list of all shifts in the system
- **View Shift Details**: Enter a shift ID to see its complete information
- **View Employee Assignments**: See which shifts an employee is assigned to

#### Shift Management (requires ASSIGN_EMPLOYEE permission)
When you select "Shift Management," you'll see a submenu with these options:

1. **Assign Employees to Shift**: Add an employee to a specific shift
2. **Remove Employee from Shift**: Remove an employee from a shift
3. **Modify Required Roles**: Change the roles required for a shift
4. **Create Full Week of Shifts**: Create shifts for an entire week (Sunday-Saturday)
    - You'll need to specify a Sunday start date
    - Define the roles required for all shifts in the week
    - The system will create morning and evening shifts for each day

---


## Availability Management Instructions TODO

### Overview
The XXXX Management allows you to manage employees, roles, and permissions within the system. Different options are available based on user permissions.

### Getting Started
1. When you start the XXXX Management, you'll see a main menu with numbered options.
2. Enter the number corresponding to your desired action and press Enter.

### Features


---

## General 

### Data and Massages info
- Required fields are marked with an asterisk (*) or highlighted
- Error messages appear in red text
- Success messages appear in green text
- Warning messages appear in yellow text
- Press Enter when prompted to continue to the next screen
- The System date format is dd-MM-yyyy (e.g 02-05-2025)

### System Permission List

| Permission | Category | Description |
|------------|----------|-------------|
| `CREATE_EMPLOYEE` | Employee Management | Allows creating new employees in the system. Users with this permission can add new employee records with personal details, salary information, and employment terms. |
| `UPDATE_EMPLOYEE` | Employee Management | Allows modifying existing employee information such as name, salary, and employment terms. |
| `EDIT_EMPLOYEE` | Employee Management | Similar to UPDATE_EMPLOYEE, provides the ability to edit employee information. |
| `DEACTIVATE_EMPLOYEE` | Employee Management | Allows marking an employee as inactive in the system without deleting their record. |
| `DELETE_EMPLOYEE` | Employee Management | Allows permanent removal of an employee record from the system. This is different from deactivation. |
| `MANAGE_HR` | Employee Management | Provides general human resources management capabilities. |
| `CREATE_ROLE` | Role Management | Allows creating new roles in the system. Users with this permission can also clone existing roles. |
| `ROLE_PERMISSION` | Role Management | Allows assigning roles to employees and removing roles from employees. |
| `ADD_PERMISSION_TO_ROLE` | Role Management | Allows adding specific permissions to a role, modifying what users with that role can do. |
| `REMOVE_PERMISSION_FROM_ROLE` | Role Management | Allows removing specific permissions from a role. |
| `CREATE_PERMISSION` | Role Management | Allows creating new permission types in the system. |
| `CREATE_SHIFT` | Shift Management | Allows creating new shifts in the system, including creating a full week of shifts. |
| `UPDATE_SHIFT` | Shift Management | Allows modifying existing shift information such as date, time, and required roles. |
| `REMOVE_SHIFT` | Shift Management | Allows deleting shifts from the system. |
| `GET_SHIFT` | Shift Management | Allows viewing shift details. |
| `MANAGE_SHIFT` | Shift Management | Provides general shift management capabilities. |
| `ASSIGN_EMPLOYEE_TO_SHIFT` | Shift Management | Allows assigning employees to specific shifts based on roles and availability. |
| `MANAGE_INVENTORY` | Operational | Allows managing inventory items, stock levels, and related operations. |
| `DRIVE_VEHICLE` | Operational | Allows operating company vehicles for deliveries or other purposes. |
| `STOCK_SHELVES` | Operational | Allows stocking and organizing merchandise on store shelves. |
---