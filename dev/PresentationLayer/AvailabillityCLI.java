package PresentationLayer;

import ServiceLayer.ShiftSL;
import ServiceLayer.ShiftService;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class AvailabillityCLI {
    private final ShiftService shiftService;
    private final Scanner scanner;
    private final long doneBy;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("E dd/MM");

    public AvailabillityCLI(ShiftService shiftService, long doneBy) {
        this.shiftService = shiftService;
        this.scanner = new Scanner(System.in);
        this.doneBy = doneBy;
    }

    public void start() {
        while (true) {

            System.out.println("1. Add Availability");
            System.out.println("2. Update Availability");
            System.out.println("3. Delete Availability");
            System.out.println("4. View Availability");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    createAvailability();
                    break;
                case 2:
                    updateAvailability();
                    break;
                case 3:
                    deleteAvailability();
                    break;
                case 4:
                    viewAvailability();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (isWeekendBlocked()) {
                System.out.println("⚠️  The availability system is currently closed (Thursday 16:00 to Saturday night).");
                return;
            }

            System.out.println("🗓️  Employee Weekly Availability");
            LocalDate nextSunday = getNextSunday(LocalDate.now());

            printTableHeader();

            for (int i = 0; i < 7; i++) {
                LocalDate day = startOfWeek.plusDays(i);
                Set<ShiftTime> existing = availability.getOrDefault(day, new HashSet<>());

                for (ShiftTime shift : List.of(ShiftTime.MORNING, ShiftTime.EVENING, ShiftTime.NIGHT)) {
                    String symbol;
                    if (existing.contains(shift)) {
                        symbol = "v"; // Available
                    } else if (existing != null && !existing.contains(shift)) {
                        symbol = "-"; // Not available
                    } else {
                        symbol = " "; // Not selected
                    }

                    String dayStr = shift == ShiftTime.MORNING ? String.format("%-15s", day.format(dateFormatter)) : String.format("%-15s", "");
                    System.out.printf("%s| %-13s| %-13s| ", dayStr, shift, symbol);

                    String input = scanner.nextLine().trim().toLowerCase();
                    if (input.equals("v")) {
                        existing.add(shift);
                    } else if (input.equals("x") || input.isEmpty()) {
                        existing.remove(shift);
                    }
                }

                availability.put(day, existing);
            }

            System.out.println("\n✅ Availability updated successfully!");
        }

    }

    private LocalDate getNextSunday(LocalDate fromDate) {
        // אם היום ראשון, זה נחשב השבוע הזה. אנחנו רוצים את ראשון הבא
        int daysUntilSunday = (DayOfWeek.SUNDAY.getValue() - fromDate.getDayOfWeek().getValue() + 7) % 7;
        if (daysUntilSunday == 0) daysUntilSunday = 7;
        return fromDate.plusDays(daysUntilSunday);
    }

    private boolean isWeekendBlocked() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        DayOfWeek day = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();

        return (day == DayOfWeek.THURSDAY && time.isAfter(LocalTime.of(16, 0)))
                || day == DayOfWeek.FRIDAY
                || day == DayOfWeek.SATURDAY;
    }

    private static void printTableHeader() {
        System.out.printf("%-15s| %-13s| %-13s| Input (v/x/Enter)\n", "Day", "Type", "Available");
        System.out.println("------------------------------------------------------------------");
    }


}
