package PresentationLayer;

import DomainLayer.Shift;
import DomainLayer.ShiftController;
import DomainLayer.enums.ShiftType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ShiftCLI {
    private final ShiftController shiftController;
    private final Scanner scanner;
    private final long employeeId;

    public ShiftCLI(ShiftController shiftController, long employeeId) {
        this.shiftController = shiftController;
        this.scanner = new Scanner(System.in);
        this.employeeId = employeeId;
    }

    public void start() {
        while (true) {
            System.out.println("1. Add Shift");
//            System.out.println("2. Add weekly Shifts");
            System.out.println("3. Update Shift");
            System.out.println("4. Delete Shift");
            System.out.println("5. View Shifts");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    addShift();
                    break;
//                case 2:
//                    addWeeklyShifts();
//                    break;
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

    private void addShift() {
        System.out.println("Creat shift:");
        System.out.print("Enter shift type (MORNING / EVENING): ");
        String shiftType = scanner.nextLine();
        System.out.print("Enter shift date (YYYY-MM-DD): ");
        LocalDate Date = LocalDate.parse(scanner.nextLine());
        Map<String, Integer> termsOfEmployment = new HashMap<>();
        System.out.println("Add required amount of Employees of each role (role-number pairs). Type 'done' to finish.");
        while (true) {
            System.out.print("role (or 'done'): ");
            String key = scanner.nextLine();
            if (key.equalsIgnoreCase("done")) break;

            System.out.print("number: ");
            Integer value = Integer.valueOf(scanner.nextLine());
            termsOfEmployment.put(key, value);
        }
        System.out.print("Is this shift a manager shift? (T/F): ");
        boolean isManagerShift = scanner.nextLine().equalsIgnoreCase("T");
        System.out.print("Is this shift open? (T/F): ");
        boolean isOpen = scanner.nextLine().equalsIgnoreCase("T");

        // Add logic to create a shift using shiftController
        // Example: shiftController.createShift(shiftType, date);
        System.out.println("Shift added successfully.");
    }

    private void updateShift() {
        System.out.println("Update shift:");
        System.out.print("Enter shift type (MORNING / EVENING): ");
        String shiftType = scanner.nextLine();
        System.out.print("Enter shift date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        Shift shift = shiftController.getshift(date, shiftType);
        System.out.print("choose what to update: ");
        System.out.println("1. Roles Required");
        System.out.println("2. Assigned Employees");
        System.out.println("3. Available Employees");
        System.out.println("4. Is Assigned Shift Manager");
        System.out.println("5. Is Open");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:
                System.out.println("Update Roles Required:");
                System.out.print("Enter role to update: ");
                String roleToUpdate = scanner.nextLine();
                System.out.print("Enter new number of employees for this role: ");
                int newNumber = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                shiftController.updateRolesRequired()
                updateRolesRequired(shift);
                break;
            case 2:
                updateAssignedEmployees(shift);
                break;
            case 3:
                updateAvailableEmployees(shift);
                break;
            case 4:
                updateIsAssignedShiftManager(shift);
                break;
            case 5:
                updateIsOpen(shift);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        Map<String, Integer> termsOfEmployment = new HashMap<>();
        System.out.println("Add required amount of Employees of each role (role-number pairs). Type 'done' to finish.");
        while (true) {
            System.out.print("role (or 'done'): ");
            String key = scanner.nextLine();
            if (key.equalsIgnoreCase("done")) break;

            System.out.print("number: ");
            Integer value = Integer.valueOf(scanner.nextLine());
            termsOfEmployment.put(key, value);
        }
        System.out.print("Is this shift a manager shift? (T/F): ");
        boolean isManagerShift = scanner.nextLine().equalsIgnoreCase("T");
        System.out.print("Is this shift open? (T/F): ");
        boolean isOpen = scanner.nextLine().equalsIgnoreCase("T");

        // Add logic to update a shift using shiftController
        // Example: shiftController.updateShift(shiftId, shiftType, date);
        System.out.println("Shift updated successfully.");
    }

}
