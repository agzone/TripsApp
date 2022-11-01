package org.example.trip;

import org.example.trip.model.BusStop;

import java.math.BigDecimal;

public class FareCalculator {

    private static final BigDecimal[][] amounts = new BigDecimal[][] {
            { value(0), value(3.25), value(7.3) },
            { value(3.25), value(0), value(5.5) },
            { value(7.3), value(5.5), value(0) }
    };

    private static BigDecimal value(double val) {
        return BigDecimal.valueOf(val);
    }

    public BigDecimal calculate(BusStop from, BusStop to) {
        return to == null ? getMaxChargeAmount(from.ordinal()) : amounts[from.ordinal()][to.ordinal()];
    }

    private BigDecimal getMaxChargeAmount(int index1) {
        int indexMax = amounts[index1].length - 1;
        return amounts[index1][0].max(amounts[index1][indexMax]);
    }

}
