package io.kuark.base.time

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalTime

internal class XLocalTimeTest {

    private val localTime = LocalTime.of(17, 15, 1)

    @Test
    fun format() {
        Assertions.assertEquals("17:15:01", localTime.format(DateTimeFormatPattern.HH_mm_ss))
    }

}