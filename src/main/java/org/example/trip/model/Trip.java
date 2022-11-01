package org.example.trip.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Trip {
    private final TapOn tapOn;
    private final TapOff tapOff;
}
