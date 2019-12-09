package e.caioluis.android_case.utils

import e.caioluis.android_case.util.*
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun convertMetersToKm_isCorrect() {

        assertEquals("28.123 Km", 28123.convertMetersToKm())
    }

    @Test
    fun secondsToHours_isCorrect() {

        assertEquals("1h 16m 27s", 4587.secondsToHours())
    }

    @Test
    fun toBRLCurrency_isCorrect() {

        assertEquals("R$  25,37", 25.37.toBRLCurrency())
    }

    @Test
    fun doubleToString_IsCorrect() {

        assertEquals("2.2", 2.2.doubleToString())
    }

    @Test
    fun intToString_isCorrect() {

        assertEquals("25", 25.intToString())
    }

    @Test
    fun formatLiters_isCorrect(){

        assertEquals("1.3 L", 1.3.formatLiters())
    }
}