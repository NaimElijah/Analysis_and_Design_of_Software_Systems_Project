package PresentationLayer;

import DomainLayer.enums.ShiftType;
import ServiceLayer.EmployeeSL;
import ServiceLayer.EmployeeService;
import ServiceLayer.ShiftSL;
import ServiceLayer.ShiftService;
import ServiceLayer.exception.ServiceException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ShiftCLI {
    // ANSI color codes
    private static final String RESET = CliUtil.RESET;
    private static final String YELLOW = CliUtil.YELLOW;

    // Properties
    private final ShiftService shiftService;
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private final long doneBy;
    private static final DateTimeFormatter dateFormatter = CliUtil.dateFormatter;

    /**
     * Constructor for ShiftCLI
     * @param shiftService
     * @param employeeService
     * @param doneBy
     */
    public ShiftCLI(ShiftService shiftService, EmployeeService employeeService, long doneBy) {
        this.shiftService = shiftService;
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
        this.doneBy = doneBy;
    }

    public void start() {
        printWelcomeBanner();
        boolean running = true;

        while (running) {
            int max = displayMenu();
            String choice = scanner.nextLine().trim();
            running = processMenuChoice(choice,max);
        }
    }

    private int displayMenu() {
        CliUtil.printEmptyLine();
        printSectionHeader("Main Menu");
        CliUtil.printBreadcrumb("Shift Management");

        List<String> menuOptions = new ArrayList<>();
        int optionNumber = 1;

        if (hasPermission("VIEW_SHIFT")) {
            menuOptions.add(CliUtil.YELLOW + optionNumber++ + CliUtil.RESET + ". View All Shifts");
            menuOptions.add(YELLOW + optionNumber++ + RESET + ". View Shift Details");
        }
        if (hasPermission("CREATE_SHIFT")) {
            menuOptions.add(CliUtil.YELLOW + optionNumber++ + CliUtil.RESET + ". Add Shift");
            menuOptions.add(CliUtil.YELLOW + optionNumber++ + CliUtil.RESET + ". Add Weekly Shifts");
        }
        if (hasPermission("EDIT_SHIFT")) {
            menuOptions.add(CliUtil.YELLOW + optionNumber++ + CliUtil.RESET + ". Edit Shifts");
        }
        if (hasPermission("DELETE_SHIFT")) {
            menuOptions.add(CliUtil.YELLOW + optionNumber++ + CliUtil.RESET + ". Delete Shifts");
        }

        menuOptions.add(CliUtil.YELLOW + optionNumber + CliUtil.RESET + ". Back To Main Menu");

        for (String option : menuOptions) {
            CliUtil.print("  " + option);
        }

        CliUtil.printEmptyLine();
        CliUtil.printPrompt("Enter your choice: ");
        return menuOptions.size();
    }

    private boolean processMenuChoice(String choice, int max) {
        CliUtil.printEmptyLine();
        try {
            int choiceNum = Integer.parseInt(choice);
            int currentOption = 1;

            if (choiceNum < 1 || choiceNum > max) {
                printError("Invalid choice. Please enter a number between 1 and " + max + ".");
                return true;
            }

            if (choiceNum == currentOption++) {
                viewAllShifts();
                return true;
            }
            if (choiceNum == currentOption++) {
                viewShiftDetails();
                return true;
            }

            if (choiceNum == currentOption++) {
                createShift();
                return true;
            }
            if (choiceNum == currentOption++) {
                addWeeklyShifts();
                return true;
            }

            if (choiceNum == currentOption++) {
                updateShift();
                return true;
            }
            if (choiceNum == currentOption++) {
                deleteShift();
                return true;
            }

            if (choiceNum == currentOption++) {
                CliUtil.printReturnMessage("main menu");
                return false;
            }

            CliUtil.printError("Invalid choice. Please try again.");
            return true;

        } catch (NumberFormatException e) {
            CliUtil.printError("Please enter a valid number.");
            return true;
        }
    }

    private void printWelcomeBanner() {
        CliUtil.printWelcomeBanner("SHIFT MANAGEMENT SYSTEM", LocalDate.now().toString(), "Employee #" + doneBy);
    }

    private boolean hasPermission(String permission) {
        return employeeService.hasPermission(doneBy, permission);
    }

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
     * Utility method to select a shift by date and type
     * Shows available shifts on the selected date to help user make a selection
     *
     * @param headerText The header text to display
     * @param openOnly Whether to only show open shifts
     * @return The selected shift, or null if no shift was selected
     */
    private ShiftSL selectShiftByDateAndType(String headerText, boolean openOnly) {
        printSectionHeader(headerText);

        // Get date from user
        LocalDate date = getDateInput("Select a date for the shift:");

        // Show available shifts on this date to help user make a selection
        try {
            ShiftSL[] shiftsOnDate = shiftService.getAllShiftsByDate(doneBy, date);

            // Filter shifts if openOnly is true
            if (openOnly) {
                List<ShiftSL> openShifts = new ArrayList<>();
                for (ShiftSL s : shiftsOnDate) {
                    if (s.isOpen()) {
                        openShifts.add(s);
                    }
                }

                if (openShifts.isEmpty()) {
                    printError("No open shifts found for the selected date: " + date.format(dateFormatter));
                    waitForEnter();
                    return null;
                }

                shiftsOnDate = openShifts.toArray(new ShiftSL[0]);
            }

            if (shiftsOnDate.length == 0) {
                printError("No shifts found for the selected date: " + date.format(dateFormatter));
                waitForEnter();
                return null;
            }

            CliUtil.printSectionWithIcon("Available shifts on " + date.format(dateFormatter) + ":", "📅");

            List<String> shiftItems = new ArrayList<>();
            for (ShiftSL s : shiftsOnDate) {
                String status = s.isOpen() ? "(Open)" : "(Closed)";
                shiftItems.add(s.getShiftType() + " shift " + status + " (ID: " + s.getId() + ")");
            }
            CliUtil.printHierarchicalList(shiftItems, "• ", 2);
            CliUtil.printEmptyLine();
        } catch (Exception e) {
            // If there's an error getting shifts by date, continue with manual selection
            CliUtil.printError("Could not retrieve shifts for the selected date. Please select a shift type manually.");
        }

        // Get shift type from user
        ShiftType shiftType = getShiftTypeInput("Select the shift type:");

        try {
            // Get shift by date and type
            ShiftSL shift = shiftService.getShift(doneBy, date, shiftType);

            // Check if shift is open if openOnly is true
            if (openOnly && !shift.isOpen()) {
                printError("The selected shift is closed for modifications.");
                waitForEnter();
                return null;
            }

            CliUtil.printSuccessWithCheckmark("Selected shift: " + date.format(dateFormatter) + " " + shiftType + " (ID: " + shift.getId() + ")");
            CliUtil.printEmptyLine();

            return shift;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                printError("No " + shiftType + " shift found for date " + date.format(dateFormatter));
            } else {
                printError("Error retrieving shift: " + e.getMessage());
            }
            waitForEnter();
            return null;
        }
    }

    /**
     * Prints a section header with a border.
     *
     * @param title - The title of the section to be printed.
     */
    private void printSectionHeader(String title) {
        boolean isMainMenu = title.equalsIgnoreCase("Shift Management Menu");
        CliUtil.printSectionHeader(title, isMainMenu, "SHIFTS");
    }

    /**
     * Prints an error message with formatting
     *
     * @param message The error message to display
     */
    private void printError(String message) {
        CliUtil.printError(message);
    }

    /**
     * Gets a date from user input using numbered options for common dates
     *
     * @param prompt The prompt to display
     * @return The LocalDate selected by the user
     */
    private LocalDate getDateInput(String prompt) {
        return CliUtil.getDateInput(prompt, scanner);
    }


    /**
     * Waits for the user to press Enter to continue
     */
    private void waitForEnter() {
        CliUtil.waitForEnter(scanner);
    }

    /**
     * Gets a shift type from user input using numbered options
     *
     * @param prompt The prompt to display
     * @return The ShiftType selected by the user
     */
    private ShiftType getShiftTypeInput(String prompt) {
        return CliUtil.getShiftTypeInput(prompt, scanner);
    }



    //===============================================================================
    // Shift Management Methods
    //===============================================================================

    /**
     * Displays all shifts in the system with pagination
     */
    private void viewAllShifts() {
        // Convert array to list for pagination
        List<ShiftSL> shiftList = Arrays.asList(shiftService.getAllShifts(doneBy));

        // Define how many shifts to show per page
        final int ITEMS_PER_PAGE = 5;

        // Use the pagination utility to display shifts
        CliUtil.displayPaginatedList(
                "All Shifts",
                shiftList,
                ITEMS_PER_PAGE,
                shift -> {
                    // Format each shift for display
                    String openStatus = shift.isOpen() ? CliUtil.greenString("Open") : CliUtil.redString("Closed");

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

                    // Return formatted string representation of the shift
                    return String.format("ID: %s | Date: %s | Type: %s | Status: %s | Assigned/Required: %s",
                            shift.getId(),
                            shift.getShiftDate().toString(),
                            shift.getShiftType().toString(),
                            openStatus,
                            assignmentStatus);
                },
                scanner
        );
    }

    /**
     * Displays detailed information about a specific shift
     * User selects a shift by entering date and shift type
     */
    private void viewShiftDetails() {
        printSectionHeader("Shift Details");

        // Display breadcrumb navigation
        CliUtil.printBreadcrumb("Assignment Management > View Shift Details");

        // Get date from user
        LocalDate date = getDateInput("Select a date for the shift you want to view:");

        // Show available shifts on this date to help user make a selection
        try {
            ShiftSL[] shiftsOnDate = shiftService.getAllShiftsByDate(doneBy, date);

            if (shiftsOnDate.length == 0) {
                printError("No shifts found for the selected date: " + date.format(dateFormatter));
                waitForEnter();
                return;
            }

            CliUtil.printSectionWithIcon("Available shifts on " + date.format(dateFormatter) + ":", "📅");

            List<String> shiftItems = new ArrayList<>();
            for (ShiftSL s : shiftsOnDate) {
                shiftItems.add(s.getShiftType() + " shift (ID: " + s.getId() + ")");
            }
            CliUtil.printHierarchicalList(shiftItems, "• ", 2);
            CliUtil.printEmptyLine();
        } catch (Exception e) {
            // If there's an error getting shifts by date, continue with manual selection
            CliUtil.printError("Could not retrieve shifts for the selected date. Please select a shift type manually.");
        }

        // Get shift type from user
        ShiftType shiftType = getShiftTypeInput("Select the shift type:");

        try {
            // Get shift by date and type
            ShiftSL shift = shiftService.getShift(doneBy, date, shiftType);

            CliUtil.printSuccessWithCheckmark("Found shift: " + date.format(dateFormatter) + " " + shiftType + " (ID: " + shift.getId() + ")");
            CliUtil.printEmptyLine();

            // Prepare data for the formatted table
            List<String> headers = new ArrayList<>();
            headers.add("SHIFT INFORMATION");
            headers.add("ROLES & ASSIGNMENTS");
            headers.add("AVAILABLE EMPLOYEES");

            Map<String, List<String[]>> content = new HashMap<>();
            Map<String, String> emptyMessages = new HashMap<>();

            // Shift information section
            List<String[]> shiftInfo = new ArrayList<>();
            shiftInfo.add(new String[]{"ID:", String.valueOf(shift.getId())});
            shiftInfo.add(new String[]{"Date:", shift.getShiftDate().toString()});
            shiftInfo.add(new String[]{"Type:", shift.getShiftType().toString()});

            String status = shift.isOpen()
                    ? CliUtil.greenString("Open")
                    : CliUtil.redString("Closed");
            shiftInfo.add(new String[]{"Status:", status});

            String managerStatus = shift.isAssignedShiftManager()
                    ? CliUtil.greenString("Assigned")
                    : CliUtil.redString("Not Assigned");
            shiftInfo.add(new String[]{"Shift Manager:", managerStatus});

            content.put("SHIFT INFORMATION", shiftInfo);

            // Roles & assignments section
            List<String[]> rolesInfo = new ArrayList<>();
            Map<String, Integer> rolesRequired = shift.getRolesRequired();
            Map<String, Set<Long>> assignedEmployees = shift.getAssignedEmployees();

            if (rolesRequired.isEmpty()) {
                emptyMessages.put("ROLES & ASSIGNMENTS", "No roles defined for this shift");
            } else {
                for (Map.Entry<String, Integer> entry : rolesRequired.entrySet()) {
                    String role = entry.getKey();
                    int required = entry.getValue();
                    Set<Long> assigned = assignedEmployees.getOrDefault(role, new HashSet<>());

                    rolesInfo.add(new String[]{role + ":", assigned.size() + "/" + required + " assigned"});

                    if (!assigned.isEmpty()) {
                        for (Long employeeId : assigned) {
                            rolesInfo.add(new String[]{"", "• " + formatEmployeeDisplay(employeeId)});
                        }
                    }
                }
            }
            content.put("ROLES & ASSIGNMENTS", rolesInfo);

            // Available employees section
            List<String[]> availableInfo = new ArrayList<>();
            Set<Long> availableEmployees = shift.getAvailableEmployees();

            if (availableEmployees.isEmpty()) {
                emptyMessages.put("AVAILABLE EMPLOYEES", "No employees available for this shift");
            } else {
                for (Long employeeId : availableEmployees) {
                    availableInfo.add(new String[]{"  • " + formatEmployeeDisplay(employeeId)});
                }
            }
            content.put("AVAILABLE EMPLOYEES", availableInfo);

            // Display the formatted table
            CliUtil.printFormattedTable(null, headers, content, emptyMessages);

        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                printError("No " + shiftType + " shift found for date " + date.format(dateFormatter));
                CliUtil.printTip("Check if the shift exists using 'View All Shifts' option first.");
            } else {
                printError("Error retrieving shift details: " + e.getMessage());
            }
        } catch (Exception e) {
            printError("Unexpected error: " + e.getMessage());
        }

        CliUtil.printReturnPrompt("Assignment Management Menu", scanner);
    }



