package org.example.trip;

import org.example.trip.model.ProcessedTrip;
import org.example.trip.model.TripStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import static org.example.trip.util.TripUtils.dateTimeToString;

public class TripsCsvFileWriter {

    public void write(List<ProcessedTrip> trips, File tripsFile) {
        try (PrintWriter pw = new PrintWriter(tripsFile)) {
            pw.println(createCsvHeading());

            trips.stream()
                    .map(this::convertToCsvString)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private String createCsvHeading() {
        return new StringBuilder("Started")
                .append(col("Finished"))
                .append(col("DurationSecs"))
                .append(col("FromStopId"))
                .append(col("ToStopId"))
                .append(col("ChargeAmount"))
                .append(col("CompanyId"))
                .append(col("BusID"))
                .append(col("PAN"))
                .append(col("Status"))
                .toString();
    }

    private String convertToCsvString(ProcessedTrip trip) {
        boolean isIncomplete = trip.getTripStatus().equals(TripStatus.INCOMPLETE);

        return new StringBuilder(dateTimeToString(trip.getStartTimestamp()))
                .append(col(isIncomplete ? "" : dateTimeToString(trip.getEndTimestamp())))
                .append(col(isIncomplete ? "" : String.valueOf(trip.getDurationSecs())))
                .append(col(trip.getFrom().toString()))
                .append(col(isIncomplete ? "" : trip.getTo().toString()))
                .append(col("$" + trip.getAmount().setScale(2)))
                .append(col(trip.getCompanyId()))
                .append(col(trip.getBusId()))
                .append(col(trip.getPan()))
                .append(col(trip.getTripStatus().toString()))
                .toString();
    }

    private String col(String value) {
        return ", " + value;
    }
}
