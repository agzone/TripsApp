package org.example.trip;

import org.example.trip.fixture.TripsFixture;
import org.example.trip.model.ProcessedTrip;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TripsCsvFileWriterTest {

    static final File TRIPS_FILE = new File(TestUtils.getOrCreateDirectoryFromClasspathRoot("out").toFile(), "TripsFileWriterTest.csv");

    @BeforeEach
    public void setup() {
        assertThat(TRIPS_FILE.exists(), is(false));
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(TRIPS_FILE.toPath());
    }

    @Test
    public void writesTripsToFile() throws IOException {
        List<ProcessedTrip> trips = TripsFixture.processedTripsFixture();

        new TripsCsvFileWriter().write(trips, TRIPS_FILE);

        assertThat(TRIPS_FILE.exists(), is(true));
        assertThat(Files.lines(TRIPS_FILE.toPath()).count(), equalTo(trips.size() + 1L));
    }
}


