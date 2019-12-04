package com.diegobezerra.truckpadcase.util

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DateUtilsTest {

    @Test
    fun getFormattedDuration_isCorrect() {
        // Given that the unit passed is below the supported
        var actual = getFormattedDuration(MINUTE_IN_SECONDS)
        var expected = ""

        // We should get an empty string
        assertEquals(expected, actual)

        // Given that the seconds passed is equal to 4 hours
        actual = getFormattedDuration(HOUR_IN_SECONDS * 4)
        expected = "4 horas"

        // We should get the expected string
        assertEquals(expected, actual)

        // Given that the seconds passed is equivalent to 4 days and 2 hours
        actual = getFormattedDuration(DAY_IN_SECONDS * 4 + HOUR_IN_SECONDS * 2)
        expected = "4 dias 2 horas"

        // We should get the expected string
        assertEquals(expected, actual)
    }

}