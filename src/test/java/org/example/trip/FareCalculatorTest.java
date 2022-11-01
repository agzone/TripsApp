package org.example.trip;

import org.example.trip.model.BusStop;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "STOP_1, STOP_2, 3.25",
            "STOP_1, STOP_3, 7.3",
            "STOP_1, NIL, 7.3",
            "STOP_2, STOP_1, 3.25",
            "STOP_2, STOP_3, 5.5",
            "STOP_2, NIL, 5.5",
            "STOP_3, STOP_1, 7.3",
            "STOP_3, STOP_2, 5.5",
            "STOP_3, NIL, 7.3"}, nullValues = "NIL")
    public void calculatesPriceOfTrip(BusStop from, BusStop to, BigDecimal amount) {
        BigDecimal actual = new FareCalculator().calculate(from, to);
        assertThat(actual, equalTo(amount));
    }

    @ParameterizedTest
    @CsvSource({
            "STOP_1, STOP_1",
            "STOP_2, STOP_2",
            "STOP_3, STOP_3"})
    public void returnsZeroWhenFromAndToAreEqual(BusStop from, BusStop to) {
        BigDecimal actual = new FareCalculator().calculate(from, to);
        assertThat(actual, equalTo(BigDecimal.valueOf(0.0)));
    }
}
