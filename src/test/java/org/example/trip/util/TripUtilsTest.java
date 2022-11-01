package org.example.trip.util;

import org.example.trip.model.BusStop;
import org.example.trip.model.TripStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TripUtilsTest {

    @Test
    public void convertsStringToDateTime() {
        String input = "22-01-2022 13:02:30";
        LocalDateTime expected = LocalDateTime.of(2022, 1, 22, 13, 2, 30);

        LocalDateTime actual = TripUtils.stringToDateTime(input);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void calculatesDurationSecsBetweenDateTimeInstances() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 22, 13, 2, 30);
        LocalDateTime end = LocalDateTime.of(2022, 1, 22, 13, 12, 0);
        long expected = 570;

        long actual = TripUtils.durationSecsBetween(start, end);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void formatsDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 22, 13, 2, 30);
        String expected = "22-01-2022 13:02:30";

        String actual = TripUtils.dateTimeToString(dateTime);

        assertThat(actual, equalTo(expected));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "STOP_1, NIL, INCOMPLETE",
            "STOP_1, STOP_1, CANCELLED",
            "STOP_2, STOP_1, COMPLETED"}, nullValues = "NIL")
    public void determinesTripStatusFromBusStops(BusStop from, BusStop to, TripStatus expected) {
        TripStatus actual = TripUtils.tripStatusOf(from, to);

        assertThat(actual, equalTo(expected));
    }
}
