package com.example.guitarscaleviewer

import com.example.guitarscaleviewer.model.Interval
import com.example.guitarscaleviewer.model.IntervalModifier
import org.junit.Test
import org.junit.Assert.*

class IntervalUnitTest {
    @Test
    fun equalsTest() {

        // 1# = 2b
        assertEquals(Interval(1, IntervalModifier.SHARP), Interval(2, IntervalModifier.FLAT))

        // 2# = 3b
        assertEquals(Interval(2, IntervalModifier.SHARP), Interval(3, IntervalModifier.FLAT))

        // 3 = 4b
        assertEquals(Interval(3, IntervalModifier.NATURAL), Interval(4, IntervalModifier.FLAT))

        // 4 = 3#
        assertEquals(Interval(4, IntervalModifier.NATURAL), Interval(3, IntervalModifier.SHARP))

        // 4# = 5b
        assertEquals(Interval(4, IntervalModifier.SHARP), Interval(5, IntervalModifier.FLAT))

        // 5# = 6b
        assertEquals(Interval(5, IntervalModifier.SHARP), Interval(6, IntervalModifier.FLAT))

        // 6# = 7b
        assertEquals(Interval(6, IntervalModifier.SHARP), Interval(7, IntervalModifier.FLAT))

        // 7# = 1
        assertEquals(Interval(7, IntervalModifier.SHARP), Interval(1))

        // 1b = 7
        assertEquals(Interval(1, IntervalModifier.FLAT), Interval(7))
    }
}