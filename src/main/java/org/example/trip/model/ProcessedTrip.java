package org.example.trip.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ProcessedTrip implements Comparable<ProcessedTrip> {

    private final LocalDateTime startTimestamp;
    private final LocalDateTime endTimestamp;
    private final Long durationSecs;
    private final BusStop from;
    private final BusStop to;
    private final BigDecimal amount;
    private final String companyId;
    private final String busId;
    private final String pan;
    private final TripStatus tripStatus;

    @Override
    public int compareTo(ProcessedTrip o) {
        Comparator<ProcessedTrip> c = Comparator.comparing(ProcessedTrip::getStartTimestamp)
                .thenComparing(ProcessedTrip::getEndTimestamp, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(ProcessedTrip::getPan);

        return c.compare(this, o);
    }
}
