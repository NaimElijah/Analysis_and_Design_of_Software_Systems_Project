package PresentationLayer;

import ServiceLayer.EmployeeSL;
import ServiceLayer.EmployeeService;
import ServiceLayer.exception.AuthorizationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EmployeeCLI {
    // ANSI color codes
    private static final String RESET = CliUtil.RESET;
    private static final String RED = CliUtil.RED;
    private static final String GREEN = CliUtil.GREEN;
    private static final String YELLOW = CliUtil.YELLOW;
    private static final String BLUE = CliUtil.BLUE;
    private static final String PURPLE = CliUtil.PURPLE;
    private static final String CYAN = CliUtil.CYAN;
    private static final String BOLD = CliUtil.BOLD;

    // Properties
    private final DateTimeFormatter dateFormatter = CliUtil.dateFormatter;
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private final long doneBy;


    /**
     * Constructor for the Employee Command Line Interface
     * 
     * @param employeeService The service layer for employee operations
     * @param doneBy The ID of the employee using the CLI
     */
    public EmployeeCLI(EmployeeService employeeService, long doneBy) {
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
        this.doneBy = doneBy;
    }

    /**
     * Start the CLI application
     */
    public void start() {
        printWelcomeBanner();

        while (true) {
            displayMenu();
            String choice = scanner.nextLine();
            processMenuChoice(choice);
        }
    }

    //==========================================================================================
    // MENU SYSTEM METHODS
    //==========================================================================================

    /**
     * Displays the main menu with options based on user permissions
     */
    private void displayMenu() {
        System.out.println();
        printSectionHeader("Main Menu");

        List<String> menuOptions = new ArrayList<>();
        int optionNumber = 1;

        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View All Employees");
        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View Employee Details");

        if (hasPermission("CREATE_EMPLOYEE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Create Employee");
        }

        if (hasPermission("UPDATE_EMPLOYEE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Update Employee");
        }

        if (hasPermission("DEACTIVATE_EMPLOYEE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Deactivate Employee");
        }

        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View All Roles");
        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View Role Details");

        if (hasPermission("CREATE_ROLE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Create Role");
        }

        if (hasPermission("CREATE_ROLE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Clone Role");
        }

        if (hasPermission("ROLE_PERMISSION")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Add Role to Employee");
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Remove Role from Employee");
        }

        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View All Permissions");

        if (hasPermission("CREATE_PERMISSION")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Create Permission");
        }

        if (hasPermission("ADD_PERMISSION_TO_ROLE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Add Permission to Role");
        }

        if (hasPermission("REMOVE_PERMISSION_FROM_ROLE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Remove Permission from Role");
        }

        menuOptions.add(YELLOW + optionNumber++ + RESET + ". Back To Main Menu");

        for (String option : menuOptions) {
            System.out.println("  " + option);
        }

        System.out.println();
        System.out.print(CYAN + "======>" + RESET + " Enter your choice: ");
    }

    /**
     * Processes the user's menu choice
     * 
     * @param choice The user's input choice
     */
    private void processMenuChoice(String choice) {
        System.out.println();

        try {
            int choiceNum = Integer.parseInt(choice);
            int currentOption = 1;

            // Employee Management Section
            if (choiceNum == currentOption++) {
                printAllEmployees();
                return;
            }

            if (choiceNum == currentOption++) {
                System.out.print(BOLD + "Enter Employee Israeli ID: " + RESET);
                long israeliId = Long.parseLong(scanner.nextLine());
                printEmployeeDetails(israeliId);
                return;
            }

            if (hasPermission("CREATE_EMPLOYEE")) {
                if (choiceNum == currentOption++) {
                    createEmployee();
                    return;
                }
            }

            if (hasPermission("UPDATE_EMPLOYEE")) {
                if (choiceNum == currentOption++) {
                    updateEmployee();
                    return;
                }
            }

            if (hasPermission("DEACTIVATE_EMPLOYEE")) {
                if (choiceNum == currentOption++) {
                    deactivateEmployee();
                    return;
                }
            }

            // Role Management Section
            if (choiceNum == currentOption++) {
                printAllRoles();
                return;
            }

            if (choiceNum == currentOption++) {
                System.out.print(BOLD + "Enter Role Name: " + RESET);
                String roleName = scanner.nextLine();
                printRoleDetails(roleName);
                return;
            }

            if (hasPermission("CREATE_ROLE")) {
                if (choiceNum == currentOption++) {
                    createRole();
                    return;
                }
            }

            if (hasPermission("CREATE_ROLE")) {
                if (choiceNum == currentOption++) {
                    cloneRole();
                    return;
                }
            }

            if (hasPermission("ROLE_PERMISSION")) {
                if (choiceNum == currentOption++) {
                    addRoleToEmployee();
                    return;
                }

                if (choiceNum == currentOption++) {
                    removeRoleFromEmployee();
                    return;
                }
            }

            if (choiceNum == currentOption++) {
                printAllPermissions();
                return;
            }

            if (hasPermission("CREATE_PERMISSION")) {
                if (choiceNum == currentOption++) {
                    createPermission();
                    return;
                }
            }

            if (hasPermission("ADD_PERMISSION_TO_ROLE")) {
                if (choiceNum == currentOption++) {
                    addPermissionToRole();
                    return;
                }
            }

            if (hasPermission("REMOVE_PERMISSION_FROM_ROLE")) {
                if (choiceNum == currentOption++) {
                    removePermissionFromRole();
                    return;
                }
            }

            if (choiceNum == currentOption++) {
                System.out.println(BOLD + YELLOW + "Exiting system..." + RESET);
                System.exit(0);
                return;
            }

            printError("Invalid choice. Please try again.");
        } catch (NumberFormatException e) {
            printError("Please enter a valid number.");
        }
    }

    //==========================================================================================
    // UI UTILITY METHODS
    //==========================================================================================

    /**
     * Prints an error message with formatting
     *
     * @param message The error message to display
     */
    private void printError(String message) {
        CliUtil.printError(message);
    }

    /**
     * Prints a success message with formatting
     *
     * @param message The success message to display
     */
    private void printSuccess(String message) {
        CliUtil.printSuccess(message);
    }

    /**
     * Waits for the user to press Enter to continue
     */
    private void waitForEnter() {
        CliUtil.waitForEnter(scanner);
    }

    /**
     * Gets user confirmation for an action
     * 
     * @param message The confirmation message to display
     * @return true if confirmed, false otherwise
     */
    private boolean confirm(String message) {
        return CliUtil.confirm(message, scanner);
    }

    /**
     * Gets a long value from user input
     * 
     * @param prompt The prompt to display
     * @return The long value entered by the user
     */
    private long getLongInput(String prompt) {
        return CliUtil.getLongInput(prompt, scanner);
    }

    /**
     * Checks if the current user has a specific permission
     * 
     * @param permission The permission to check
     * @return true if the user has the permission, false otherwise
     */
    private boolean hasPermission(String permission) {
        try {
            employeeService.isEmployeeAuthorised(doneBy, permission);
            return true;
        } catch (AuthorizationException e) {
            return false;
        } catch (Exception e) {
            System.out.println("Error checking permissions: " + e.getMessage());
            return false;
        }
    }


    //==========================================================================================
    // EMPLOYEE MANAGEMENT METHODS
    //==========================================================================================

    /**
     * Deactivates an employee
     */
    private void deactivateEmployee() {
        printSectionHeader("Deactivate Employee");

        // Show all employee
        System.out.println(CYAN + "Current employees:" + RESET);
        EmployeeSL[] employees = employeeService.getAllEmployees();
        for (EmployeeSL employee : employees) {
            String status = employee.isActive() ? GREEN + "Active" + RESET : RED + "Inactive" + RESET;
            System.out.printf("  • ID: %-9s | Name: %-20s | Status: %s%n", 
                employee.getIsraeliId(), 
                employee.getFullName(),
                status);
        }

        System.out.println();
        long israeliId = getLongInput("Employee Israeli ID: ");

        try {
            EmployeeSL employee = employeeService.getEmployeeById(israeliId);

            if (!employee.isActive()) {
                printError("Employee is already inactive.");
                waitForEnter();
                return;
            }

            // Confirm
            if (confirm("Confirm deactivating employee '" + employee.getFullName() + "' (ID: " + israeliId + ")?")) {
                String result = employeeService.deactivateEmployee(doneBy, israeliId);

                if (result.contains("successfully")) {
                    printSuccess(result);
                } else {
                    printError(result);
                }
            } else {
                System.out.println(YELLOW + "Operation cancelled." + RESET);
            }
        } catch (Exception e) {
            printError("Error retrieving employee details: " + e.getMessage());
        }

        waitForEnter();
    }

    private void printWelcomeBanner() {
        CliUtil.printWelcomeBanner("EMPLOYEE MANAGEMENT SYSTEM", LocalDate.now().toString(), "Employee #" + doneBy);
    }

    /**
     * Prints a section header with a border.
     *
     * @param title - The title of the section to be printed.
     */
    private void printSectionHeader(String title) {
        boolean isMainMenu = title.equalsIgnoreCase("Main Menu");
        CliUtil.printSectionHeader(title, isMainMenu, "SYSTEM");
    }

    private void createEmployee() {
        printSectionHeader("Create Employee");

        System.out.print(BOLD + "Israeli ID: " + RESET);
        long israeliId = Long.parseLong(scanner.nextLine());

        System.out.print(BOLD + "First Name: " + RESET);
        String firstName = scanner.nextLine();

        System.out.print(BOLD + "Last Name: " + RESET);
        String lastName = scanner.nextLine();

        System.out.print(BOLD + "Salary: " + RESET);
        long salary = Long.parseLong(scanner.nextLine());

        System.out.print(BOLD + "Start Date (dd-MM-yyyy): " + RESET);
        LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

        Map<String, Object> termsOfEmployment = new HashMap<>();
        System.out.println();
        System.out.println(CYAN + "📋 " + BOLD + "TERMS OF EMPLOYMENT" + RESET);
        System.out.println(CYAN + "Enter key-value pairs. Type 'done' when finished." + RESET);
        System.out.println();

        while (true) {
            System.out.print(BOLD + "Key (or 'done'): " + RESET);
            String key = scanner.nextLine();
            if (key.equalsIgnoreCase("done")) break;

            System.out.print(BOLD + "Value: " + RESET);
            String value = scanner.nextLine();
            termsOfEmployment.put(key, value);
            System.out.println(CYAN + "✓ Added: " + key + " = " + value + RESET);
        }

        String result = employeeService.createEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, startDate);

        if (result.contains("successfully")) {
            printSuccess(result);
        } else {
            printError(result);
        }

        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }
    private void updateEmployee() {
        printSectionHeader("Update Employee");
        System.out.print(BOLD + "Israeli ID: " + RESET);
        long israeliId = Long.parseLong(scanner.nextLine());

        EmployeeSL existing = employeeService.getEmployeeById(israeliId);
        if (existing == null) {
            printError("Employee not found.");
            return;
        }

        printSectionHeader("Edit Employee: " + YELLOW + existing.getFullName() + RESET);

        System.out.println(CYAN + "Leave field empty to keep current value" + RESET);
        System.out.println();

        System.out.println(BOLD + "Current First Name: " + RESET + existing.getFirstName());
        System.out.print(BOLD + "New First Name: " + RESET);
        String firstName = scanner.nextLine();
        if (firstName.isBlank()) firstName = existing.getFirstName();

        System.out.println(BOLD + "Current Last Name: " + RESET + existing.getLastName());
        System.out.print(BOLD + "New Last Name: " + RESET);
        String lastName = scanner.nextLine();
        if (lastName.isBlank()) lastName = existing.getLastName();

        System.out.println(BOLD + "Current Salary: " + RESET + existing.getSalary());
        System.out.print(BOLD + "New Salary: " + RESET);
        String salaryInput = scanner.nextLine();
        long salary = salaryInput.isBlank() ? existing.getSalary() : Long.parseLong(salaryInput);

        System.out.println(BOLD + "Current Active Status: " + RESET + existing.isActive());
        System.out.print(BOLD + "Is Active? (true/false): " + RESET);
        String activeInput = scanner.nextLine();
        boolean active = activeInput.isBlank() ? existing.isActive() : Boolean.parseBoolean(activeInput);

        System.out.println();
        System.out.println(CYAN + "📋 " + BOLD + "CURRENT TERMS OF EMPLOYMENT" + RESET);
        if (existing.getTermsOfEmployment().isEmpty()) {
            System.out.println(YELLOW + "  No terms defined" + RESET);
        } else {
            existing.getTermsOfEmployment().forEach((k, v) -> 
                System.out.println("  " + BOLD + k + RESET + ": " + v));
        }

        System.out.println();
        System.out.println(CYAN + "📝 " + BOLD + "UPDATE TERMS OF EMPLOYMENT" + RESET);
        System.out.println(CYAN + "Enter key-value pairs. Type 'done' when finished." + RESET);
        System.out.println(CYAN + "To remove a term, enter its key with an empty value." + RESET);
        System.out.println();

        Map<String, Object> termsOfEmployment = new HashMap<>(existing.getTermsOfEmployment());
        while (true) {
            System.out.print(BOLD + "Key (or 'done'): " + RESET);
            String key = scanner.nextLine();
            if (key.equalsIgnoreCase("done")) break;

            System.out.print(BOLD + "Value: " + RESET);
            String value = scanner.nextLine();

            if (value.isEmpty()) {
                termsOfEmployment.remove(key);
                System.out.println(YELLOW + "✓ Removed: " + key + RESET);
            } else {
                termsOfEmployment.put(key, value);
                System.out.println(CYAN + "✓ Updated: " + key + " = " + value + RESET);
            }
        }

        String result = employeeService.updateEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, active);

        if (result.contains("successfully")) {
            printSuccess(result);
        } else {
            printError(result);
        }

        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }
    private void addRoleToEmployee() {
        printSectionHeader("Add Role to Employee");

        String[] allRoles = employeeService.getAllRoles();

        System.out.print(BOLD + "Employee Israeli ID: " + RESET);
        long israeliId = Long.parseLong(scanner.nextLine());

        try {
            EmployeeSL employee = employeeService.getEmployeeById(israeliId);
            System.out.println(GREEN + "Employee: " + RESET + employee.getFullName());

            System.out.println();
            System.out.println(CYAN + "👤 " + BOLD + "CURRENT ROLES" + RESET);
            if (employee.getRoles().isEmpty()) {
                System.out.println(YELLOW + "  No roles assigned" + RESET);
            } else {
                for (String role : employee.getRoles()) {
                    System.out.println("  • " + role);
                }
            }
        } catch (Exception e) {
            printError("Could not retrieve employee details: " + e.getMessage());
        }

        System.out.println();
        System.out.println(CYAN + "🔑 " + BOLD + "AVAILABLE ROLES" + RESET);
        for (int i = 0; i < allRoles.length; i++) {
            System.out.println("  • " + allRoles[i]);
        }

        System.out.println();
        System.out.print(BOLD + "Role Name to Add: " + RESET);
        String roleName = scanner.nextLine();

        // Confirm
        System.out.println();
        System.out.print(YELLOW + "Confirm adding role '" + roleName + "' to employee #" + israeliId + "? (y/n): " + RESET);
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            String result = employeeService.addRoleToEmployee(doneBy, israeliId, roleName);

            if (result.contains("successfully")) {
                printSuccess(result);
            } else {
                printError(result);
            }
        } else {
            System.out.println(YELLOW + "Operation cancelled." + RESET);
        }

        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    private void removeRoleFromEmployee() {
        printSectionHeader("Remove Role from Employee");

        System.out.print(BOLD + "Employee Israeli ID: " + RESET);
        long israeliId = Long.parseLong(scanner.nextLine());

        try {
            EmployeeSL employee = employeeService.getEmployeeById(israeliId);
            System.out.println(GREEN + "Employee: " + RESET + employee.getFullName());

            System.out.println();
            System.out.println(CYAN + "👤 " + BOLD + "CURRENT ROLES" + RESET);
            if (employee.getRoles().isEmpty()) {
                System.out.println(YELLOW + "  No roles assigned" + RESET);
                System.out.println(YELLOW + "Press Enter to continue..." + RESET);
                scanner.nextLine();
                return;
            } else {
                for (String role : employee.getRoles()) {
                    System.out.println("  • " + role);
                }
            }

            System.out.println();
            System.out.print(BOLD + "Role Name to Remove: " + RESET);
            String roleName = scanner.nextLine();

            // Confirm
            System.out.println();
            System.out.print(YELLOW + "Confirm removing role '" + roleName + "' from employee #" + israeliId + "? (y/n): " + RESET);
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                String result = employeeService.removeRoleFromEmployee(doneBy, israeliId, roleName);

                if (result.contains("successfully")) {
                    printSuccess(result);
                } else {
                    printError(result);
                }
            } else {
                System.out.println(YELLOW + "Operation cancelled." + RESET);
            }
        } catch (Exception e) {
            printError("Could not retrieve employee details: " + e.getMessage());
        }

        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    private void createRole() {
        printSectionHeader("Create Role");

        System.out.print(BOLD + "Role Name: " + RESET);
        String roleName = scanner.nextLine();

        System.out.println();
        System.out.print(BOLD + "Do you want to add permissions now? (yes/no): " + RESET);
        String addPermissions = scanner.nextLine();

        if (addPermissions.equalsIgnoreCase("yes")) {
            System.out.println();
            System.out.println(CYAN + "🔑 " + BOLD + "AVAILABLE PERMISSIONS" + RESET);
            String[] allPermissions = employeeService.getAllPermissions();

            if (allPermissions.length == 0) {
                System.out.println(YELLOW + "  No permissions defined in the system" + RESET);
            } else {
                for (String permission : allPermissions) {
                    System.out.println("  • " + permission);
                }
            }

            System.out.println();
            System.out.println(CYAN + "📝 " + BOLD + "ADD PERMISSIONS TO ROLE" + RESET);
            System.out.println(CYAN + "Enter permissions one by one. Type 'done' when finished." + RESET);
            System.out.println();

            HashSet<String> permissions = new HashSet<>();
            while (true) {
                System.out.print(BOLD + "Permission (or 'done'): " + RESET);
                String permission = scanner.nextLine();
                if (permission.equalsIgnoreCase("done")) break;
                permissions.add(permission);
                System.out.println(CYAN + "✓ Added: " + permission + RESET);
            }

            // Confirm
            System.out.println();
            System.out.print(YELLOW + "Confirm creating role '" + roleName + "' with " + permissions.size() + " permissions? (y/n): " + RESET);
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                String result = employeeService.createRoleWithPermissions(doneBy, roleName, permissions);

                if (result.contains("successfully")) {
                    printSuccess(result);
                } else {
                    printError(result);
                }
            } else {
                System.out.println(YELLOW + "Operation cancelled." + RESET);
            }
        } else {
            // Confirm
            System.out.println();
            System.out.print(YELLOW + "Confirm creating role '" + roleName + "' with no permissions? (y/n): " + RESET);
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                // Create role without permissions
                String result = employeeService.createRole(doneBy, roleName);

                if (result.contains("successfully")) {
                    printSuccess(result);
                } else {
                    printError(result);
                }
            } else {
                System.out.println(YELLOW + "Operation cancelled." + RESET);
            }
        }

        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    private void addPermissionToRole() {
        printSectionHeader("Add Permission to Role");

        // Show roles
        System.out.println(CYAN + "Available roles:" + RESET);
        String[] allRoles = employeeService.getAllRoles();
        for (String role : allRoles) {
            System.out.println("  • " + role);
        }

        System.out.println();
        System.out.print(BOLD + "Role Name: " + RESET);
        String roleName = scanner.nextLine();

        // Show permissions
        System.out.println();
        System.out.println(CYAN + "Available permissions:" + RESET);
        String[] allPermissions = employeeService.getAllPermissions();
        for (String permission : allPermissions) {
            System.out.println("  • " + permission);
        }

        System.out.println();
        System.out.print(BOLD + "Permission Name: " + RESET);
        String permissionName = scanner.nextLine();

        // Confirm
        System.out.println();
        System.out.print(YELLOW + "Confirm adding permission '" + permissionName + "' to role '" + roleName + "'? (y/n): " + RESET);
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            String result = employeeService.addPermissionToRole(doneBy, roleName, permissionName);

            if (result.contains("successfully")) {
                printSuccess(result);
            } else {
                printError(result);
            }
        } else {
            System.out.println(YELLOW + "Operation cancelled." + RESET);
        }

        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    private void removePermissionFromRole() {
        printSectionHeader("Remove Permission from Role");

        System.out.println(CYAN + "Available roles:" + RESET);
        String[] allRoles = employeeService.getAllRoles();
        for (String role : allRoles) {
            System.out.println("  • " + role);
        }

        System.out.println();
        System.out.print(BOLD + "Role Name: " + RESET);
        String roleName = scanner.nextLine();

        try {
            Map<String, HashSet<String>> roleDetails = employeeService.getRoleDetails(roleName);
            if (roleDetails != null && !roleDetails.isEmpty()) {
                HashSet<String> permissions = roleDetails.get(roleName);

                System.out.println();
                System.out.println(CYAN + "Current permissions for role '" + roleName + "':" + RESET);

                if (permissions == null || permissions.isEmpty()) {
                    System.out.println(YELLOW + "  No permissions assigned to this role" + RESET);
                    System.out.println(YELLOW + "Press Enter to continue..." + RESET);
                    scanner.nextLine();
                    return;
                } else {
                    for (String permission : permissions) {
                        System.out.println("  • " + permission);
                    }
                }

                System.out.println();
                System.out.print(BOLD + "Permission Name to Remove: " + RESET);
                String permissionName = scanner.nextLine();

                // Confirm
                System.out.println();
                System.out.print(YELLOW + "Confirm removing permission '" + permissionName + "' from role '" + roleName + "'? (y/n): " + RESET);
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                    String result = employeeService.removePermissionFromRole(doneBy, roleName, permissionName);

                    if (result.contains("successfully")) {
                        printSuccess(result);
                    } else {
                        printError(result);
                    }
                } else {
                    System.out.println(YELLOW + "Operation cancelled." + RESET);
                }
            } else {
                printError("Role '" + roleName + "' not found.");
            }
        } catch (Exception e) {
            printError("Error retrieving role details: " + e.getMessage());
        }

        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    /**
     * Displays all employees in the system
     */
    private void printAllEmployees() {
        printSectionHeader("All Employees");

        EmployeeSL[] employees = employeeService.getAllEmployees();

        if (employees.length == 0) {
            System.out.println(YELLOW + "No employees found in the system." + RESET);
        } else {
            System.out.println(CYAN + "Found " + employees.length + " employees:" + RESET);
            System.out.println();

            System.out.println(BOLD + "┌─────────────┬────────────────────────────┬──────────┐" + RESET);
            System.out.println(BOLD + "│     ID      │            Name            │  Status  │" + RESET);
            System.out.println(BOLD + "├─────────────┼────────────────────────────┼──────────┤" + RESET);

            for (EmployeeSL employee : employees) {
                String status = employee.isActive() ? GREEN + "Active" + RESET : RED + "Inactive" + RESET;
                System.out.printf("│ %-11s │ %-28s │ %-8s │%n", 
                    employee.getIsraeliId(), 
                    employee.getFullName(),
                    status);
            }

            // Print table footer
            System.out.println(BOLD + "└─────────────┴────────────────────────────┴──────────┘" + RESET);
        }

        waitForEnter();
    }

    /**
     * Displays detailed information about a specific employee
     * 
     * @param israeliId The ID of the employee to display
     */
    private void printEmployeeDetails(long israeliId) {
        printSectionHeader("Employee Details");

        try {
            EmployeeSL employee = employeeService.getEmployeeById(israeliId);

            System.out.println(BLUE + "┌───────────────────────────────────────────────────┐" + RESET);
            System.out.println(BLUE + "│" + RESET + BOLD + YELLOW + "                 EMPLOYEE INFORMATION              " + RESET + BLUE + "│" + RESET);
            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);

            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "ID:", employee.getIsraeliId());
            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Name:", employee.getFullName());
            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Salary:", employee.getSalary());

            String status = employee.isActive()
                ? GREEN + "Active" + RESET 
                : RED + "Inactive" + RESET;
            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Status:", status);

            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Start Date:", employee.getStartOfEmployment());

            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);
            System.out.println(BLUE + "│" + RESET + BOLD + YELLOW + "                  ROLES & PERMISSIONS              " + RESET + BLUE + "│" + RESET);
            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);

            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Roles:", "");
            if (employee.getRoles().isEmpty()) {
                System.out.printf(BLUE + "│ " + RESET + "%-15s" + "│ %-33s " + BLUE + "│%n" + RESET, "", YELLOW + "No roles assigned" + RESET);
            } else {
                boolean first = true;
                for (String role : employee.getRoles()) {
                    if (first) {
                        System.out.printf(BLUE + "│ " + RESET + "%-15s" + "│ %-33s " + BLUE + "│%n" + RESET, "", "• " + role);
                        first = false;
                    } else {
                        System.out.printf(BLUE + "│ " + RESET + "%-15s" + "│ %-33s " + BLUE + "│%n" + RESET, "", "• " + role);
                    }
                }
            }

            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);
            System.out.println(BLUE + "│" + RESET + BOLD + YELLOW + "               TERMS OF EMPLOYMENT               " + RESET + BLUE + "│" + RESET);
            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);

            if (employee.getTermsOfEmployment().isEmpty()) {
                System.out.printf(BLUE + "│ " + RESET + "%-49s " + BLUE + "│%n" + RESET, YELLOW + "  No terms of employment defined" + RESET);
            } else {
                for (Map.Entry<String, Object> entry : employee.getTermsOfEmployment().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();

                    // Handle long values by splitting them
                    if (key.length() + value.length() > 45) {
                        System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "%-34s " + BLUE + "│%n" + RESET, key + ":", value.substring(0, Math.min(value.length(), 34)));

                        // Print remaining value on next lines if needed
                        String remaining = value.substring(Math.min(value.length(), 34));
                        while (remaining.length() > 0) {
                            String chunk = remaining.substring(0, Math.min(remaining.length(), 49));
                            System.out.printf(BLUE + "│ " + RESET + "%-49s " + BLUE + "│%n" + RESET, chunk);
                            remaining = remaining.substring(Math.min(remaining.length(), 49));
                        }
                    } else {
                        System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "%-34s " + BLUE + "│%n" + RESET, key + ":", value);
                    }
                }
            }

            System.out.println(BLUE + "└───────────────────────────────────────────────────┘" + RESET);

        } catch (Exception e) {
            printError("Employee not found or error retrieving details: " + e.getMessage());
        }

        waitForEnter();
    }
    //==========================================================================================
    // ROLE MANAGEMENT METHODS
    //==========================================================================================

    /**
     * Displays all roles in the system
     */
    private void printAllRoles() {
        printSectionHeader("All Roles");

        String[] roles = employeeService.getAllRoles();

        if (roles.length == 0) {
            System.out.println(YELLOW + "No roles found in the system." + RESET);
        } else {
            System.out.println(CYAN + "Found " + roles.length + " roles:" + RESET);
            System.out.println();

            System.out.println(BOLD + "┌────────────────────────────────────────────┐" + RESET);
            System.out.println(BOLD + "│                  ROLE NAME                 │" + RESET);
            System.out.println(BOLD + "├────────────────────────────────────────────┤" + RESET);

            for (String role : roles) {
                System.out.printf("│ %-42s │%n", role);
            }

            System.out.println(BOLD + "└────────────────────────────────────────────┘" + RESET);
            System.out.println();
            System.out.println(CYAN + "💡 " + RESET + "Use 'View Role Details' to see permissions for each role");
        }

        waitForEnter();
    }

    /**
     * Displays detailed information about a specific role
     * 
     * @param roleName The name of the role to display
     */
    private void printRoleDetails(String roleName) {
        printSectionHeader("Role Details");

        try {
            Map<String, HashSet<String>> roleDetails = employeeService.getRoleDetails(roleName);

            if (roleDetails != null && !roleDetails.isEmpty()) {
                System.out.println(PURPLE + "┌───────────────────────────────────────────────────┐" + RESET);
                System.out.println(PURPLE + "│" + RESET + BOLD + YELLOW + "                    ROLE DETAILS                  " + RESET + PURPLE + "│" + RESET);
                System.out.println(PURPLE + "├───────────────────────────────────────────────────┤" + RESET);

                System.out.printf(PURPLE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + PURPLE + "│%n" + RESET, "Role Name:", roleName);

                System.out.println(PURPLE + "├───────────────────────────────────────────────────┤" + RESET);
                System.out.println(PURPLE + "│" + RESET + BOLD + YELLOW + "                    PERMISSIONS                   " + RESET + PURPLE + "│" + RESET);
                System.out.println(PURPLE + "├───────────────────────────────────────────────────┤" + RESET);

                HashSet<String> permissions = roleDetails.get(roleName);
                if (permissions == null || permissions.isEmpty()) {
                    System.out.printf(PURPLE + "│ " + RESET + "%-49s " + PURPLE + "│%n" + RESET, YELLOW + "  No permissions assigned to this role" + RESET);
                } else {
                    for (String permission : permissions) {
                        System.out.printf(PURPLE + "│ " + RESET + "  • %-45s " + PURPLE + "│%n" + RESET, permission);
                    }
                }

                System.out.println(PURPLE + "└───────────────────────────────────────────────────┘" + RESET);
            } else {
                printError("Role '" + roleName + "' not found.");
            }
        } catch (Exception e) {
            printError("Error retrieving role details: " + e.getMessage());
        }

        waitForEnter();
    }

    //==========================================================================================
    // PERMISSION MANAGEMENT METHODS
    //==========================================================================================

    /**
     * Displays all permissions in the system
     */
    private void printAllPermissions() {
        printSectionHeader("All Permissions");

        String[] permissions = employeeService.getAllPermissions();

        if (permissions.length == 0) {
            System.out.println(YELLOW + "No permissions found in the system." + RESET);
        } else {
            System.out.println(CYAN + "Found " + permissions.length + " permissions:" + RESET);
            System.out.println();

            System.out.println(BOLD + "┌────────────────────────────────────────────┐" + RESET);
            System.out.println(BOLD + "│              PERMISSION NAME               │" + RESET);
            System.out.println(BOLD + "├────────────────────────────────────────────┤" + RESET);

            for (String permission : permissions) {
                System.out.printf("│ %-42s │%n", permission);
            }

            System.out.println(BOLD + "└────────────────────────────────────────────┘" + RESET);
        }

        waitForEnter();
    }

    /**
     * Creates a new permission in the system
     */
    private void createPermission() {
        printSectionHeader("Create Permission");

        // Show permissions
        String[] existingPermissions = employeeService.getAllPermissions();
        if (existingPermissions.length > 0) {
            System.out.println(CYAN + "Existing permissions in the system:" + RESET);
            for (String permission : existingPermissions) {
                System.out.println("  • " + permission);
            }
            System.out.println();
        }

        System.out.print(BOLD + "New Permission Name: " + RESET);
        String permissionName = scanner.nextLine();

        // Confirm
        if (confirm("Confirm creating permission '" + permissionName + "'?")) {
            String result = employeeService.createPermission(doneBy, permissionName);

            if (result.contains("successfully")) {
                printSuccess(result);
            } else {
                printError(result);
            }
        } else {
            System.out.println(YELLOW + "Operation cancelled." + RESET);
        }

        waitForEnter();
    }

    /**
     * Clones an existing role with all its permissions
     */
    private void cloneRole() {
        printSectionHeader("Clone Role");

        // Show roles
        String[] existingRoles = employeeService.getAllRoles();
        if (existingRoles.length == 0) {
            printError("No roles exist in the system to clone.");
            waitForEnter();
            return;
        }

        System.out.println(CYAN + "Available roles to clone:" + RESET);
        for (String role : existingRoles) {
            System.out.println("  • " + role);
        }
        System.out.println();

        System.out.print(BOLD + "Source Role Name: " + RESET);
        String existingRoleName = scanner.nextLine();

        try {
            Map<String, HashSet<String>> roleDetails = employeeService.getRoleDetails(existingRoleName);
            if (roleDetails == null || roleDetails.isEmpty()) {
                printError("Source role '" + existingRoleName + "' not found.");
                waitForEnter();
                return;
            }

            // Show permissions that will be cloned
            HashSet<String> permissions = roleDetails.get(existingRoleName);
            System.out.println();
            System.out.println(CYAN + "Permissions that will be cloned:" + RESET);
            if (permissions == null || permissions.isEmpty()) {
                System.out.println(YELLOW + "  No permissions in source role" + RESET);
            } else {
                for (String permission : permissions) {
                    System.out.println("  • " + permission);
                }
            }
        } catch (Exception e) {
            printError("Error retrieving role details: " + e.getMessage());
            waitForEnter();
            return;
        }

        System.out.println();
        System.out.print(BOLD + "New Role Name: " + RESET);
        String newRoleName = scanner.nextLine();

        // Confirm
        if (confirm("Confirm cloning role '" + existingRoleName + "' to new role '" + newRoleName + "'?")) {
            String result = employeeService.cloneRole(doneBy, existingRoleName, newRoleName);

            if (result.contains("successfully")) {
                printSuccess(result);
            } else {
                printError(result);
            }
        } else {
            System.out.println(YELLOW + "Operation cancelled." + RESET);
        }

        waitForEnter();
    }
}
