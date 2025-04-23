package PresentationLayer;

import ServiceLayer.ShiftSL;
import ServiceLayer.ShiftService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class ShiftCLI {
    private final ShiftService shiftService;
    private final Scanner scanner;
    private final long doneBy;

    public ShiftCLI(ShiftService shiftService, long doneBy) {
        this.shiftService = shiftService;
        this.scanner = new Scanner(System.in);
        this.doneBy = doneBy;
    }

    public void start() {
        while (true) {
            System.out.println("1. Add Shift");
            System.out.println("2. Add weekly Shifts");
            System.out.println("3. Update Shift");
            System.out.println("4. Delete Shift");
            System.out.println("5. View Shift");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    createShift();
                    break;
                case 2:
                    addWeeklyShifts();
                    break;
                case 3:
                    updateShift();
                    break;
                case 4:
                    deleteShift();
                    break;
                case 5:
                    viewShifts();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createShift() {
        System.out.println("Creat shift:");
        System.out.print("Enter shift date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter shift type (MORNING / EVENING): ");
        String shiftType = scanner.nextLine();
        Map<String, Integer> rolesRequired = new HashMap<>();
        Set<String> roles = shiftService.getRoles(doneBy);
        System.out.println("Enter number of employees for each role: (if not required, enter 0)");
        for (String role : roles) {
            System.out.print(role + ": ");
            int number = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (number > 0) {
                rolesRequired.put(role, number);
            }

        }
        System.out.print("Is this shift a manager shift? (T/F): ");
        boolean isManagerShift = scanner.nextLine().equalsIgnoreCase("T");
        System.out.print("Is this shift open? (T/F): ");
        boolean isOpen = scanner.nextLine().equalsIgnoreCase("T");
        Map<String, Set<Long>> assignedEmployees = new HashMap<>();
        Set<Long> availableEmployees = new HashSet<>();
        String result = shiftService.createShift(doneBy, shiftType, date, rolesRequired, assignedEmployees, availableEmployees, isManagerShift, isOpen, LocalDate.now());
        System.out.println(result);
    }

    private void updateShift() {
        System.out.println("Update shift:");
        System.out.print("Enter shift date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter shift type (MORNING / EVENING): ");
        String shiftType = scanner.nextLine();

        ShiftSL shift = shiftService.getShift(doneBy, date, shiftType);
        boolean isOpen = shift.isOpen();
        boolean hasShiftManager = shift.isAssignedShiftManager();
        if (shift == null) {
            System.out.println("Shift not found.");
            return;
        }

        // Display the editable table with prompts
        System.out.printf("| %-26s | %-24s | %-20s |\n", "Shift Detail", "Current", "New");
        System.out.println("+----------------------------+--------------------------+----------------------+");
        System.out.printf("| %-26s | %-24s | %-20s |\n", "Shift Type", shift.getShiftType(), "-");
        System.out.printf("| %-26s | %-24s | %-20s |\n", "Date", shift.getShiftDate(), "-");

        // Ask for "Is Open"
        System.out.print("| Is Open (T/F)            | " + padRight(String.valueOf(shift.isOpen()) , 24) + " | ");
        String isOpenInput = scanner.nextLine().trim();

        // Ask for "Has Shift Manager"
        System.out.print("| Has Shift Manager (T/F) | " + padRight(String.valueOf(shift.isAssignedShiftManager()), 24) + " | ");
        String hasManagerInput = scanner.nextLine().trim();

        // Parse and apply updates
        if (!isOpenInput.isEmpty()) {
            if (isOpenInput.equalsIgnoreCase("T") || isOpenInput.equalsIgnoreCase("F")) {
                isOpen = Boolean.parseBoolean(isOpenInput);
            } else {
                System.out.println("Invalid input for 'Is Open'. Skipping update for this field.");
            }
        }
        if (!hasManagerInput.isEmpty()) {
            if (hasManagerInput.equalsIgnoreCase("T") && hasManagerInput.equalsIgnoreCase("F")) {
                hasShiftManager = Boolean.parseBoolean(hasManagerInput);
            }
            else {
                System.out.println("Invalid input for 'Has Shift Manager'. Skipping update for this field.");
            }

        }

        // Ask if user wants to update roles
        System.out.print("Update number of employees per role? (yes/no): ");
        String updateRolesAnswer = scanner.nextLine().trim();

        if (updateRolesAnswer.equalsIgnoreCase("yes")) {
            // Print the table header once
            System.out.printf("| %-26s | %-24s | %-20s |\n", "Role", "Current", "New");
            System.out.println("--------------------------------------------------------------------------------");

            for (Map.Entry<String, Integer> entry : shift.getRolesRequired().entrySet()) {
                String role = entry.getKey();
                int current = entry.getValue();

                // Print current value
                System.out.printf("| %-26s | %-24s | ", role, current);

                // Get input
                String roleInput = scanner.nextLine().trim();

                // Skip if input is empty
                if (!roleInput.isEmpty()) {
                    try {
                        int newNumber = Integer.parseInt(roleInput);
                        shiftService.updateRolesRequired(doneBy, shift.getId(), role, newNumber);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered for " + role + ". Skipping.");
                    }
                }
            }
        }
        String result = shiftService.updateShift(doneBy, shift.getId(), shiftType, date, isOpen, hasShiftManager, LocalDate.now());
        System.out.println(result);

    }

    private String padRight(String s, int i) {
        return String.format("%-" + i + "s", s);
    }

    private void deleteShift() {
        System.out.println("Delete shift:");
        System.out.print("Enter shift date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter shift type (MORNING / EVENING): ");
        String shiftType = scanner.nextLine();
        System.out.print("Are you sure you want to delete this shift? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Shift deletion cancelled.");
            return;
        }
        String result = shiftService.removeShift(doneBy, date, shiftType);
        System.out.println(result);
    }

    private void viewShifts() {
        System.out.println("View shifts:");
        System.out.print("Enter shift date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter shift type (MORNING / EVENING): ");
        String shiftType = scanner.nextLine();
        ShiftSL shift = shiftService.getShift(doneBy, date, shiftType);
        if (shift == null) {
            System.out.println("Shift not found.");
            return;
        }
        System.out.println("Shift Details:");
        System.out.println("Shift Type: " + shift.getShiftType());
        System.out.println("Shift Date: " + shift.getShiftDate());
        System.out.println("Roles Required: " + shift.getRolesRequired());
        System.out.println("Assigned Employees: " + shift.getAssignedEmployees());
        System.out.println("Available Employees: " + shift.getAvailableEmployees());
    }

    private void addWeeklyShifts() {
        System.out.println("Add weekly shifts:");
        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        Map<String, Integer> rolesRequired = new HashMap<>();
        Set<String> roles = shiftService.getRoles(doneBy);
        System.out.println("Enter default number of employees for each role for all shifts: (if not required, enter 0)");
        for (String role : roles) {
            System.out.print(role + ": ");
            int number = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (number > 0) {
                rolesRequired.put(role, number);
            }
        }
        String result = shiftService.createWeeklyShifts(doneBy, startDate, rolesRequired);
        System.out.println(result);
    }




}
