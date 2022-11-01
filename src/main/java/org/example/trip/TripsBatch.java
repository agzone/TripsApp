package org.example.trip;

import lombok.extern.slf4j.Slf4j;
import org.example.trip.model.ProcessedTrip;
import org.example.trip.model.TapRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TripsBatch {

    private final TapsProcessor tapsProcessor;
    private final TripsCsvFileWriter fileWriter;


    public TripsBatch(TapsProcessor tapsProcessor, TripsCsvFileWriter fileWriter) {
        this.tapsProcessor = tapsProcessor;
        this.fileWriter = fileWriter;
    }

    public void execute(File output, File source) throws IOException {
        List<TapRecord> taps = Files.lines(source.toPath(), StandardCharsets.UTF_8)
                .skip(1)
                .map(TapRecord::fromCsvString)
                .collect(Collectors.toList());

        List<ProcessedTrip> trips = tapsProcessor.process(taps);
        fileWriter.write(trips, output);
    }

    public static void main(String[] args) {
        log.info("Trips batch started with arguments:");
        Arrays.stream(args).forEach(s -> log.info("\t- {}", s));

        if (args.length < 2)
            throw new IllegalArgumentException("Invalid number of arguments provided. Source file and output file paths are required.");

        String sourceFilePath = args[0];
        String outputFilePath = args[1];
        File source = new File(sourceFilePath);
        File output = new File(outputFilePath);

        FareCalculator fareCalculator = new FareCalculator();
        TripTransformer tripTransformer = new TripTransformer(fareCalculator);
        TapsProcessor processor = new TapsProcessor(tripTransformer);
        TripsCsvFileWriter writer = new TripsCsvFileWriter();

        log.info("Starting taps file processing from file: {}", sourceFilePath);
        try {
            new TripsBatch(processor, writer).execute(output, source);
        } catch (Exception e) {
            log.error(String.format(
                    "Something went wrong processing taps file: %s, to output file: %s", sourceFilePath, outputFilePath), e);
            System.exit(1);
        }
        log.info("Finsihed procssing taps. Created trips file: {}", outputFilePath);
    }
}
