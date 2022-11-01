package org.example.trip.model;

import java.util.HashMap;
import java.util.Map;

public enum BusStop {
    STOP_1("Stop1"),
    STOP_2("Stop2"),
    STOP_3("Stop3");

    private static final Map<String, BusStop> STOP_NAMES = new HashMap<>();

    static {
        for (BusStop stop: values()) {
            STOP_NAMES.put(stop.stopName, stop);
        }
    }

    private final String stopName;

    private BusStop(String stopName) {
        this.stopName = stopName;
    }

    @Override
    public String toString() {
        return stopName;
    }

    public static BusStop valueOfStopName(String stopName) {
        return STOP_NAMES.get(stopName);
    }
}
