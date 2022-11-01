package org.example.trip.util;

import org.example.trip.model.BusStop;
import org.example.trip.model.TripStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.example.trip.model.TripStatus.*;

public class TripUtils {
    public static LocalDateTime stringToDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static long durationSecsBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).getSeconds();
    }

    public static TripStatus tripStatusOf(BusStop from, BusStop to) {
        if (to == null)
            return INCOMPLETE;
        return from == to ? CANCELLED : COMPLETED;
    }

    public static String dateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }
}
