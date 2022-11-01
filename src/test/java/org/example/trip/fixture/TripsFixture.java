package org.example.trip.fixture;

import org.example.trip.model.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.trip.fixture.TripsFixture.Bus.*;
import static org.example.trip.fixture.TripsFixture.Company.*;
import static org.example.trip.fixture.TripsFixture.Fare.*;
import static org.example.trip.fixture.TripsFixture.Pan.*;
import static org.example.trip.util.TripUtils.tripStatusOf;
import static org.example.trip.model.BusStop.*;
import static org.example.trip.model.TripStatus.INCOMPLETE;

public class TripsFixture {

    enum Bus {
        BUS_1("Bus1"),
        BUS_2("Bus2"),
        BUS_3("Bus3"),
        BUS_4("Bus4");

        private final String busName;
        private Bus(String busName) {
            this.busName = busName;
        }

        public String getBusName() {
            return busName;
        }

        @Override
        public String toString() {
            return busName;
        }
    }

    enum Company {
        COMPANY_1("Company1"),
        COMPANY_2("Company2"),
        COMPANY_3("Company3");

        private final String companyName;
        private Company(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyName() {
            return companyName;
        }

        @Override
        public String toString() {
            return companyName;
        }
    }

    enum Pan {
        PAN_1("5454545454545454"),
        PAN_2("4444333322221111"),
        PAN_3("6799990100000000019");

        private final String pan;
        private Pan(String pan) {
            this.pan = pan;
        }
        public String getPan() {
            return pan;
        }
    }

    enum Fare {
        FARE_0(0),
        FARE_1(3.25),
        FARE_2(5.5),
        FARE_3(7.3);

        private final BigDecimal amount;

        private Fare(double val) {
            this.amount = BigDecimal.valueOf(val);
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }



    public static List<ProcessedTrip> processedTripsFixture() {
        LocalDateTime startDay1 = LocalDateTime.of(2022, 1, 2, 7, 0, 0);
        LocalDateTime startDay2 = startDay1.plusDays(1).plusHours(1);
        return Arrays.asList(
                processedTrip(startDay1, startDay1.plusMinutes(3), STOP_1, STOP_2, COMPANY_1, BUS_1, PAN_1, FARE_1),
                processedTrip(startDay1.plusMinutes(1), startDay1.plusMinutes(2), STOP_3, STOP_3, COMPANY_1,BUS_1, PAN_2, FARE_0),
                processedIncompleteTrip(startDay1.plusMinutes(4), STOP_2, COMPANY_1, BUS_3, PAN_1, FARE_2),
                processedTrip(startDay1.plusMinutes(5), startDay1.plusMinutes(7), STOP_3, STOP_2, COMPANY_2, BUS_2, PAN_3, FARE_2),
                processedTrip(startDay2, startDay2.plusMinutes(8), STOP_1, STOP_3, COMPANY_3, BUS_4, PAN_1, FARE_3),
                processedIncompleteTrip(startDay2, STOP_3, COMPANY_1, BUS_3, PAN_2, FARE_3),
                processedTrip(startDay2.plusMinutes(1), startDay2.plusMinutes(4), STOP_2, STOP_1, COMPANY_2, BUS_2, PAN_3, FARE_1)
        ).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<Trip> convertToTrips(List<ProcessedTrip> processedTrips) {
        return processedTrips.stream()
                .map(pt -> {
                    TapOn on = new TapOn(pt.getStartTimestamp(), pt.getFrom(), pt.getCompanyId(),
                            pt.getBusId(), pt.getPan());
                    TapOff off = pt.getTo() != null ?
                            new TapOff(pt.getEndTimestamp(), pt.getTo(), pt.getCompanyId(),
                                    pt.getBusId(), pt.getPan()) : null;
                    return new Trip(on, off);
                })
                .collect(Collectors.toList());
    }

    public static List<TapRecord> generateTapsFixture() {
        List<Tap> taps = convertToTrips(processedTripsFixture()).stream()
                .map(trip -> {
                    List<Tap> result = new ArrayList<>();
                    result.add(trip.getTapOn());
                    if (trip.getTapOff() != null)
                        result.add(trip.getTapOff());
                    return result;
                })
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Tap::getTimestamp))
                .collect(Collectors.toList());

        return IntStream.range(0, taps.size())
                .mapToObj(i -> new TapRecord(String.valueOf(i + 1), taps.get(i)))
                .collect(Collectors.toList());
    }

    private static ProcessedTrip processedTrip(LocalDateTime started, LocalDateTime finished, BusStop from, BusStop to,
                                               Company company, Bus bus, Pan pan, Fare fare) {
        long durationSecs = Duration.between(started, finished).getSeconds();
        TripStatus status = tripStatusOf(from, to);
        return ProcessedTrip.builder()
                .startTimestamp(started)
                .endTimestamp(finished)
                .durationSecs(durationSecs)
                .from(from)
                .to(to)
                .amount(fare.getAmount())
                .companyId(company.getCompanyName())
                .busId(bus.getBusName())
                .pan(pan.getPan())
                .tripStatus(status)
                .build();
    }

    private static ProcessedTrip processedIncompleteTrip(LocalDateTime started, BusStop from,
                                                         Company company, Bus bus, Pan pan, Fare fare) {
        return ProcessedTrip.builder()
                .startTimestamp(started)
                .from(from)
                .amount(fare.getAmount())
                .companyId(company.getCompanyName())
                .busId(bus.getBusName())
                .pan(pan.getPan())
                .tripStatus(INCOMPLETE)
                .build();
    }
}
