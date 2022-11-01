package org.example.trip.model;

import java.time.LocalDateTime;

public class TapOff extends Tap {

    public TapOff(LocalDateTime timestamp, BusStop stop, String companyId, String busId, String pan) {
        super(timestamp, stop, companyId, busId, pan);
    }

    public String getTapType() {
        return "OFF";
    }
}
