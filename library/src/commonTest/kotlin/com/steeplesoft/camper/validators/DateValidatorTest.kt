package com.steeplesoft.camper.validators

import com.steeplesoft.camper.fields.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse


class DateValidatorTest {

    @Test
    fun validate_withDateAfterMinDateTime_returnsTrue() {
        // Arrange
        val minDateTime = LocalDate(2023, 1, 1)
        val validator = DateValidator({ minDateTime })
        val testDate = LocalDate(2023, 6, 15)

        // Act
        val result = validator.validate(testDate)

        // Assert
        assertTrue("Date after minimum should pass validation", result)
    }

    @Test
    fun validate_withDateBeforeMinDateTime_returnsFalse() {
        // Arrange
        val minDateTime = LocalDate(2023, 6, 1)
        val validator = DateValidator({ minDateTime })
        val testDate = LocalDate(2023, 1, 15)

        // Act
        val result = validator.validate(testDate)

        // Assert
        assertFalse(result, "Date before minimum should fail validation")
    }

    @Test
    fun validate_withDateExactlyAtMinDateTime_returnsTrue() {
        // Arrange
        val minDateTime = LocalDate(2023, 6, 1)
        val validator = DateValidator({ minDateTime })
        val testDate = minDateTime

        // Act
        val result = validator.validate(testDate)

        // Assert
        assertTrue("Date exactly at minimum should pass validation", result)
    }

    @Test
    fun validate_withNullDate_returnsFalse() {
        // Arrange
        val validator = DateValidator({ LocalDate.now() })

        // Act
        val result = validator.validate(null)

        // Assert
        assertFalse(result, "Null date should fail validation")
    }
    /*

        @Test
        fun validate_withCurrentDateAndPastMinimum_returnsTrue() {
            // Arrange
    //        val today = LocalDate.now()
    //        val oneDayAgo = LocalDate.now() - (24 * 60 * 60 * 1000)
            val now = val now = Clock.System.now().minus(Duration.
            val validator = DateValidator({
                now - DatePeriod(days = 1) })
            val currentDate = Date()

            // Act
            val result = validator.validate(currentDate)

            // Assert
            assertTrue("Current date should pass validation with past minimum", result)
        }
    */

    @Test
    fun validate_withCurrentDateAndFutureMinimum_returnsFalse() {
        val currentDate = LocalDate.now()
        val oneDayFromNow = currentDate.plus(1, DateTimeUnit.DAY)
        val validator = DateValidator({ oneDayFromNow })

        // Act
        val result = validator.validate(currentDate)

        // Assert
        assertFalse(result, "Current date should fail validation with future minimum")
    }

    @Test
    fun validate_withVeryOldDate_behavesCorrectly() {
        // Arrange
        val minDateTime = LocalDate(2000, 1, 1)
        val validator = DateValidator({ minDateTime })
        val veryOldDate = LocalDate(1990, 12, 25)

        // Act
        val result = validator.validate(veryOldDate)

        // Assert
        assertFalse(result, "Very old date should fail validation")
    }

    @Test
    fun validate_withVeryFutureDate_returnsTrue() {
        // Arrange
        val validator = DateValidator({ LocalDate.now() })
        val futureDate = LocalDate(2050, 12, 31)

        // Act
        val result = validator.validate(futureDate)

        // Assert
        assertTrue("Future date should pass validation with current minimum", result)
    }

    @Test
    fun validate_withDynamicMinDateTime_worksCorrectly() {
        // Arrange
        var minDateTime = LocalDate(2023, Month.JANUARY, 1)
        val validator = DateValidator({ minDateTime })
        val testDate = LocalDate(2023, Month.JUNE, 1)

        // Act & Assert - First validation
        assertTrue(
            "Date should pass with initial minimum",
            validator.validate(testDate)
        )

        // Change minimum to future date
        minDateTime = LocalDate(2023, Month.DECEMBER, 1)

        // Act & Assert - Second validation
        assertFalse(validator.validate(testDate), "Date should fail with updated minimum")
    }

    @Test
    fun validate_withSpecificDateScenarios_worksCorrectly() {
        // Test leap year date
        val leapYearDate = LocalDate(2024, Month.FEBRUARY, 29)
        val beforeLeapYear = LocalDate(2024, Month.JANUARY, 1)
        val validator = DateValidator({ beforeLeapYear })

        assertTrue(
            "Leap year date should pass validation",
            validator.validate(leapYearDate)
        )
    }

    @Test
    fun validate_withEndOfYearDates_worksCorrectly() {
        // Arrange
        val minDateTime = LocalDate(2023, Month.DECEMBER, 31)
        val validator = DateValidator({ minDateTime })
        val newYearDate = LocalDate(2024, Month.JANUARY, 1)

        // Act
        val result = validator.validate(newYearDate)

        // Assert
        assertTrue("New Year date should pass validation", result)
    }

    @Test
    fun errorText_withDefaultMessage_returnsExpectedText() {
        // Arrange
        val validator = DateValidator({ LocalDate.now() })

        // Act & Assert
        assertEquals(
            "This field is not valid.",
            validator.errorText,
            "Default error message should match expected text"
        )
    }

    @Test
    fun errorText_withCustomMessage_returnsCustomText() {
        // Arrange
        val customMessage = "Date must be today or later"
        val validator = DateValidator({ LocalDate.now() }, customMessage)

        // Act & Assert
        assertEquals(
            customMessage,
            validator.errorText,
            "Custom error message should be returned"
        )
    }

    @Test
    fun errorText_withNullCustomMessage_returnsDefaultText() {
        // Arrange
        val validator = DateValidator({ LocalDate.now() }, null)

        // Act & Assert
        assertEquals(
            "This field is not valid.",
            validator.errorText,
            "Null custom message should fallback to default"
        )
    }
}
