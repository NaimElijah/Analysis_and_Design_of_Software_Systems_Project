package Util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public record Week(int year, int week) {
    public static Week from(LocalDate date) {
        WeekFields wf = WeekFields.of(Locale.getDefault());
        return new Week(date.getYear(), date.get(wf.weekOfWeekBasedYear()));
    }

    public static LocalDate getNextSunday(LocalDate fromDate) {
        // אם היום ראשון, זה נחשב השבוע הזה. אנחנו רוצים את ראשון הבא
        int daysUntilSunday = (DayOfWeek.SUNDAY.getValue() - fromDate.getDayOfWeek().getValue() + 7) % 7;
        if (daysUntilSunday == 0) daysUntilSunday = 7;
        return fromDate.plusDays(daysUntilSunday);
    }
}

