package org.example.trip.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TapTest {
    private LocalDateTime timestamp = LocalDateTime.of(2022, 1, 22, 13, 2, 30);
    private BusStop busStop = BusStop.STOP_1;
    private String companyId = "company1";
    private String busId = "Bus1";
    private String pan = "5500005555555559";

    @Test
    public void tapOnHasCorrectTapType() {
        TapOn actual = new TapOn(timestamp, busStop, companyId, busId, pan);

        assertThat(actual.getTapType(), equalTo("ON"));
    }

    @Test
    public void tapOffHasCorrectTapType() {
        TapOff actual = new TapOff(timestamp, busStop, companyId, busId, pan);

        assertThat(actual.getTapType(), equalTo("OFF"));
    }
}
