package PresentationLayer;

import ServiceLayer.ShiftSL;
import ServiceLayer.ShiftService;
import ServiceLayer.EmployeeService;
import ServiceLayer.EmployeeSL;
import ServiceLayer.exception.AuthorizationException;
import ServiceLayer.exception.EmployeeNotFoundException;
import ServiceLayer.exception.ServiceException;
import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

public class AssigmentCLI {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    // Properties
    private final ShiftService shiftService;
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private final long doneBy;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor for the Assignment Command Line Interface
     * 
     * @param shiftService The service layer for shift operations
     * @param employeeService The service layer for employee operations
     * @param doneBy The ID of the employee using the CLI
     */
    public AssigmentCLI(ShiftService shiftService, EmployeeService employeeService, long doneBy) {
        this.shiftService = shiftService;
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
        printSectionHeader("Assignment Management Menu");

        List<String> menuOptions = new ArrayList<>();
        int optionNumber = 1;

        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View All Shifts");
        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View Shift Details");

        if (hasPermission("ASSIGN_EMPLOYEE")) {
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". Shift Management");
        }

        menuOptions.add(YELLOW + optionNumber++ + RESET + ". View Employee Assignments");
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

            // View All Shifts
            if (choiceNum == currentOption++) {
                viewAllShifts();
                return;
            }

            // View Shift Details
            if (choiceNum == currentOption++) {
                viewShiftDetails();
                return;
            }

            // Shift Management
            if (hasPermission("ASSIGN_EMPLOYEE")) {
                if (choiceNum == currentOption++) {
                    shiftManagement();
                    return;
                }
            }

            // View Employee Assignments
            if (choiceNum == currentOption++) {
                viewEmployeeAssignments();
                return;
            }

