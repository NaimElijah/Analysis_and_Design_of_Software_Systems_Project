package PresentationLayer;

import ServiceLayer.EmployeeSL;
import ServiceLayer.EmployeeService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class EmployeeCLI {
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private final long doneBy;

    public EmployeeCLI(EmployeeService employeeService, long doneBy) {
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
        this.doneBy = doneBy;
    }

    public void start() {
        printWelcomeBanner();

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. View All Employees");
            System.out.println("2. View Employee Details");
            System.out.println("3. Create Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Add Role to Employee");
            System.out.println("6. Remove Role from Employee");
            System.out.println("7. Create Role");
            System.out.println("8. Add Permission to Role");
            System.out.println("9. Remove Permission from Role");
            System.out.println("10. View All Roles");
            System.out.println("11. View Role Details");
            System.out.println("12. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    printAllEmployees();
                    break;
                case "2":
                    System.out.print("Enter Employee Israeli ID: ");
                    long israeliId = Long.parseLong(scanner.nextLine());
                    printEmployeeDetails(israeliId);
                    break;
                case "3":
                    createEmployee();
                    break;
                case "4":
                    updateEmployee();
                    break;
                case "5":
                    addRoleToEmployee();
                    break;
                case "6":
                    removeRoleFromEmployee();
                    break;
                case "7":
                    createRole();
                    break;
                case "8":
                    addPermissionToRole();
                    break;
                case "9":
                    removePermissionFromRole();
                    break;
                case "10":
                    printAllRoles();
                    break;
                case "11":
                    System.out.print("Enter Role Name: ");
                    String roleName = scanner.nextLine();
                    printRoleDetails(roleName);
                    break;
                case "12":
                    System.out.println("Exiting back to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printWelcomeBanner() {
        System.out.println("+==============================================+");
        System.out.println("|                                              |");
        System.out.println("|      WELCOME TO EMPLOYEE MANAGEMENT SYSTEM   |");
        System.out.println("|                                              |");
        System.out.println("+==============================================+");
    }

    /**
     * Prints a section header with a border.
     *
     * @param title - The title of the section to be printed.
     */
    private void printSectionHeader(String title) {
        String border = "+------------------------------+";
        String formatted = String.format("| %1$-28s |", title.toUpperCase());
        System.out.println(border);
        System.out.println(formatted);
        System.out.println(border);
    }

    private void createEmployee() {
        printSectionHeader("Create Employee");
        System.out.print("Israeli ID: ");
        long israeliId = Long.parseLong(scanner.nextLine());
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Salary: ");
        long salary = Long.parseLong(scanner.nextLine());
        System.out.print("Start Date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        Map<String, Object> termsOfEmployment = new HashMap<>();
        System.out.println("Add Terms of Employment (key-value pairs). Type 'done' to finish.");
        while (true) {
            System.out.print("Key (or 'done'): ");
            String key = scanner.nextLine();
            if (key.equalsIgnoreCase("done")) break;

            System.out.print("Value: ");
            String value = scanner.nextLine();
            termsOfEmployment.put(key, value);
        }

        String result = employeeService.createEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, startDate);
        System.out.println(result);
    }
    private void updateEmployee() {
        printSectionHeader("Update Employee");
        System.out.print("Israeli ID: ");
        long israeliId = Long.parseLong(scanner.nextLine());

        // Load existing employee (adjust method name as needed)
        EmployeeSL existing = employeeService.getEmployeeById(israeliId);
        if (existing == null) {
            System.out.println("Employee not found.");
            return;
        }
        printSectionHeader("Edit Employee: " + existing.getFullName());

        System.out.println("Current First Name: " + existing.getFirstName());
        System.out.print("New First Name: ");
        String firstName = scanner.nextLine();
        if (firstName.isBlank()) firstName = existing.getFirstName();

        System.out.println("Current Last Name: " + existing.getLastName());
        System.out.print("New Last Name: ");
        String lastName = scanner.nextLine();
        if (lastName.isBlank()) lastName = existing.getLastName();

        System.out.println("Current Salary: " + existing.getSalary());
        System.out.print("New Salary: ");
        String salaryInput = scanner.nextLine();
        long salary = salaryInput.isBlank() ? existing.getSalary() : Long.parseLong(salaryInput);

        System.out.println("Current Active Status: " + existing.isActive());
        System.out.print("Is Active? (true/false): ");
        String activeInput = scanner.nextLine();
        boolean active = activeInput.isBlank() ? existing.isActive() : Boolean.parseBoolean(activeInput);

        // Show current terms
        System.out.println("\nCurrent Terms of Employment:");
        existing.getTermsOfEmployment().forEach((k, v) -> System.out.println(" - " + k + ": " + v));

        // Prompt to update terms
        Map<String, Object> termsOfEmployment = new HashMap<>(existing.getTermsOfEmployment());
        System.out.println("\nAdd or update terms. Type 'done' to finish.");
        while (true) {
            System.out.print("Key (or 'done'): ");
            String key = scanner.nextLine();
            if (key.equalsIgnoreCase("done")) break;

            System.out.print("Value: ");
            String value = scanner.nextLine();
            termsOfEmployment.put(key, value);
        }

        String result = employeeService.updateEmployee(doneBy, israeliId, firstName, lastName, salary, termsOfEmployment, active);
        System.out.println(result);
    }
    private void addRoleToEmployee() {
        printSectionHeader("Add Role to Employee");
        System.out.print("Employee Israeli ID: ");
        long israeliId = Long.parseLong(scanner.nextLine());
        System.out.print("Role Name: ");
        String roleName = scanner.nextLine();

        String result = employeeService.addRoleToEmployee(doneBy, israeliId, roleName);
        System.out.println(result);
    }

    private void removeRoleFromEmployee() {
        printSectionHeader("Remove Role from Employee");
        System.out.print("Employee Israeli ID: ");
        long israeliId = Long.parseLong(scanner.nextLine());
        System.out.print("Role Name: ");
        String roleName = scanner.nextLine();

        String result = employeeService.removeRoleFromEmployee(doneBy, israeliId, roleName);
        System.out.println(result);
    }

    private void createRole() {
        printSectionHeader("Create Role");
        System.out.print("Role Name: ");
        String roleName = scanner.nextLine();

        String result = employeeService.CreateRole(doneBy, roleName);
        System.out.println(result);
    }

    private void addPermissionToRole() {
        printSectionHeader("Add Permission to Role");
        long doneBy = Long.parseLong(scanner.nextLine());
        System.out.print("Role Name: ");
        String roleName = scanner.nextLine();
        System.out.print("Permission Name: ");
        String permissionName = scanner.nextLine();

        String result = employeeService.addPermissionToRole(doneBy, roleName, permissionName);
        System.out.println(result);
    }

    private void removePermissionFromRole() {
        printSectionHeader("Remove Permission from Role");
        System.out.print("Role Name: ");
        String roleName = scanner.nextLine();
        System.out.print("Permission Name: ");
        String permissionName = scanner.nextLine();

        String result = employeeService.removePermissionFromRole(doneBy, roleName, permissionName);
        System.out.println(result);
    }

    private void printAllEmployees() {
        printSectionHeader("All Employees");
        for (EmployeeSL employee : employeeService.getAllEmployees()) {
            System.out.println(employee.getFullName() + " (ID: " + employee.getIsraeliId() + ")");
        }
    }
    private void printEmployeeDetails(long israeliId) {
        EmployeeSL employee = employeeService.getEmployeeById(israeliId);
        if (employee != null) {
            System.out.println("Employee Details:");
            System.out.println("ID: " + employee.getIsraeliId());
            System.out.println("Name: " + employee.getFullName());
            System.out.println("Salary: " + employee.getSalary());
            System.out.println("Active: " + employee.isActive());
            System.out.println("Start Date: " + employee.getStartOfEmployment());
            System.out.println("Terms of Employment: " + employee.getTermsOfEmployment());
        } else {
            System.out.println("Employee not found.");
        }
    }
    private void printAllRoles() {
        printSectionHeader("All Roles");
        for (String role : employeeService.getAllRoles()) {
            System.out.println(role);
        }
    }

    private void printRoleDetails(String roleName) {
        Map<String, HashSet<String>> roleDetails = employeeService.getRoleDetails(roleName);
        if (roleDetails != null) {
            System.out.println("Role Details:");
            System.out.println("Name: " + roleName);
            System.out.println("Permissions: " + roleDetails.get("permissions"));
            System.out.println("Employees: " + roleDetails.get("employees"));
        } else {
            System.out.println("Role not found.");
        }
    }
}