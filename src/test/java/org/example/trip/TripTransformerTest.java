package org.example.trip;

import org.example.trip.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.example.trip.TestUtils.assertProcessedTrip;
import static org.example.trip.util.TripUtils.stringToDateTime;
import static org.example.trip.model.BusStop.STOP_1;
import static org.example.trip.model.BusStop.STOP_2;
import static org.example.trip.model.TripStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TripTransformerTest {

    private BigDecimal amount = BigDecimal.valueOf(5.0);
    private FareCalculator fareCalculator = mock(FareCalculator.class);
    private TripTransformer tripTransformer = new TripTransformer(fareCalculator);

    private LocalDateTime started = stringToDateTime("22-01-2018 13:00:00");
    private LocalDateTime finished = stringToDateTime("22-01-2018 13:05:00");
    private Long durationSecs = 300L;
    private String companyId = "Company1";
    private String busId = "Bus37";
    private String pan = "5500005555555559";

    @BeforeEach
    public void setup() {
        when(fareCalculator.calculate(any(BusStop.class), any())).thenReturn(amount);
    }

    @Test
    public void calculatesCompletedTrip() {
        BusStop from = STOP_1;
        BusStop to = STOP_2;
        Trip trip = new Trip(createTapOn(from), createTapOff(to));
        ProcessedTrip expected = buildProcessedTripWithTwoTaps(from, to, COMPLETED).build();

        ProcessedTrip actual = tripTransformer.transform(trip);

        assertThat(actual, is(notNullValue()));
        assertProcessedTrip(actual, expected);
    }

    @Test
    public void calculatesIncompleteTrip() {
        BusStop from = STOP_2;
        Trip trip = new Trip(createTapOn(from), null);
        ProcessedTrip expected = buildProcessedTripWithOneTap(from, INCOMPLETE).build();

        ProcessedTrip actual = tripTransformer.transform(trip);

        assertThat(actual, is(notNullValue()));
        assertProcessedTrip(actual, expected);
    }

    @Test
    public void calculatesCancelledTrip() {
        finished = stringToDateTime("22-01-2018 13:01:00");
        durationSecs = 60L;
        BusStop from = STOP_1;
        BusStop to = STOP_1;
        Trip trip = new Trip(createTapOn(from), createTapOff(to));
        ProcessedTrip expected = buildProcessedTripWithTwoTaps(from, to, CANCELLED).build();

        ProcessedTrip actual = tripTransformer.transform(trip);

        assertThat(actual, is(notNullValue()));
        assertProcessedTrip(actual, expected);
    }

    private TapOn createTapOn(BusStop from) {
        return new TapOn(started, from, companyId, busId, pan);
    }

    private TapOff createTapOff(BusStop to) {
        return new TapOff(finished, to, companyId, busId, pan);
    }

    private ProcessedTrip.ProcessedTripBuilder buildProcessedTripWithTwoTaps(
            BusStop from, BusStop to, TripStatus status) {
        return buildProcessedTripWithOneTap(from, status)
                .endTimestamp(finished)
                .durationSecs(durationSecs)
                .to(to);

    }

    private ProcessedTrip.ProcessedTripBuilder buildProcessedTripWithOneTap(
            BusStop from, TripStatus status) {
        return ProcessedTrip.builder()
                .startTimestamp(started)
                .from(from)
                .amount(amount)
                .companyId(companyId)
                .busId(busId)
                .pan(pan)
                .tripStatus(status);
    }
}