            // Exit
            if (choiceNum == currentOption++) {
                System.out.println(BOLD + YELLOW + "Returning to main menu..." + RESET);
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
     * Formats employee display with name and number
     *
     * @param employeeId The ID of the employee
     * @return A formatted string with employee name and number
     */
    private String formatEmployeeDisplay(long employeeId) {
        try {
            EmployeeSL employee = employeeService.getEmployeeById(employeeId);
            return employee.getFullName() + " (#" + employeeId + ")";
        } catch (ServiceException e) {
            // If we can't get the employee name, just return the ID
            return "Employee #" + employeeId;
        }
    }

    /**
     * Prints an error message with formatting
     *
     * @param message The error message to display
     */
    private void printError(String message) {
        System.out.println(RED + "❌ ERROR: " + message + RESET);
    }

    /**
     * Prints a success message with formatting
     *
     * @param message The success message to display
     */
    private void printSuccess(String message) {
        System.out.println(GREEN + "✅ SUCCESS: " + message + RESET);
    }

    /**
     * Waits for the user to press Enter to continue
     */
    private void waitForEnter() {
        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    /**
     * Gets user confirmation for an action
     * 
     * @param message The confirmation message to display
     * @return true if confirmed, false otherwise
     */
    private boolean confirm(String message) {
        System.out.println();
        System.out.print(YELLOW + message + " (y/n): " + RESET);
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }

    /**
     * Gets a long value from user input
     * 
     * @param prompt The prompt to display
     * @return The long value entered by the user
     */
    private long getLongInput(String prompt) {
        while (true) {
            try {
                System.out.print(BOLD + prompt + RESET);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
    }

    /**
     * Gets a date from user input using numbered options for common dates
     * 
     * @param prompt The prompt to display
     * @return The LocalDate selected by the user
     */
    private LocalDate getDateInput(String prompt) {
        LocalDate today = LocalDate.now();

        while (true) {
            System.out.println(BOLD + prompt + RESET);
            System.out.println("1. Today (" + today.format(dateFormatter) + ")");
            System.out.println("2. Tomorrow (" + today.plusDays(1).format(dateFormatter) + ")");
            System.out.println("3. Next week (" + today.plusDays(7).format(dateFormatter) + ")");
            System.out.println("4. Enter specific date (yyyy-MM-dd)");
            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-4): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        return today;
                    case 2:
                        return today.plusDays(1);
                    case 3:
                        return today.plusDays(7);
                    case 4:
                        System.out.print(BOLD + "Enter date (yyyy-MM-dd): " + RESET);
                        String input = scanner.nextLine();
                        return LocalDate.parse(input, dateFormatter);
                    default:
                        printError("Please enter a valid option (1-4).");
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            } catch (Exception e) {
                printError("Please enter a valid date in the format yyyy-MM-dd.");
            }
        }
    }

    /**
     * Gets a shift type from user input using numbered options
     * 
     * @param prompt The prompt to display
     * @return The ShiftType selected by the user
     */
    private ShiftType getShiftTypeInput(String prompt) {
        while (true) {
            System.out.println(BOLD + prompt + RESET);
            System.out.println("1. MORNING");
            System.out.println("2. EVENING");
            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-2): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        return ShiftType.MORNING;
                    case 2:
                        return ShiftType.EVENING;
                    default:
                        printError("Please enter a valid option (1-2).");
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
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

    /**
     * Prints a welcome banner for the CLI
     */
    private void printWelcomeBanner() {
        System.out.println(CYAN + "╔══════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + RESET + "                                                  " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + BOLD + BLUE + "          ASSIGNMENT MANAGEMENT SYSTEM           " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "                                                  " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠══════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║" + RESET + YELLOW + "                    Welcome!                     " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println(GREEN + "Current date: " + RESET + LocalDate.now());
        System.out.println(GREEN + "Logged in as: " + RESET + formatEmployeeDisplay(doneBy));
    }

    /**
     * Prints a section header with a border.
     *
     * @param title - The title of the section to be printed.
     */
    private void printSectionHeader(String title) {
        String border = "┌─────────────────────────────────────────────┐";

        if (title.equalsIgnoreCase("Assignment Management Menu")) {
            System.out.println(BOLD + CYAN + "⚙️ ASSIGNMENT OPTIONS:" + RESET);
        } else {
            String formatted = String.format("│ %1$-43s │", BOLD + PURPLE + title.toUpperCase() + RESET);
            System.out.println(BLUE + border + RESET);
            System.out.println(BLUE + formatted + RESET);
            System.out.println(BLUE + border.replace('┌', '└').replace('┐', '┘') + RESET);
        }
    }

    //==========================================================================================
    // ASSIGNMENT MANAGEMENT METHODS
    //==========================================================================================

    /**
     * Displays the shift management submenu and processes user choices
     */
    private void shiftManagement() {
        printSectionHeader("Shift Management");

        System.out.println(CYAN + "Select a shift management option:" + RESET);
        System.out.println("1. Assign Employees to Shift");
        System.out.println("2. Remove Employee from Shift");
        System.out.println("3. Modify Required Roles");
        System.out.println("4. Back to Assignment Management Menu");

        System.out.println();
        System.out.print(CYAN + "======>" + RESET + " Enter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    assignEmployeeToShift();
                    break;
                case 2:
                    removeEmployeeFromShift();
                    break;
                case 3:
                    modifyRequiredRoles();
                    break;
                case 4:
                    System.out.println(YELLOW + "Returning to Assignment Management Menu..." + RESET);
                    break;
                default:
                    printError("Invalid choice. Please try again.");
                    shiftManagement(); // Recursive call to show the menu again
                    break;
            }
        } catch (NumberFormatException e) {
            printError("Please enter a valid number.");
            shiftManagement(); // Recursive call to show the menu again
        }
    }

    /**
     * Displays all shifts in the system
     */
    private void viewAllShifts() {
        printSectionHeader("All Shifts");

        ShiftSL[] shifts = shiftService.getAllShifts(doneBy);

        if (shifts.length == 0) {
            System.out.println(YELLOW + "No shifts found in the system." + RESET);
        } else {
            System.out.println(CYAN + "Found " + shifts.length + " shifts:" + RESET);
            System.out.println();

            System.out.println(BOLD + "┌─────────┬────────────┬───────────┬──────────┬───────────────────┐" + RESET);
            System.out.println(BOLD + "│   ID    │    Date    │   Type    │   Open   │ Assigned/Required │" + RESET);
            System.out.println(BOLD + "├─────────┼────────────┼───────────┼──────────┼───────────────────┤" + RESET);

            for (ShiftSL shift : shifts) {
                String openStatus = shift.isOpen() ? GREEN + "Open" + RESET : RED + "Closed" + RESET;

                // Calculate total assigned vs required employees
                int totalAssigned = 0;
                int totalRequired = 0;

                for (Map.Entry<String, Set<Long>> entry : shift.getAssignedEmployees().entrySet()) {
                    totalAssigned += entry.getValue().size();
                }

                for (Integer required : shift.getRolesRequired().values()) {
                    totalRequired += required;
                }

                String assignmentStatus = totalAssigned + "/" + totalRequired;

                System.out.printf("│ %-7d │ %-10s │ %-9s │ %-8s │ %-17s │%n", 
                    shift.getId(), 
                    shift.getShiftDate(),
                    shift.getShiftType(),
                    openStatus,
                    assignmentStatus);
            }

            System.out.println(BOLD + "└─────────┴────────────┴───────────┴──────────┴───────────────────┘" + RESET);
        }

        waitForEnter();
    }

    /**
     * Displays detailed information about a specific shift
     */
    private void viewShiftDetails() {
        printSectionHeader("Shift Details");

        long shiftId = getLongInput("Enter Shift ID: ");

        try {
            ShiftSL shift = shiftService.getShiftById(doneBy, shiftId);

            System.out.println(BLUE + "┌───────────────────────────────────────────────────┐" + RESET);
            System.out.println(BLUE + "│" + RESET + BOLD + YELLOW + "                  SHIFT INFORMATION               " + RESET + BLUE + "│" + RESET);
            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);

            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "ID:", shift.getId());
            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Date:", shift.getShiftDate());
            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Type:", shift.getShiftType());

            String status = shift.isOpen()
                ? GREEN + "Open" + RESET 
                : RED + "Closed" + RESET;
            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Status:", status);

            String managerStatus = shift.isAssignedShiftManager()
                ? GREEN + "Yes" + RESET 
                : RED + "No" + RESET;
            System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, "Shift Manager:", managerStatus);

            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);
            System.out.println(BLUE + "│" + RESET + BOLD + YELLOW + "                 ROLES & ASSIGNMENTS              " + RESET + BLUE + "│" + RESET);
            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);

