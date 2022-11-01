package org.example.trip.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TapRecordTest {

    @Test
    public void createsTapRecordFromCsvString() {
        String csv = "2, 02-01-2022 13:02:30, ON, Stop3, Company1, Bus1, 4444333322221111";
        LocalDateTime timestamp = LocalDateTime.of(2022, 1, 2, 13, 2, 30);
        TapRecord expected = new TapRecord("2",
                new TapOn(timestamp, BusStop.STOP_3, "Company1", "Bus1", "4444333322221111"));

        TapRecord actual = TapRecord.fromCsvString(csv);

        assertTapRecord(actual, expected);
    }

    private void assertTapRecord(TapRecord actual, TapRecord expected) {
        assertThat(actual, is(notNullValue()));
        Tap actualTap = actual.getTap();
        Tap expectedTap = expected.getTap();
        assertThat(actualTap, is(notNullValue()));

        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actualTap.getTimestamp(), equalTo(expectedTap.getTimestamp()));
        assertThat(actualTap.getTapType(), equalTo(expectedTap.getTapType()));
        assertThat(actualTap.getStop(), equalTo(expectedTap.getStop()));
        assertThat(actualTap.getCompanyId(), equalTo(expectedTap.getCompanyId()));
        assertThat(actualTap.getBusId(), equalTo(expectedTap.getBusId()));
        assertThat(actualTap.getPan(), equalTo(expectedTap.getPan()));
    }
}
