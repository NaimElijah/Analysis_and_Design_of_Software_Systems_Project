package PresentationLayer;

import ServiceLayer.EmployeeService;
import ServiceLayer.ShiftService;

import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class MainCLI {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;
    private final long doneBy;
    private final Scanner scanner;

    private final EmployeeCLI employeeCLI;
    private final AssigmentCLI assignmentCLI;
    private final AvailabillityCLI availabilityCLI;

    public MainCLI(EmployeeService employeeService, ShiftService shiftService, long doneBy) {
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.doneBy = doneBy;
        this.scanner = new Scanner(System.in);

        // Initialize sub-CLI classes
        this.employeeCLI = new EmployeeCLI(employeeService, doneBy);
        this.assignmentCLI = new AssigmentCLI(shiftService, employeeService, doneBy);
        this.availabilityCLI = new AvailabillityCLI(shiftService, doneBy);
    }

    public void start() {
        printWelcomeBanner();
        boolean running = true;

        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            running = processMenuChoice(choice);
        }

        System.out.println("Thank you for using the Employee Module. Goodbye!");
    }

    private void displayMenu() {
        System.out.println("\n===== Employee Module Main Menu =====");
        System.out.println("1. Employee Management");
        System.out.println("2. Shift Assignment");
        System.out.println("3. Availability Management");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private boolean processMenuChoice(String choice) {
        switch (choice) {
            case "1":
                employeeCLI.start();
                return true;
            case "2":
                assignmentCLI.start();
                return true;
            case "3":
                availabilityCLI.start();
                return true;
            case "0":
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
                return true;
        }
    }

    private void printWelcomeBanner() {
        CliUtil.printWelcomeBanner("Employee Module", LocalDate.now().format(CliUtil.dateFormatter).toString(), employeeService.getEmployeeById(doneBy).getFullName());
    }
}
