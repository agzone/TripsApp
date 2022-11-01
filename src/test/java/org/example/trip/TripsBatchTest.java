package org.example.trip;

import org.example.trip.model.ProcessedTrip;
import org.example.trip.model.TapRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.example.trip.fixture.TripsFixture.generateTapsFixture;
import static org.example.trip.fixture.TripsFixture.processedTripsFixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class TripsBatchTest {

    static final File TAPS_FILE = TestUtils.getResource("fixtures/taps.csv").toFile();
    static final File TRIPS_FILE = new File(TestUtils.getOrCreateDirectoryFromClasspathRoot("outx").toFile(), "trips.csv");

    private TapsProcessor tapsProcessor = mock(TapsProcessor.class);
    private TripsCsvFileWriter fileWriter = mock(TripsCsvFileWriter.class);
    private TripsBatch batch = new TripsBatch(tapsProcessor, fileWriter);
    private static final List<ProcessedTrip> PROCESSED_TRIPS = processedTripsFixture();
    private static final List<TapRecord> TAPS = generateTapsFixture();

    @BeforeEach
    public void setup() {
        assertThat(TAPS_FILE.exists(), is(true));
        assertThat(TRIPS_FILE.exists(), is(false));
        when(tapsProcessor.process(anyList())).thenReturn(PROCESSED_TRIPS);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(TRIPS_FILE.toPath());
    }

    @Test
    public void batchReadsTapsFileAndWritesTripsToFile() throws IOException {

        batch.execute(TRIPS_FILE, TAPS_FILE);

        verify(tapsProcessor).process(TAPS);
        verify(fileWriter).write(PROCESSED_TRIPS, TRIPS_FILE);
    }

    @Test
    public void throwsExceptionWhenMissingArgument() {

        assertThrows(IllegalArgumentException.class, () -> {
            TripsBatch.main(new String[] { TAPS_FILE.toString() });
        });
    }

    @Test
    public void createsTripsFileFromProcessedTaps() throws IOException {
        long expectedTripsFileRowCount = PROCESSED_TRIPS.size() + 1;

        TripsBatch.main(new String[] { TAPS_FILE.toString(), TRIPS_FILE.toString() });

        assertThat("Trips file created?", TRIPS_FILE.exists(), is(true));
        assertThat(Files.lines(TRIPS_FILE.toPath()).count(), equalTo(expectedTripsFileRowCount));
    }
}
