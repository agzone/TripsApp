package org.example.trip.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.trip.util.TripUtils;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class TapRecord {

    private final String id;

    private final Tap tap;

    public static TapRecord fromCsvString(String csv) {
        String[] cols = csv.split(",");
        String id = cols[0];
        LocalDateTime timestamp = TripUtils.stringToDateTime(str(cols[1]));
        String tapType = str(cols[2]);
        BusStop busStop = BusStop.valueOfStopName(str(cols[3]));
        String companyId = str(cols[4]);
        String busId = str(cols[5]);
        String pan = str(cols[6]);
        Tap tap = tapType.equals("OFF") ?
                new TapOff(timestamp, busStop, companyId, busId, pan) :
                new TapOn(timestamp, busStop, companyId, busId, pan);
        return new TapRecord(id, tap);
    }

    private static String str(String value) {
        return value.trim();
    }
}
