package Util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public record Week(int year, int week) implements Comparable<Week> {
    public static Week from(LocalDate date) {
        WeekFields wf = WeekFields.of(Locale.getDefault());
        return new Week(date.getYear(), date.get(wf.weekOfWeekBasedYear()));
    }

    public static LocalDate getNextSunday(LocalDate fromDate) {
        int daysUntilSunday = (DayOfWeek.SUNDAY.getValue() - fromDate.getDayOfWeek().getValue() + 7) % 7;
        if (daysUntilSunday == 0) daysUntilSunday = 7;
        return fromDate.plusDays(daysUntilSunday);
    }

    @Override
    public int compareTo(Week other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        return Integer.compare(this.week, other.week);
    }
}