//    private void viewShifts() {
//        CliUtil.printSectionHeader("View Shifts", false, "");
//        System.out.println(CliUtil.YELLOW + "1. View shift by date" + CliUtil.RESET);
//        System.out.println(CliUtil.YELLOW + "2. View all my shifts" + CliUtil.RESET);
//        System.out.println(CliUtil.YELLOW + "3. View this week's shifts" + CliUtil.RESET);
//        CliUtil.printPrompt("Choose an option (1-3): ");
//
//        String input = scanner.nextLine();
//        switch (input) {
//            case "1" -> viewShiftByDate();
////            case "2" -> viewAllUserShifts();
////            case "3" -> viewWeekShifts();
//            default -> CliUtil.printWarning("Invalid choice.");
//        }
//    }
//
//    private void viewShiftByDate() {
//        LocalDate date = CliUtil.getDateInput("Enter shift date:", scanner);
//        ShiftType shiftType = CliUtil.getShiftTypeInput("Enter shift type:", scanner);
//        ShiftSL shift = shiftService.getShift(doneBy, date, shiftType);
//        if (shift == null) {
//            CliUtil.printWarning("Shift not found.");
//            return;
//        }
//        displayShiftDetails(shift);
//    }

//    private void viewAllUserShifts() {
//        List<ShiftSL> shifts = shiftService.getShiftsForEmployee(doneBy);
//        if (shifts.isEmpty()) {
//            CliUtil.printWarning("No shifts found for you.");
//        } else {
//            for (ShiftSL shift : shifts) {
//                displayShiftDetails(shift);
//            }
//        }
//    }
//
//    private void viewWeekShifts() {
//        List<ShiftSL> weekShifts = shiftService.getShiftsForEmployeeThisWeek(doneBy);
//        if (weekShifts.isEmpty()) {
//            CliUtil.printInfo("No shifts this week.");
//        } else {
//            for (ShiftSL shift : weekShifts) {
//                displayShiftDetails(shift);
//            }
//        }
//    }

