package Util;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public record Week(int year, int week) {
    public static Week from(LocalDate date) {
        WeekFields wf = WeekFields.of(Locale.getDefault());
        return new Week(date.getYear(), date.get(wf.weekOfWeekBasedYear()));
    }
}

