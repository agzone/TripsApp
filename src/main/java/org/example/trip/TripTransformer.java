package org.example.trip;

import org.example.trip.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.example.trip.util.TripUtils.durationSecsBetween;
import static org.example.trip.util.TripUtils.tripStatusOf;
import static org.example.trip.model.TripStatus.INCOMPLETE;

public class TripTransformer {

    private final FareCalculator fareCalculator;

    public TripTransformer(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public ProcessedTrip transform(Trip trip) {
        Tap on = trip.getTapOn();
        Tap off = trip.getTapOff();
        Long durationSecs = null;
        BusStop tapOffStop = null;
        TripStatus status = INCOMPLETE;
        LocalDateTime tapOffTimestamp = null;
        if (off != null) {
            tapOffTimestamp = off.getTimestamp();
            durationSecs = durationSecsBetween(on.getTimestamp(), off.getTimestamp());
            tapOffStop = off.getStop();
            status = tripStatusOf(on.getStop(), tapOffStop);
        }
        BigDecimal amount = fareCalculator.calculate(on.getStop(), tapOffStop);

        ProcessedTrip result = ProcessedTrip.builder()
                .startTimestamp(on.getTimestamp())
                .endTimestamp(tapOffTimestamp)
                .durationSecs(durationSecs)
                .from(on.getStop())
                .to(tapOffStop)
                .amount(amount)
                .companyId(on.getCompanyId())
                .busId(on.getBusId())
                .pan(on.getPan())
                .tripStatus(status)
                .build();

        return result;
    }
}
