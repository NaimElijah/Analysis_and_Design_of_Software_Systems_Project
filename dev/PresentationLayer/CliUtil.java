package PresentationLayer;

import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Utility class for CLI operations with common methods for printing, formatting, and user interaction.
 * This class centralizes UI-related functionality used across different CLI classes.
 */
public class CliUtil {
    // ANSI color codes
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    // Properties
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    /**
     * Prints an error message with formatting
     *
     * @param message The error message to display
     */
    public static void printError(String message) {
        System.out.println(RED + "❌ ERROR: " + message + RESET);
    }

    /**
     * Prints a success message with formatting
     *
     * @param message The success message to display
     */
    public static void printSuccess(String message) {
        System.out.println(GREEN + "✅ SUCCESS: " + message + RESET);
    }

    /**
     * Waits for the user to press Enter to continue
     * 
     * @param scanner The scanner to use for input
     */
    public static void waitForEnter(Scanner scanner) {
        System.out.println(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    /**
     * Gets user confirmation for an action
     * 
     * @param message The confirmation message to display
     * @param scanner The scanner to use for input
     * @return true if confirmed, false otherwise
     */
    public static boolean confirm(String message, Scanner scanner) {
        System.out.println();
        System.out.print(YELLOW + message + " (y/n): " + RESET);
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }

    /**
     * Gets a long value from user input
     * 
     * @param prompt The prompt to display
     * @param scanner The scanner to use for input
     * @return The long value entered by the user
     */
    public static long getLongInput(String prompt, Scanner scanner) {
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
     * Prints a section header with a border.
     *
     * @param title The title of the section to be printed.
     * @param isMainMenu Whether this is a main menu header
     * @param menuType The type of menu (for customizing the icon and text)
     */
    public static void printSectionHeader(String title, boolean isMainMenu, String menuType) {
        String border = "┌─────────────────────────────────────────────┐";

        if (isMainMenu) {
            System.out.println(BOLD + CYAN + "⚙️ " + menuType + " OPTIONS:" + RESET);
        } else {
            String formatted = String.format("│ %1$-43s │", BOLD + PURPLE + title.toUpperCase() + RESET);
            System.out.println(BLUE + border + RESET);
            System.out.println(BLUE + formatted + RESET);
            System.out.println(BLUE + border.replace('┌', '└').replace('┐', '┘') + RESET);
        }
    }

    /**
     * Prints a welcome banner for the CLI
     * 
     * @param title The title to display in the banner
     * @param currentDate The current date to display
     * @param userInfo Information about the current user
     */
    public static void printWelcomeBanner(String title, String currentDate, String userInfo) {
        System.out.println(CYAN + "╔══════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + RESET + "                                                  " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + BOLD + BLUE + String.format("%42s", title) + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "║" + RESET + "                                                  " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠══════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║" + RESET + YELLOW + "                    Welcome!                     " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println(GREEN + "Current date: " + RESET + currentDate);
        System.out.println(GREEN + "Logged in as: " + RESET + userInfo);
    }

    /**
     * Gets a shift type from user input using numbered options
     *
     * @param prompt The prompt to display
     * @return The ShiftType selected by the user
     */
    public static ShiftType getShiftTypeInput(String prompt, Scanner scanner) {
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
     * Gets a date from user input using numbered options for common dates
     *
     * @param prompt The prompt to display
     * @return The LocalDate selected by the user
     */
    public static LocalDate getDateInput(String prompt, Scanner scanner) {
        LocalDate today = LocalDate.now();

        while (true) {
            System.out.println(BOLD + prompt + RESET);
            System.out.println("1. Today (" + today.format(dateFormatter) + ")");
            System.out.println("2. Tomorrow (" + today.plusDays(1).format(dateFormatter) + ")");
            System.out.println("3. Next week (" + today.plusDays(7).format(dateFormatter) + ")");
            System.out.println("4. Enter specific date (dd-mm-yyyy):");
            System.out.print(CYAN + "======>" + RESET + " Enter your choice (1-4): ");

            String input = scanner.nextLine();

            // Check if input is a date in the format dd-mm-yyyy
            if (input.matches("\\d{2}-\\d{2}-\\d{4}")) {
                try {
                    return LocalDate.parse(input, dateFormatter);
                } catch (Exception e) {
                    printError("Please enter a valid date in the format dd-mm-yyyy.");
                    continue;
                }
            }

            // Otherwise, try to parse as a menu choice
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        return today;
                    case 2:
                        return today.plusDays(1);
                    case 3:
                        return today.plusDays(7);
                    case 4:
                        System.out.print(BOLD + "Enter date (dd-mm-yyyy): " + RESET);
                        input = scanner.nextLine();
                        return LocalDate.parse(input, dateFormatter);
                    default:
                        printError("Please enter a valid option (1-4).");
                }
            } catch (NumberFormatException e) {
                printError("Please enter a valid number or date in the format dd-mm-yyyy.");
            } catch (Exception e) {
                printError("Please enter a valid date in the format dd-mm-yyyy.");
            }
        }
    }
}
