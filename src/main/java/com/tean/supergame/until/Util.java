package com.tean.supergame.until;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.time.Duration;

@UtilityClass
public class Util {
    private static final LocalTime MORNING_START = LocalTime.of(7, 0);
    private static final LocalTime MORNING_END = LocalTime.of(9, 0);
    private static final LocalTime AFTERNOON_START = LocalTime.of(13, 0);
    private static final LocalTime AFTERNOON_END = LocalTime.of(16, 0);
    private static final LocalTime EVENING_START = LocalTime.of(17, 0);
    private static final LocalTime EVENING_END = LocalTime.of(19, 0);

    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }


    public boolean isWithinCheckinTime(LocalTime currentTime) {
        return (currentTime.isAfter(MORNING_START) && currentTime.isBefore(MORNING_END)) ||
                (currentTime.isAfter(AFTERNOON_START) && currentTime.isBefore(AFTERNOON_END)) ||
                (currentTime.isAfter(EVENING_START) && currentTime.isBefore(EVENING_END));
    }

    public long getSecondsUntilEnd(LocalTime currentTime) {
        long second = 1;
        if (currentTime.isAfter(MORNING_START) && currentTime.isBefore(MORNING_END)) {
            second = Duration.between(currentTime, MORNING_END).getSeconds();
        } else if (currentTime.isAfter(AFTERNOON_START) && currentTime.isBefore(AFTERNOON_END)) {
            second = Duration.between(currentTime, AFTERNOON_END).getSeconds();
        } else if (currentTime.isAfter(EVENING_START) && currentTime.isBefore(EVENING_END)) {
            second = Duration.between(currentTime, EVENING_END).getSeconds();
        }
        return second;
    }
}