            Map<String, Integer> rolesRequired = shift.getRolesRequired();
            Map<String, Set<Long>> assignedEmployees = shift.getAssignedEmployees();

            if (rolesRequired.isEmpty()) {
                System.out.printf(BLUE + "│ " + RESET + "%-49s " + BLUE + "│%n" + RESET, YELLOW + "  No roles defined for this shift" + RESET);
            } else {
                for (Map.Entry<String, Integer> entry : rolesRequired.entrySet()) {
                    String role = entry.getKey();
                    int required = entry.getValue();
                    Set<Long> assigned = assignedEmployees.getOrDefault(role, new HashSet<>());

                    System.out.printf(BLUE + "│ " + RESET + BOLD + "%-15s" + RESET + "│ %-33s " + BLUE + "│%n" + RESET, 
                        role + ":", 
                        assigned.size() + "/" + required + " assigned");

                    if (!assigned.isEmpty()) {
                        for (Long employeeId : assigned) {
                            System.out.printf(BLUE + "│ " + RESET + "%-15s" + "│ %-33s " + BLUE + "│%n" + RESET, 
                                "", 
                                "• " + formatEmployeeDisplay(employeeId));
                        }
                    }
                }
            }

            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);
            System.out.println(BLUE + "│" + RESET + BOLD + YELLOW + "               AVAILABLE EMPLOYEES               " + RESET + BLUE + "│" + RESET);
            System.out.println(BLUE + "├───────────────────────────────────────────────────┤" + RESET);

            Set<Long> availableEmployees = shift.getAvailableEmployees();
            if (availableEmployees.isEmpty()) {
                System.out.printf(BLUE + "│ " + RESET + "%-49s " + BLUE + "│%n" + RESET, YELLOW + "  No employees available for this shift" + RESET);
            } else {
                for (Long employeeId : availableEmployees) {
                    System.out.printf(BLUE + "│ " + RESET + "%-49s " + BLUE + "│%n" + RESET, "  • " + formatEmployeeDisplay(employeeId));
                }
            }

            System.out.println(BLUE + "└───────────────────────────────────────────────────┘" + RESET);

        } catch (Exception e) {
            printError("Shift not found or error retrieving details: " + e.getMessage());
        }

        waitForEnter();
    }

    /**
     * Assigns employees to a shift
     * Allows selecting multiple employees to assign to different roles in the shift
     */
    private void assignEmployeeToShift() {
        printSectionHeader("Assign Employees to Shift");

        if (!hasPermission("ASSIGN_EMPLOYEE")) {
            printError("You don't have permission to assign employees to shifts.");
            waitForEnter();
            return;
        }

        ShiftSL shift;
        long shiftId = 0;
        try {
            // Get all shifts
            ShiftSL[] shifts = shiftService.getAllShifts(doneBy);

            if (shifts.length == 0) {
                printError("No shifts found in the system.");
                waitForEnter();
                return;
            }

            // Filter to only show open shifts
            List<ShiftSL> openShifts = new ArrayList<>();
            for (ShiftSL s : shifts) {
                if (s.isOpen()) {
                    openShifts.add(s);
                }
            }

            if (openShifts.isEmpty()) {
                printError("No open shifts available for assignment.");
                waitForEnter();
                return;
            }

            // Display shifts as numbered options
            System.out.println(CYAN + "Select a shift:" + RESET);
            for (int i = 0; i < openShifts.size(); i++) {
                ShiftSL s = openShifts.get(i);
                System.out.printf("%d. %s (%s) - ID: %d%n", 
                    i + 1, 
                    s.getShiftDate(), 
                    s.getShiftType(),
                    s.getId());
            }

            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-" + openShifts.size() + "): ");

            int shiftChoice;
            try {
                shiftChoice = Integer.parseInt(scanner.nextLine());
                if (shiftChoice < 1 || shiftChoice > openShifts.size()) {
                    printError("Invalid choice. Please try again.");
                    waitForEnter();
                    return;
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
                waitForEnter();
                return;
            }

            // Get the selected shift
            shift = openShifts.get(shiftChoice - 1);
            shiftId = shift.getId();

            System.out.println(GREEN + "Shift: " + RESET + shift.getShiftDate() + " (" + shift.getShiftType() + ")");

            if (!shift.isOpen()) {
                printError("This shift is closed for assignments.");
                waitForEnter();
                return;
            }

            // Display roles and their requirements
            System.out.println();
            System.out.println(CYAN + "👥 " + BOLD + "ROLES REQUIRED" + RESET);

            Map<String, Integer> rolesRequired = shift.getRolesRequired();
            Map<String, Set<Long>> assignedEmployees = shift.getAssignedEmployees();

            if (rolesRequired.isEmpty()) {
                printError("No roles defined for this shift.");
                waitForEnter();
                return;
            }

            // Continue assigning employees until the user is done
            boolean continuingAssignments = true;
            while (continuingAssignments) {
                // Refresh shift data
                shift = shiftService.getShiftById(doneBy, shiftId);
                rolesRequired = shift.getRolesRequired();
                assignedEmployees = shift.getAssignedEmployees();

                // Display current assignment status
                System.out.println();
                System.out.println(CYAN + "👥 " + BOLD + "CURRENT ROLE ASSIGNMENTS" + RESET);

                List<String> availableRoles = new ArrayList<>();
                boolean allRolesStaffed = true;

                for (Map.Entry<String, Integer> entry : rolesRequired.entrySet()) {
                    String role = entry.getKey();
                    int required = entry.getValue();
                    Set<Long> assigned = assignedEmployees.getOrDefault(role, new HashSet<>());

                    System.out.printf("  • %-15s: %d/%d assigned%n", role, assigned.size(), required);

                    if (assigned.size() < required) {
                        availableRoles.add(role);
                        allRolesStaffed = false;
                    }

                    // Display employees assigned to this role
                    if (!assigned.isEmpty()) {
                        for (Long empId : assigned) {
                            System.out.printf("    - %s%n", formatEmployeeDisplay(empId));
                        }
                    }
                }

                if (allRolesStaffed) {
                    System.out.println(GREEN + "✅ All roles are fully staffed for this shift." + RESET);
                    waitForEnter();
                    return;
                }

                System.out.println();
                System.out.println(CYAN + "What would you like to do?" + RESET);
                System.out.println("1. Assign an employee to a role");
                System.out.println("2. Finish assignments");
                System.out.print(BOLD + "Enter your choice (1 or 2): " + RESET);
                String assignChoice = scanner.nextLine();

                if (assignChoice.equals("2")) {
                    System.out.println(YELLOW + "Finishing assignments." + RESET);
                    continuingAssignments = false;
                    continue;
                }

                if (!assignChoice.equals("1")) {
                    printError("Invalid choice. Please try again.");
                    continue;
                }

                // Select role using numbered options
                System.out.println();
                System.out.println(CYAN + "Available roles:" + RESET);

                if (availableRoles.isEmpty()) {
                    printError("No roles available for assignment.");
                    continue;
                }

                // Display roles as numbered options
                for (int i = 0; i < availableRoles.size(); i++) {
                    String availableRole = availableRoles.get(i);
                    int required = rolesRequired.get(availableRole);
                    int assigned = assignedEmployees.containsKey(availableRole) ? 
                                  assignedEmployees.get(availableRole).size() : 0;
                    System.out.printf("  %d. %s (%d/%d assigned)%n", i + 1, availableRole, assigned, required);
                }

                System.out.println();
                System.out.print(BOLD + "Select Role (1-" + availableRoles.size() + "): " + RESET);

                int roleChoice;
                try {
                    roleChoice = Integer.parseInt(scanner.nextLine());
                    if (roleChoice < 1 || roleChoice > availableRoles.size()) {
                        printError("Invalid choice. Please try again.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    printError("Please enter a valid number.");
                    continue;
                }

                // Get the selected role
                String role = availableRoles.get(roleChoice - 1);

                if (assignedEmployees.containsKey(role) && 
                    assignedEmployees.get(role).size() >= rolesRequired.get(role)) {
                    printError("This role is already fully staffed.");
                    continue;
                }

                // Display available employees for this shift
                Set<Long> availableEmployees = shift.getAvailableEmployees();
                System.out.println();
                System.out.println(CYAN + "👥 " + BOLD + "AVAILABLE EMPLOYEES FOR THIS SHIFT" + RESET);

                long employeeId;

                if (availableEmployees.isEmpty()) {
                    System.out.println(YELLOW + "  No employees are available for this shift." + RESET);
                    System.out.println();
                    System.out.println(CYAN + "Would you like to:" + RESET);
                    System.out.println("1. Enter an employee ID manually");
                    System.out.println("2. Cancel and return to role selection");
                    System.out.print(BOLD + "Enter your choice (1-2): " + RESET);

                    int manualChoice;
                    try {
                        manualChoice = Integer.parseInt(scanner.nextLine());
                        if (manualChoice != 1) {
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        printError("Please enter a valid number.");
                        continue;
                    }

                    // Manual entry
                    System.out.println();
                    employeeId = getLongInput("Enter Employee ID to assign: ");
                } else {
                    // Convert set to list for indexed access
                    List<Long> employeeList = new ArrayList<>(availableEmployees);

                    // Display employees as numbered options
                    for (int i = 0; i < employeeList.size(); i++) {
                        Long empId = employeeList.get(i);
                        System.out.printf("  %d. %s%n", i + 1, formatEmployeeDisplay(empId));
                    }

                    System.out.println();
                    System.out.println(CYAN + "Options:" + RESET);
                    System.out.println("1-" + employeeList.size() + ". Select an employee from the list");
                    System.out.println((employeeList.size() + 1) + ". Enter an employee ID manually");
                    System.out.print(BOLD + "Enter your choice: " + RESET);

                    try {
                        int empChoice = Integer.parseInt(scanner.nextLine());
                        if (empChoice >= 1 && empChoice <= employeeList.size()) {
                            // Selected from list
                            employeeId = employeeList.get(empChoice - 1);
                        } else if (empChoice == employeeList.size() + 1) {
                            // Manual entry
                            System.out.println();
                            employeeId = getLongInput("Enter Employee ID to assign: ");
                        } else {
                            printError("Invalid choice. Please try again.");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        printError("Please enter a valid number.");
                        continue;
                    }
                }

                // Check if employee is available
                boolean isAvailable = shiftService.isEmployeeAvailable(doneBy, shiftId, employeeId);

                if (!isAvailable) {
                    System.out.println(YELLOW + "⚠️ WARNING: " + formatEmployeeDisplay(employeeId) + " is not marked as available for this shift." + RESET);
                    if (confirm("Do you want to assign this employee anyway?")) {
                        // Continue with assignment
                    } else {
                        System.out.println(YELLOW + "Operation cancelled." + RESET);
                        continue;
                    }
                }

                // Check if employee is already assigned to this shift
                boolean isAssigned = shiftService.isEmployeeAssigned(doneBy, shiftId, employeeId);

                if (isAssigned) {
                    // Find which role the employee is already assigned to
                    String assignedRole = null;
                    for (Map.Entry<String, Set<Long>> entry : assignedEmployees.entrySet()) {
                        if (entry.getValue().contains(employeeId)) {
                            assignedRole = entry.getKey();
                            break;
                        }
                    }

                    if (assignedRole != null && assignedRole.equals(role)) {
                        printError(formatEmployeeDisplay(employeeId) + " is already assigned to this role in this shift.");
                        continue;
                    } else if (assignedRole != null) {
                        printError(formatEmployeeDisplay(employeeId) + " is already assigned to role '" + assignedRole + "' in this shift.");
                        System.out.println(YELLOW + "⚠️ WARNING: An employee cannot be assigned to multiple roles in the same shift." + RESET);
                        continue;
                    } else {
                        printError(formatEmployeeDisplay(employeeId) + " is already assigned to this shift.");
                        continue;
                    }
                }

                // Confirm assignment
                if (confirm("Confirm assigning " + formatEmployeeDisplay(employeeId) + " to role '" + role + "' for shift on " + 
                        shift.getShiftDate() + " (" + shift.getShiftType() + ")?")) {

                    String result = shiftService.assignEmployeeToRole(doneBy, shiftId, employeeId, role);

                    if (result.contains("successfully")) {
                        printSuccess(result);
                    } else {
                        printError(result);
                    }
                } else {
                    System.out.println(YELLOW + "Operation cancelled." + RESET);
                }
            }

        } catch (Exception e) {
            printError("Error assigning employee: " + e.getMessage());
        }

        waitForEnter();
    }

    /**
     * Removes an employee from a shift
     */
    private void removeEmployeeFromShift() {
        printSectionHeader("Remove Employee from Shift");

        if (!hasPermission("ASSIGN_EMPLOYEE")) {
            printError("You don't have permission to remove employees from shifts.");
            waitForEnter();
            return;
        }

        long shiftId = getLongInput("Enter Shift ID: ");

        try {
            ShiftSL shift = shiftService.getShiftById(doneBy, shiftId);

            System.out.println(GREEN + "Shift: " + RESET + shift.getShiftDate() + " (" + shift.getShiftType() + ")");

            if (!shift.isOpen()) {
                printError("This shift is closed for modifications.");
                waitForEnter();
                return;
            }

            // Display current assignments
            System.out.println();
            System.out.println(CYAN + "👥 " + BOLD + "CURRENT ASSIGNMENTS" + RESET);

            Map<String, Set<Long>> assignedEmployees = shift.getAssignedEmployees();
            boolean hasAssignments = false;

            for (Map.Entry<String, Set<Long>> entry : assignedEmployees.entrySet()) {
                String role = entry.getKey();
                Set<Long> employees = entry.getValue();

                if (!employees.isEmpty()) {
                    hasAssignments = true;
                    System.out.println("  " + BOLD + role + ":" + RESET);

                    for (Long employeeId : employees) {
                        System.out.println("    • " + formatEmployeeDisplay(employeeId));
                    }
                }
            }

            if (!hasAssignments) {
                printError("No employees are assigned to this shift.");
                waitForEnter();
                return;
            }

            // Select employee to remove
            long employeeId = getLongInput("Enter Employee ID to remove: ");

            // Check if employee is assigned to this shift
            boolean isAssigned = shiftService.isEmployeeAssigned(doneBy, shiftId, employeeId);

            if (!isAssigned) {
                printError(formatEmployeeDisplay(employeeId) + " is not assigned to this shift.");
                waitForEnter();
                return;
            }

            // Find which role the employee is assigned to
            String assignedRole = null;
            for (Map.Entry<String, Set<Long>> entry : assignedEmployees.entrySet()) {
                if (entry.getValue().contains(employeeId)) {
                    assignedRole = entry.getKey();
                    break;
                }
            }

            // Confirm removal
            if (confirm("Confirm removing " + formatEmployeeDisplay(employeeId) + " from role '" + assignedRole + "' for shift on " + 
                    shift.getShiftDate() + " (" + shift.getShiftType() + ")?")) {

                String result = shiftService.removeAssignedEmployee(doneBy, shiftId, assignedRole, employeeId);

                if (result.contains("successfully")) {
                    printSuccess(result);
                } else {
                    printError(result);
                }
            } else {
                System.out.println(YELLOW + "Operation cancelled." + RESET);
            }

        } catch (Exception e) {
            printError("Error removing employee: " + e.getMessage());
        }

        waitForEnter();
    }

    /**
     * Allows users to modify the required roles for a shift
     */
    private void modifyRequiredRoles() {
        printSectionHeader("Modify Required Roles");

        if (!hasPermission("ASSIGN_EMPLOYEE")) {
            printError("You don't have permission to modify required roles for shifts.");
            waitForEnter();
            return;
        }

        ShiftSL shift;
        long shiftId = 0;
        try {
            // Get all shifts
            ShiftSL[] shifts = shiftService.getAllShifts(doneBy);

            if (shifts.length == 0) {
                printError("No shifts found in the system.");
                waitForEnter();
                return;
            }

            // Filter to only show open shifts
            List<ShiftSL> openShifts = new ArrayList<>();
            for (ShiftSL s : shifts) {
                if (s.isOpen()) {
                    openShifts.add(s);
                }
            }

            if (openShifts.isEmpty()) {
                printError("No open shifts available for modification.");
                waitForEnter();
                return;
            }

            // Display shifts as numbered options
            System.out.println(CYAN + "Select a shift to modify required roles:" + RESET);
            for (int i = 0; i < openShifts.size(); i++) {
                ShiftSL s = openShifts.get(i);
                System.out.printf("%d. %s (%s) - ID: %d%n", 
                    i + 1, 
                    s.getShiftDate(), 
                    s.getShiftType(),
                    s.getId());
            }

            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-" + openShifts.size() + "): ");

            int shiftChoice;
            try {
                shiftChoice = Integer.parseInt(scanner.nextLine());
                if (shiftChoice < 1 || shiftChoice > openShifts.size()) {
                    printError("Invalid choice. Please try again.");
                    waitForEnter();
                    return;
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
                waitForEnter();
                return;
            }

            // Get the selected shift
            shift = openShifts.get(shiftChoice - 1);
            shiftId = shift.getId();

            System.out.println(GREEN + "Shift: " + RESET + shift.getShiftDate() + " (" + shift.getShiftType() + ")");

            if (!shift.isOpen()) {
                printError("This shift is closed for modifications.");
                waitForEnter();
                return;
            }

            // Display current required roles
            Map<String, Integer> rolesRequired = shift.getRolesRequired();

            boolean continuingModifications = true;
            while (continuingModifications) {
                // Refresh shift data
                shift = shiftService.getShiftById(doneBy, shiftId);
                rolesRequired = shift.getRolesRequired();

                System.out.println();
                System.out.println(CYAN + "👥 " + BOLD + "CURRENT REQUIRED ROLES" + RESET);

                if (rolesRequired.isEmpty()) {
                    System.out.println(YELLOW + "  No roles defined for this shift." + RESET);
                } else {
                    int index = 1;
                    for (Map.Entry<String, Integer> entry : rolesRequired.entrySet()) {
                        System.out.printf("  %d. %-15s: %d required%n", index++, entry.getKey(), entry.getValue());
                    }
                }

                System.out.println();
                System.out.println(CYAN + "What would you like to do?" + RESET);
                System.out.println("1. Add a new required role");
                System.out.println("2. Modify an existing required role");
                System.out.println("3. Remove a required role");
                System.out.println("4. Finish modifications");
                System.out.print(BOLD + "Enter your choice (1-4): " + RESET);
                String modifyChoice = scanner.nextLine();

                switch (modifyChoice) {
                    case "1": // Add a new required role
                        addRequiredRole(shiftId, rolesRequired);
                        break;
                    case "2": // Modify an existing required role
                        modifyExistingRole(shiftId, rolesRequired);
                        break;
                    case "3": // Remove a required role
                        removeRequiredRole(shiftId, rolesRequired);
                        break;
                    case "4": // Finish modifications
                        System.out.println(YELLOW + "Finishing modifications." + RESET);
                        continuingModifications = false;
                        break;
                    default:
                        printError("Invalid choice. Please try again.");
                        break;
                }
            }

        } catch (Exception e) {
            printError("Error modifying required roles: " + e.getMessage());
        }

        waitForEnter();
    }

    /**
     * Adds a new required role to a shift
     * 
     * @param shiftId The ID of the shift
     * @param currentRoles The current required roles for the shift
     */
    private void addRequiredRole(long shiftId, Map<String, Integer> currentRoles) {
        try {
            // Get all available roles
            Set<String> allRoles = shiftService.getRoles(doneBy);

            // Filter out roles that are already required
            List<String> availableRoles = new ArrayList<>();
            for (String role : allRoles) {
                if (!currentRoles.containsKey(role)) {
                    availableRoles.add(role);
                }
            }

            if (availableRoles.isEmpty()) {
                printError("No additional roles available to add.");
                return;
            }

            // Display available roles as numbered options
            System.out.println();
            System.out.println(CYAN + "Available roles to add:" + RESET);
            for (int i = 0; i < availableRoles.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, availableRoles.get(i));
            }

            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-" + availableRoles.size() + "): ");

            int roleChoice;
            try {
                roleChoice = Integer.parseInt(scanner.nextLine());
                if (roleChoice < 1 || roleChoice > availableRoles.size()) {
                    printError("Invalid choice. Please try again.");
                    return;
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
                return;
            }

            // Get the selected role
            String selectedRole = availableRoles.get(roleChoice - 1);

            // Get the number of employees required for this role
            int requiredCount = (int) getLongInput("Enter number of employees required for " + selectedRole + ": ");

            if (requiredCount <= 0) {
                printError("Number of required employees must be greater than zero.");
                return;
            }

            // Add the required role
            String result = shiftService.addRoleRequired(doneBy, shiftId, selectedRole, requiredCount);

            if (result.contains("success")) {
                printSuccess("Role '" + selectedRole + "' added with " + requiredCount + " required employees.");
            } else {
                printError("Failed to add role: " + result);
            }

        } catch (Exception e) {
            printError("Error adding required role: " + e.getMessage());
        }
    }

    /**
     * Modifies an existing required role for a shift
     * 
     * @param shiftId The ID of the shift
     * @param currentRoles The current required roles for the shift
     */
    private void modifyExistingRole(long shiftId, Map<String, Integer> currentRoles) {
        try {
            if (currentRoles.isEmpty()) {
                printError("No roles defined for this shift to modify.");
                return;
            }

            // Display current roles as numbered options
            System.out.println();
            System.out.println(CYAN + "Select a role to modify:" + RESET);

            List<String> rolesList = new ArrayList<>(currentRoles.keySet());
            for (int i = 0; i < rolesList.size(); i++) {
                String role = rolesList.get(i);
                System.out.printf("  %d. %-15s: %d required%n", i + 1, role, currentRoles.get(role));
            }

            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-" + rolesList.size() + "): ");

            int roleChoice;
            try {
                roleChoice = Integer.parseInt(scanner.nextLine());
                if (roleChoice < 1 || roleChoice > rolesList.size()) {
                    printError("Invalid choice. Please try again.");
                    return;
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
                return;
            }

            // Get the selected role
            String selectedRole = rolesList.get(roleChoice - 1);
            int currentCount = currentRoles.get(selectedRole);

            // Get the new number of employees required for this role
            int newCount = (int) getLongInput("Enter new number of employees required for " + selectedRole + " (currently " + currentCount + "): ");

            if (newCount <= 0) {
                printError("Number of required employees must be greater than zero.");
                return;
            }

            // Update the required role
            String result = shiftService.updateRolesRequired(doneBy, shiftId, selectedRole, newCount);

            if (result.contains("success")) {
                printSuccess("Role '" + selectedRole + "' updated from " + currentCount + " to " + newCount + " required employees.");
            } else {
                printError("Failed to update role: " + result);
            }

        } catch (Exception e) {
            printError("Error modifying required role: " + e.getMessage());
        }
    }

    /**
     * Removes a required role from a shift
     * 
     * @param shiftId The ID of the shift
     * @param currentRoles The current required roles for the shift
     */
    private void removeRequiredRole(long shiftId, Map<String, Integer> currentRoles) {
        try {
            if (currentRoles.isEmpty()) {
                printError("No roles defined for this shift to remove.");
                return;
            }

            // Display current roles as numbered options
            System.out.println();
            System.out.println(CYAN + "Select a role to remove:" + RESET);

            List<String> rolesList = new ArrayList<>(currentRoles.keySet());
            for (int i = 0; i < rolesList.size(); i++) {
                String role = rolesList.get(i);
                System.out.printf("  %d. %-15s: %d required%n", i + 1, role, currentRoles.get(role));
            }

            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-" + rolesList.size() + "): ");

            int roleChoice;
            try {
                roleChoice = Integer.parseInt(scanner.nextLine());
                if (roleChoice < 1 || roleChoice > rolesList.size()) {
                    printError("Invalid choice. Please try again.");
                    return;
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
                return;
            }

            // Get the selected role
            String selectedRole = rolesList.get(roleChoice - 1);

            // Confirm removal
            if (confirm("Are you sure you want to remove the role '" + selectedRole + "' from this shift?")) {
                // Remove the required role
                String result = shiftService.removeRoleRequired(doneBy, shiftId, selectedRole);

                if (result.contains("success")) {
                    printSuccess("Role '" + selectedRole + "' removed from shift requirements.");
                } else {
                    printError("Failed to remove role: " + result);
                }
            } else {
                System.out.println(YELLOW + "Operation cancelled." + RESET);
            }

        } catch (Exception e) {
            printError("Error removing required role: " + e.getMessage());
        }
    }

    private void viewEmployeeAssignments() {
        printSectionHeader("Employee Assignments");

        long employeeId = getLongInput("Enter Employee ID: ");

        try {
            ShiftSL[] shifts = shiftService.getShiftsByEmployee(doneBy, employeeId);

            if (shifts.length == 0) {
                System.out.println(YELLOW + "No shifts assigned to " + formatEmployeeDisplay(employeeId) + "." + RESET);
            } else {
                System.out.println(CYAN + "Found " + shifts.length + " shifts assigned to " + formatEmployeeDisplay(employeeId) + ":" + RESET);
                System.out.println();

                System.out.println(BOLD + "┌─────────┬────────────┬───────────┬──────────────┐" + RESET);
                System.out.println(BOLD + "│   ID    │    Date    │   Type    │     Role     │" + RESET);
                System.out.println(BOLD + "├─────────┼────────────┼───────────┼──────────────┤" + RESET);

                for (ShiftSL shift : shifts) {
                    // Find which role the employee is assigned to
                    String assignedRole = "Unknown";
                    for (Map.Entry<String, Set<Long>> entry : shift.getAssignedEmployees().entrySet()) {
                        if (entry.getValue().contains(employeeId)) {
                            assignedRole = entry.getKey();
                            break;
                        }
                    }

                    System.out.printf("│ %-7d │ %-10s │ %-9s │ %-12s │%n", 
                        shift.getId(), 
                        shift.getShiftDate(),
                        shift.getShiftType(),
                        assignedRole);
                }

                System.out.println(BOLD + "└─────────┴────────────┴───────────┴──────────────┘" + RESET);
            }

        } catch (Exception e) {
            printError("Error retrieving employee assignments: " + e.getMessage());
        }

        waitForEnter();
    }
}
