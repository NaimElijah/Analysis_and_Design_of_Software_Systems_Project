package PresentationLayer;

import DomainLayer.exception.UnauthorizedPermissionException;
import ServiceLayer.EmployeeService;
import ServiceLayer.ShiftService;

import java.time.LocalDate;
import java.util.Scanner;

public class MainCLI {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final long doneBy;
    private final Scanner scanner;

    private final EmployeeCLI employeeCLI;
    private final AssignmentCLI assignmentCLI;
    private final AvailabillityCLI availabilityCLI;
    private final ShiftCLI shiftCLI;

    public MainCLI(EmployeeService employeeService, ShiftService shiftService, long doneBy) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.doneBy = doneBy;
        this.scanner = new Scanner(System.in);

        this.employeeCLI = new EmployeeCLI(employeeService, doneBy);
        this.assignmentCLI = new AssignmentCLI(shiftService, employeeService, doneBy);
        this.availabilityCLI = new AvailabillityCLI(shiftService, doneBy);
        this.shiftCLI = new ShiftCLI(shiftService, employeeService, doneBy);
    }

    public void start() {
        printWelcomeBanner();
        boolean running = true;

        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            running = processMenuChoice(choice);
        }

        CliUtil.printInfo("Thank you for using the Employee Module. Goodbye!");
    }

    private void displayMenu() {
        CliUtil.printSectionHeader("Main Menu", true, "Employee Module");
        int option = 1;

        if (hasPermission("EDIT_EMPLOYEE") || hasPermission("VIEW_EMPLOYEE")) {
            System.out.println(CliUtil.YELLOW + option++ + CliUtil.RESET + ". Employees");
        }

        if (hasPermission("EDIT_SHIFT") || hasPermission("VIEW_SHIFT")) {
            System.out.println(CliUtil.YELLOW + option++ + CliUtil.RESET + ". Shifts");
        }

        if (hasPermission("ASSIGN_EMPLOYEE")) {
            System.out.println(CliUtil.YELLOW + option++ + CliUtil.RESET + ". Assignment Board");
        }

        if (hasPermission("UPDATE_AVAILABLE")) {
            System.out.println(CliUtil.YELLOW + option++ + CliUtil.RESET + ". Availability Board");
        }

        System.out.println("0. Exit");
        CliUtil.printPrompt("Enter your choice: ");
    }

    private boolean processMenuChoice(String choice) {
        try {
            switch (choice) {
                case "1":
                    if (hasPermission("EDIT_EMPLOYEE") || hasPermission("VIEW_EMPLOYEE")) {
                        employeeCLI.start();
                    } else if (hasPermission("EDIT_SHIFT") || hasPermission("VIEW_SHIFT")) {
                        shiftCLI.start();
                    } else {
                        CliUtil.printWarning("You do not have permission.");
                    }
                    break;
                case "2":
                    if ((hasPermission("EDIT_EMPLOYEE") || hasPermission("VIEW_EMPLOYEE")) &&
                            (hasPermission("EDIT_SHIFT") || hasPermission("VIEW_SHIFT"))) {
                        shiftCLI.start();
                    } else if (hasPermission("UPDATE_AVAILABLE")) {
                        availabilityCLI.start();
                    } else {
                        CliUtil.printWarning("You do not have permission.");
                    }
                    break;
                case "3":
                    if (hasPermission("ASSIGN_EMPLOYEE")) {
                        assignmentCLI.start();
                    } else if (hasPermission("UPDATE_AVAILABLE") &&
                            (hasPermission("EDIT_EMPLOYEE") || hasPermission("VIEW_EMPLOYEE"))) {
                        availabilityCLI.start();
                    } else {
                        CliUtil.printWarning("You do not have permission.");
                    }
                    break;
                case "4":
                    if (hasPermission("UPDATE_AVAILABLE") &&
                            (hasPermission("EDIT_EMPLOYEE") || hasPermission("VIEW_EMPLOYEE") || hasPermission("ASSIGN_EMPLOYEE"))) {
                        availabilityCLI.start();
                    } else {
                        CliUtil.printWarning("You do not have permission.");
                    }
                    break;
                case "0":
                    return false;
                default:
                    CliUtil.printError("Invalid choice.");
            }
        } catch (UnauthorizedPermissionException e) {
            CliUtil.printError("Access denied: " + e.getMessage());
        }
        return true;
    }

    private void printWelcomeBanner() {
        String currentDate = LocalDate.now().format(CliUtil.dateFormatter);
        String userInfo = employeeService.getEmployeeById(doneBy).getFullName();
        CliUtil.printWelcomeBanner("Employee Module", currentDate, userInfo);
    }

    private boolean hasPermission(String permission) {
        return employeeService.hasPermission(doneBy, permission);
    }
}
