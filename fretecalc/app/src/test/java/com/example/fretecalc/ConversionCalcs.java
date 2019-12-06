package com.example.fretecalc;

import org.junit.Test;

import static com.example.fretecalc.utils.ConvertersUtils.distanceUnits;
import static com.example.fretecalc.utils.ConvertersUtils.timeUnits;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link com.example.fretecalc.utils.ConvertersUtils}.
 **/
public class ConversionCalcs {
    @Test
    public void distanceCalculations() {
        Double results = distanceUnits("meters", 3000.0);
        assertEquals((Double) 3.0, results);
    }

    @Test
    public void secondsToHoursCalculations() {
        String results = timeUnits("seconds", 5000L);
        assertEquals("1h23", results);
    }

    @Test
    public void millisecondsToHoursCalculations() {
        String results = timeUnits("milliseconds", 5000000L);
        assertEquals("1h23", results);
    }

    @Test
    public void minutesToHoursCalculations() {
        String results = timeUnits("minutes", 83L);
        assertEquals("1h23", results);
    }

    @Test
    public void timeUnitNotSetCalculations() {
        String results = timeUnits("", 12345L);
        assertEquals("", results);
    }
}
