package org.example.trip.model;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class Tap {

    private final LocalDateTime timestamp;
    private final BusStop stop;
    private final String companyId;
    private final String busId;
    private final String pan;

    @ToString.Include(name = "tapType")
    public abstract String getTapType();
}
