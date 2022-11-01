package org.example.trip;

import org.example.trip.fixture.TripsFixture;
import org.example.trip.model.ProcessedTrip;
import org.example.trip.model.TapRecord;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.example.trip.TestUtils.assertProcessedTrip;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TapsProcessorTest {

    private FareCalculator fareCalculator = new FareCalculator();
    private TripTransformer tripTransformer = new TripTransformer(fareCalculator);
    private TapsProcessor tapsProcessor = new TapsProcessor(tripTransformer);

    @Test
    public void returnsProcessedTripsSortedFromTapsList() {
        List<TapRecord> taps = TripsFixture.generateTapsFixture();
        List<ProcessedTrip> expected = TripsFixture.processedTripsFixture();

        List<ProcessedTrip> actual = tapsProcessor.process(taps);

        assertThat("ProcessedTrip list", actual, is(notNullValue()));
        IntStream.range(0, expected.size())
                .forEach(i -> assertProcessedTrip(actual.get(i), expected.get(i)));
    }
}
