package com.pii.tracker.track_calories.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DateTimeUtils {

    private DateTimeUtils() {}

    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    public static LocalDateTime endOfDay(LocalDate date) {
        return date.atTime(23, 59, 59);
    }
}
