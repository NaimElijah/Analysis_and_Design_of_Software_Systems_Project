package PresentationLayer;

import DomainLayer.enums.ShiftType;
import ServiceLayer.EmployeeService;
import ServiceLayer.ShiftSL;
import ServiceLayer.ShiftService;
import ServiceLayer.exception.AuthorizationException;
import Util.Week;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AvailabillityCLI {
    private final ShiftService shiftService;
    private final EmployeeService employeeService;
    private final Scanner scanner;
    private final long doneBy;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("E dd/MM");

    public AvailabillityCLI(ShiftService shiftService, EmployeeService employeeService , long doneBy) {
        this.shiftService = shiftService;
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
        this.doneBy = doneBy;
    }

    public void start() {
        printWelcomeBanner();
        boolean running = true;

        while (running) {
            running = process();
        }
    }

    private void printWelcomeBanner() {
        System.out.println("Welcome to the Employee Module!");
        System.out.println("You are logged in as: " + doneBy);
    }

    public boolean process() {
        try {
            //        if (isWeekendBlocked()) {
//                System.out.println("🚫 Availability update is blocked on weekends.");
//                return;
//            }
            LocalDate startDate = Week.getNextSunday(LocalDate.now());
            Week Week = Util.Week.from(startDate);
            Set<ShiftSL> weekShifts = shiftService.getShiftsByWeek(doneBy,Week);
            if (weekShifts.isEmpty()) {
                System.out.println("🚫 No shifts available for the upcoming week.");
                return false;
            }

            System.out.println("🗓️  Employee Weekly Availability");
            System.out.printf("%-15s| %-13s| %-13s| %-10s%n", "Day", "Type", "Available", "For update Enter Y/N, for exit enter X");
            System.out.println("---------------------------------------------------------------");

            Map<Long, Boolean> userInputs = new LinkedHashMap<>();
            for (ShiftSL shift : weekShifts) {
                LocalDate date = shift.getShiftDate();
                String dayStr = date.getDayOfWeek().toString().substring(0, 1).toUpperCase() + date.getDayOfWeek().toString().substring(1).toLowerCase();
                dayStr = dayStr.substring(0, 3); // Sun, Mon...

                for (ShiftType type : ShiftType.values()) {
                    boolean available = shift.getAvailableEmployees().contains(doneBy);
                    String availabilityMark = available ? "v" : "";

                    System.out.printf("%-15s| %-13s| %-13s| ", (type == ShiftType.MORNING ? dayStr + " " + date : ""), type, availabilityMark);

                    boolean validInput = false;
                    while (!validInput) {
                        String input = scanner.nextLine().trim().toLowerCase();

                        switch (input) {
                            case "x" -> {
                                return false;
                            }
                            case "y" -> {
                                shiftService.markEmployeeAvailable(doneBy, shift.getId());
                                validInput = true;
                            }
                            case "n" -> {
                                shiftService.removeEmployeeAvailability(doneBy, shift.getId());
                                validInput = true;
                            }
                            default -> {
                                printError("Invalid input. Please enter 'Y' or 'N'. Try again:");
                                // Loop again until valid input
                            }
                        }
                    }
                }
            }
            System.out.println("✅ Availability updated!");
            return false;
        }
        catch (Exception e) {
            printError("An error occurred: " + e.getMessage());
            return true;
        }
    }

    private boolean isWeekendBlocked() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        DayOfWeek day = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();

        return (day == DayOfWeek.THURSDAY && time.isAfter(LocalTime.of(16, 0)))
                || day == DayOfWeek.FRIDAY
                || day == DayOfWeek.SATURDAY;
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
            printError(e.getMessage());
            return false;
        } catch (Exception e) {
            printError("Error checking permissions.");
            return false;
        }
    }
}
