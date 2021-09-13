package io.kuark.base.time

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class XLocalDateTest {

    @Test
    fun format() {
        val localDate = LocalDate.of(2021, 9, 10)
        assertEquals("2021-09-10", localDate.format(DateTimeFormatPattern.yyyy_MM_dd))
    }

}