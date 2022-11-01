package org.example.trip.model;

import java.time.LocalDateTime;

public class TapOn extends Tap {

    public TapOn(LocalDateTime timestamp, BusStop stop, String companyId, String busId, String pan) {
        super(timestamp, stop, companyId, busId, pan);
    }

    public String getTapType() {
        return "ON";
    }
}
