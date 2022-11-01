package org.example.trip.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BusStopTest {

    @ParameterizedTest
    @CsvSource({"Stop1, STOP_1", "Stop2, STOP_2", "Stop3, STOP_3"})
    public void returnsCorrectValueFromStopName(String input, BusStop expected) {
        assertThat(BusStop.valueOfStopName(input), equalTo(expected));
    }
}
