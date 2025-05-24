# User Instructions for Transport Module

### Quick Start
The module entry point is from the "Transport Module Main Menu" screen where the user can navigate to the desired feature or operation. 
The system manages transport tasks, including transports scheduling, assigning drivers and trucks to transports, delivery management, and inventory-related transportation.

---

## Overview
The Transport Module handles all transportation logistics within the system and supports features such as:

- Transports and fleet management
- Trucks and drivers assignment
- Schedule management for transport tasks
- Driver assignment and tracking
- Transports Routes and logistics tracking

Below are detailed instructions for navigating and using the Transport Module.

---

## Features Overview

### 1. Vehicle Management
Efficiently manage and maintain the company's fleet.
- **View Vehicle List:** View all vehicles registered within the system.
- **Add Vehicle:** Add a new vehicle to the fleet.
- **Edit Vehicle Details:** Update attributes of a vehicle (e.g., operational status, capacity).
- **Remove Vehicle:** Remove a vehicle from the system if it is no longer operational.

---

### 2. Driver Management
Manage the drivers responsible for handling transportation.
- **View Drivers:** See a list of registered drivers and their statuses.
- **Add Driver:** Add a new driver to the system.
- **Edit Driver Details:** Update driver information (e.g., license details, availability).
- **Remove Driver:** Remove a driver profile from the system.

---

### 3. Route Planning
Optimize and plan transport routes to ensure efficient delivery.
- **View Routes:** List all available routes used for transportation.
- **Plan New Route:** Create a new route based on pickup and delivery points.
- **Edit Route Details:** Modify existing route information.
- **Delete Route:** Remove routes if they are no longer necessary.

---

### 4. Delivery Management
Oversee the complete delivery lifecycle.
- **Create New Delivery Order:** Initiate a new delivery order with assigned routes, drivers, and vehicles.
- **Check Delivery Status:** Track the status of ongoing delivery orders.
- **Edit Delivery Orders:** Update existing delivery order details.
- **Cancel Delivery Orders:** Cancel a delivery order if it is no longer valid.
- **Delivery History:** View past delivery records and reports.

---

### 5. Schedule Management
Streamline scheduling for drivers and vehicles.
- **View Transport Schedule:** View the current day's or week's transport schedules.
- **Add Schedule:** Create a new schedule for a vehicle or driver.
- **Edit Schedule:** Modify existing transport schedules as required.
- **Delete Schedule:** Remove a schedule that is no longer valid.

---

### 6. Reports and Logs
Track transport and driver performance through reports.
- **View Vehicle Logs:** Access maintenance and operational logs for vehicles.
- **Driver Performance Reports:** Evaluate driver performance metrics.
- **Delivery Success Reports:** View statistics on completed and delayed deliveries.

---

## General Usage Instructions

### Starting the Transport Module
1. Launch the system and select the Transport Module from the main menu.
2. Review your permissions to ensure access to required features.

### Navigation
- Use the numbered options on each menu to navigate to specific features.
- Submenus will guide you to finer-grain options for managing vehicles, routes, schedules, or drivers.

### Permissions
Access to specific features depends on your role and permissions. For example:
- **VIEW_VEHICLE**: For accessing vehicle information.
- **CREATE_DELIVERY_ORDER**: For creating delivery tasks.
- **EDIT_SCHEDULE**: For modifying schedules.

Ensure you have the appropriate role or notify your system administrator.

---

## Common Commands and Actions

- **Navigating Menus:** Use numeric keys to enter a menu. Enter "0" to go back to the previous menu.
- **Input Validation:** Provide correct details for inputs like vehicle registration numbers, driver IDs, or route names.
- **Error Messages:** Pay attention to error messages that may indicate invalid commands or permissions.
- **Exit Options:** Use the "Exit" option in any menu to return to the Main Menu or end the session.

---

## Error and Warning Messages

- **Missing Permissions:** "Permission Denied - You lack the required permission for this operation."
- **Invalid Inputs:** "Error - Input does not match the required format, please try again."
- **Schedule Conflict:** "Warning - Schedule conflict detected. Please review and adjust timing."

---

## Permissions Specific to Transport Management

| **Permission**          | **Description**                                        |
|--------------------------|--------------------------------------------------------|
| `VIEW_DRIVER`            | View a list of all active drivers.                     |
| `ADD_DRIVER`             | Add new drivers to the system.                         |
| `EDIT_DRIVER`            | Update driver details or availability.                |
| `REMOVE_DRIVER`          | Remove drivers from the system.                        |
| `VIEW_VEHICLE`           | View a list of all vehicles in the fleet.              |
| `ADD_VEHICLE`            | Add new vehicles to the fleet.                         |
| `EDIT_VEHICLE`           | Edit vehicle information, such as capacity or status.  |
| `REMOVE_VEHICLE`         | Remove vehicles from the system.                       |
| `VIEW_TRANSPORT_SCHEDULE`| View transport schedules for drivers and vehicles.     |
| `EDIT_SCHEDULE`          | Modify schedules assigned to drivers or vehicles.      |
| `CREATE_DELIVERY_ORDER`  | Create delivery orders and assign tasks to drivers.    |
| `VIEW_DELIVERY_ORDER`    | See details and track progress of delivery orders.     |
| `EDIT_DELIVERY_ORDER`    | Update delivery order details.                         |
| `DELETE_DELIVERY_ORDER`  | Cancel existing delivery orders.                       |
| `PLAN_ROUTE`             | Plan delivery and transport routes.                   |
| `EDIT_ROUTE`             | Modify planned routes.                                 |
| `DELETE_ROUTE`           | Remove routes no longer needed.                        |

---

## Final Notes

- The system relies on accurate and up-to-date driver, vehicle, and delivery data. Ensure the information you enter is correct.
- Contact an administrator if permission issues block you from accessing required features.
- Always log out of the module after completing work to secure access.