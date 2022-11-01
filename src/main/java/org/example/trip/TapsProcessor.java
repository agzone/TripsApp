package org.example.trip;

import org.example.trip.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class TapsProcessor {
    private final TripTransformer tripTransformer;

    public TapsProcessor(TripTransformer tripTransformer) {
        this.tripTransformer = tripTransformer;
    }

    public List<ProcessedTrip> process(List<TapRecord> taps) {
        List<ProcessedTrip> coll = taps.stream()
                .map(record -> record.getTap())
                .collect(Collectors.groupingBy(Tap::getPan,
                        Collectors.toCollection(() -> new PriorityQueue<>(Comparator.comparing(Tap::getTimestamp)))))
                .entrySet()
                .stream()
                .map(entry -> transformTapsToTrips(entry.getValue()))
                .flatMap(List::stream)
                .map(trip -> tripTransformer.transform(trip))
                .sorted()
                .collect(Collectors.toList());
        return coll;
    }

    private List<Trip> transformTapsToTrips(Queue<Tap> taps) {
        List<Trip> result = new ArrayList<>();

        while (!taps.isEmpty()) {
            Tap tap = taps.remove();
            Tap nextTap = taps.peek();
            TapOff tapOff = null;
            if (nextTap != null && nextTap instanceof TapOff) {
                tapOff = (TapOff) taps.remove();
            }
            result.add(new Trip((TapOn) tap, tapOff));
        }
        return result;
    }
}
