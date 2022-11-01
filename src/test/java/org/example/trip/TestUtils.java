package org.example.trip;

import org.example.trip.model.ProcessedTrip;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestUtils {

    public static void assertProcessedTrip(ProcessedTrip actual, ProcessedTrip expected) {
        assertThat(actual.getStartTimestamp(), equalTo(expected.getStartTimestamp()));
        assertThat(actual.getEndTimestamp(), equalTo(expected.getEndTimestamp()));
        assertThat(actual.getDurationSecs(), equalTo(expected.getDurationSecs()));
        assertThat(actual.getFrom(), equalTo(expected.getFrom()));
        assertThat(actual.getTo(), equalTo(expected.getTo()));
        assertThat(actual.getAmount(), equalTo(expected.getAmount()));
        assertThat(actual.getCompanyId(), equalTo(expected.getCompanyId()));
        assertThat(actual.getBusId(), equalTo(expected.getBusId()));
        assertThat(actual.getPan(), equalTo(expected.getPan()));
        assertThat(actual.getTripStatus(), equalTo(expected.getTripStatus()));
    }

    public static Path getOrCreateDirectoryFromClasspathRoot(String subDirPath) {
        try {
            Path classpathRoot = Paths.get(TestUtils.class.getResource("/").toURI());
            Path subDir = Paths.get(classpathRoot.toAbsolutePath().toString(), subDirPath);
            if (Files.notExists(subDir))
                Files.createDirectories(subDir);
            return subDir;
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path getResource(String filePath) {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        try {
            return Paths.get(classLoader.getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
