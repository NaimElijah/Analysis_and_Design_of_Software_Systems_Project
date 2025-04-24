package PresentationLayer;

import DomainLayer.enums.ShiftType;
import ServiceLayer.ShiftSL;
import ServiceLayer.ShiftService;
import Util.Week;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
//            if (isWeekendBlocked()) {
//                System.out.println("🚫 Availability update is blocked on weekends.");
//                return;
//            }
            LocalDate startDate = Week.getNextSunday(LocalDate.now());
            Week Week = Util.Week.from(startDate);
            Set<ShiftSL> weekShifts = shiftService.getShiftsByWeek(doneBy,Week);
            if (weekShifts.isEmpty()) {
                System.out.println("🚫 No shifts available for the upcoming week.");
                return;
            }

            System.out.println("🗓️  Employee Weekly Availability");
            System.out.printf("%-15s| %-13s| %-13s| %-10s%n", "Day", "Type", "Available", "For update Enter Y/N");
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

                    boolean input = Boolean.parseBoolean(scanner.nextLine().trim().toLowerCase());
                    if (input){
                        shiftService.markEmployeeAvailable(doneBy,shift.getId());
                    }
                }
            }
            System.out.println("✅ Availability updated!");
    }

    private boolean isWeekendBlocked() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        DayOfWeek day = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();

        return (day == DayOfWeek.THURSDAY && time.isAfter(LocalTime.of(16, 0)))
                || day == DayOfWeek.FRIDAY
                || day == DayOfWeek.SATURDAY;
    }
}