//    private void displayShiftDetails(ShiftSL shift) {
//        List<String> headers = List.of("Shift Info");
//        Map<String, List<String[]>> content = new LinkedHashMap<>();
//
//        List<String[]> details = new ArrayList<>();
//        details.add(new String[]{"Date", shift.getShiftDate().toString()});
//        details.add(new String[]{"Type", shift.getShiftType().toString()});
//        details.add(new String[]{"Is Open", String.valueOf(shift.isOpen())});
//        details.add(new String[]{"Has Manager", String.valueOf(shift.isAssignedShiftManager())});
//
//        if (!hasPermission("VIEW_SHIFT") || hasPermission("EDIT_SHIFT")) {
//            details.add(new String[]{"Roles Required", shift.getRolesRequired().toString()});
//            details.add(new String[]{"Assigned Employees", shift.getAssignedEmployees().toString()});
//            details.add(new String[]{"Available Employees", shift.getAvailableEmployees().toString()});
//        }
//
//        content.put("Shift Info", details);
//        Map<String, String> emptyMsg = Map.of("Shift Info", "No data found");
//
//        CliUtil.printFormattedTable("Shift Details", headers, content, emptyMsg);
//        CliUtil.waitForEnter(scanner);
//    }
//
    private void updateShift() {
        CliUtil.printSectionHeader("Update Shift", false, "");
        LocalDate date = CliUtil.getDateInput("Enter shift date:", scanner);
        ShiftType shiftType = CliUtil.getShiftTypeInput("Enter shift type:", scanner);
        ShiftSL shift = shiftService.getShift(doneBy, date, shiftType);

        if (shift == null) {
            CliUtil.printWarning("Shift not found.");
            return;
        }

        List<String[]> table = new ArrayList<>();
        table.add(new String[]{"Date", shift.getShiftDate().toString()});
        table.add(new String[]{"Type", shift.getShiftType().toString()});
        table.add(new String[]{"Is Open", String.valueOf(shift.isOpen())});
        table.add(new String[]{"Has Manager", String.valueOf(shift.isAssignedShiftManager())});

        CliUtil.printFormattedTable("Current Shift Details", List.of("Shift Data"), Map.of("Shift Data", table), Map.of());

        boolean isOpen = shift.isOpen();
        boolean hasManager = shift.isAssignedShiftManager();

        if (CliUtil.confirm("Do you want to change open status?", scanner)) {
            isOpen = CliUtil.confirm("Mark shift as OPEN?", scanner);
        }

        if (CliUtil.confirm("Do you want to change manager status?", scanner)) {
            hasManager = CliUtil.confirm("Is a shift manager assigned?", scanner);
        }

        if (CliUtil.confirm("Do you want to update roles required?", scanner)) {
            Map<String, Integer> updatedRoles = new HashMap<>(shift.getRolesRequired());
            for (String role : updatedRoles.keySet()) {
                int current = updatedRoles.get(role);
                CliUtil.print(CliUtil.YELLOW + role + " (current: " + current + "): " + CliUtil.RESET);
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    try {
                        updatedRoles.put(role, Integer.parseInt(input));
                    } catch (NumberFormatException e) {
                        CliUtil.printWarning("Invalid input for " + role);
                    }
                }
            }
            for (Map.Entry<String, Integer> entry : updatedRoles.entrySet()) {
                shiftService.updateRolesRequired(doneBy, shift.getId(), entry.getKey(), entry.getValue());
            }
        }

        String result = shiftService.updateShift(doneBy, shift.getId(), shiftType, date, isOpen, hasManager, LocalDate.now());
        CliUtil.printSuccess(result);
        CliUtil.waitForEnter(scanner);
    }

    private void deleteShift() {
        CliUtil.printSectionHeader("Delete Shift", false, "");
        LocalDate date = CliUtil.getDateInput("Enter shift date:", scanner);
        ShiftType shiftType = CliUtil.getShiftTypeInput("Enter shift type:", scanner);
        ShiftSL shift = shiftService.getShift(doneBy, date, shiftType);

        if (shift == null) {
            CliUtil.printWarning("Shift not found.");
            return;
        }

        List<String[]> table = new ArrayList<>();
        table.add(new String[]{"Date", shift.getShiftDate().toString()});
        table.add(new String[]{"Type", shift.getShiftType().toString()});
        table.add(new String[]{"Is Open", String.valueOf(shift.isOpen())});

        CliUtil.printFormattedTable("Shift To Delete", List.of("Shift Preview"), Map.of("Shift Preview", table), Map.of());

        if (CliUtil.confirm("Are you sure you want to delete this shift?", scanner)) {
            String result = shiftService.removeShift(doneBy, date, shiftType);
            CliUtil.printSuccess(result);
        } else {
            CliUtil.printOperationCancelled();
        }

        CliUtil.waitForEnter(scanner);
    }

    private void createShift() {
        CliUtil.printSectionHeader("Create Shift", false, "");
        LocalDate date = CliUtil.getDateInput("Enter shift date:", scanner);
        ShiftType shiftType = CliUtil.getShiftTypeInput("Enter shift type:", scanner);

        Map<String, Integer> rolesRequired = new HashMap<>();
        Set<String> roles = shiftService.getRoles(doneBy);
        for (String role : roles) {
            CliUtil.printPrompt("Enter required number for role '" + role + "': ");
            try {
                int count = Integer.parseInt(scanner.nextLine().trim());
                if (count > 0) rolesRequired.put(role, count);
            } catch (NumberFormatException e) {
                CliUtil.printWarning("Invalid number, skipping " + role);
            }
        }

        boolean isManagerShift = CliUtil.confirm("Is this a manager shift?", scanner);
        boolean isOpen = CliUtil.confirm("Is the shift open?", scanner);

        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();

        String result = shiftService.createShift(doneBy, shiftType, date, rolesRequired, assignedEmployees, availableEmployees, isManagerShift, isOpen, LocalDate.now());
        CliUtil.printSuccess(result);
        CliUtil.waitForEnter(scanner);
    }

    private void addWeeklyShifts() {
        CliUtil.printSectionHeader("Add Weekly Shifts", false, "");
        LocalDate startDate = CliUtil.getDateInput("Enter start date:", scanner);
        Map<String, Integer> rolesRequired = new HashMap<>();
        Set<String> roles = shiftService.getRoles(doneBy);

        for (String role : roles) {
            CliUtil.printPrompt("Default number for '" + role + "': ");
            try {
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number > 0) rolesRequired.put(role, number);
            } catch (NumberFormatException e) {
                CliUtil.printWarning("Invalid number for " + role);
            }
        }

        String result = shiftService.createWeeklyShifts(doneBy, startDate, rolesRequired);
        CliUtil.printSuccess(result);
        CliUtil.waitForEnter(scanner);
    }
}