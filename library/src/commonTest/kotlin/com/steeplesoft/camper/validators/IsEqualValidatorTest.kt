package com.steeplesoft.camper.validators

import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class IsEqualValidatorTest {

    @Test
    fun validate_withEqualStringValues_returnsTrue() {
        // Arrange
        val expectedValue = "test"
        val validator = IsEqualValidator<String>({ expectedValue })

        // Act
        val result = validator.validate("test")

        // Assert
        assertTrue("Equal string values should pass validation", result)
    }

    @Test
    fun validate_withDifferentStringValues_returnsFalse() {
        // Arrange
        val expectedValue = "test"
        val validator = IsEqualValidator<String>({ expectedValue })

        // Act
        val result = validator.validate("different")

        // Assert
        assertFalse(result, "Different string values should fail validation")
    }

    @Test
    fun validate_withEqualIntegerValues_returnsTrue() {
        // Arrange
        val expectedValue = 42
        val validator = IsEqualValidator<Int>({ expectedValue })

        // Act
        val result = validator.validate(42)

        // Assert
        assertTrue("Equal integer values should pass validation", result)
    }

    @Test
    fun validate_withDifferentIntegerValues_returnsFalse() {
        // Arrange
        val expectedValue = 42
        val validator = IsEqualValidator<Int>({ expectedValue })

        // Act
        val result = validator.validate(24)

        // Assert
        assertFalse(result, "Different integer values should fail validation")
    }

    @Test
    fun validate_withBothNullValues_returnsTrue() {
        // Arrange
        val validator = IsEqualValidator<String?>({ null })

        // Act
        val result = validator.validate(null)

        // Assert
        assertTrue("Both null values should be considered equal", result)
    }

    @Test
    fun validate_withNullInputAndNonNullExpected_returnsFalse() {
        // Arrange
        val expectedValue = "test"
        val validator = IsEqualValidator<String?>({ expectedValue })

        // Act
        val result = validator.validate(null)

        // Assert
        assertFalse(result, "Null input should not equal non-null expected value")
    }

    @Test
    fun validate_withNonNullInputAndNullExpected_returnsFalse() {
        // Arrange
        val validator = IsEqualValidator<String?>({ null })

        // Act
        val result = validator.validate("test")

        // Assert
        assertFalse(result, "Non-null input should not equal null expected value")
    }

    @Test
    fun validate_withEqualBooleanValues_returnsTrue() {
        // Arrange
        val expectedValue = true
        val validator = IsEqualValidator<Boolean>({ expectedValue })

        // Act
        val result = validator.validate(true)

        // Assert
        assertTrue("Equal boolean values should pass validation", result)
    }

    @Test
    fun validate_withDifferentBooleanValues_returnsFalse() {
        // Arrange
        val expectedValue = true
        val validator = IsEqualValidator<Boolean>({ expectedValue })

        // Act
        val result = validator.validate(false)

        // Assert
        assertFalse(result, "Different boolean values should fail validation")
    }

    @Test
    fun validate_withEqualDoubleValues_returnsTrue() {
        // Arrange
        val expectedValue = 3.14
        val validator = IsEqualValidator<Double>({ expectedValue })

        // Act
        val result = validator.validate(3.14)

        // Assert
        assertTrue("Equal double values should pass validation", result)
    }

    @Test
    fun validate_withDifferentDoubleValues_returnsFalse() {
        // Arrange
        val expectedValue = 3.14
        val validator = IsEqualValidator<Double>({ expectedValue })

        // Act
        val result = validator.validate(2.71)

        // Assert
        assertFalse(result, "Different double values should fail validation")
    }

    @Test
    fun validate_withEqualCustomObjects_returnsTrue() {
        // Arrange
        data class TestObject(val value: String)

        val expectedObject = TestObject("test")
        val validator = IsEqualValidator<TestObject>({ expectedObject })

        // Act
        val result = validator.validate(TestObject("test"))

        // Assert
        assertTrue("Equal data class objects should pass validation", result)
    }

    @Test
    fun validate_withDifferentCustomObjects_returnsFalse() {
        // Arrange
        data class TestObject(val value: String)

        val expectedObject = TestObject("test")
        val validator = IsEqualValidator<TestObject>({ expectedObject })

        // Act
        val result = validator.validate(TestObject("different"))

        // Assert
        assertFalse(result, "Different data class objects should fail validation")
    }

    @Test
    fun validate_withDynamicExpectedValue_worksCorrectly() {
        // Arrange
        var expectedValue = "initial"
        val validator = IsEqualValidator<String>({ expectedValue })

        // Act & Assert - First validation
        assertTrue(
            "Should pass with initial expected value",
            validator.validate("initial")
        )

        // Change expected value
        expectedValue = "changed"

        // Act & Assert - Second validation
        assertTrue(
            "Should pass with changed expected value",
            validator.validate("changed")
        )
        assertFalse(validator.validate("initial"), "Should fail with old value")
    }

    @Test
    fun validate_withZeroValues_returnsTrue() {
        // Arrange
        val expectedValue = 0
        val validator = IsEqualValidator<Int>({ expectedValue })

        // Act
        val result = validator.validate(0)

        // Assert
        assertTrue("Zero values should be equal", result)
    }

    @Test
    fun validate_withEmptyStringValues_returnsTrue() {
        // Arrange
        val expectedValue = ""
        val validator = IsEqualValidator<String>({ expectedValue })

        // Act
        val result = validator.validate("")

        // Assert
        assertTrue("Empty string values should be equal", result)
    }

    @Test
    fun validate_withEmptyAndNonEmptyStrings_returnsFalse() {
        // Arrange
        val expectedValue = ""
        val validator = IsEqualValidator<String>({ expectedValue })

        // Act
        val result = validator.validate("non-empty")

        // Assert
        assertFalse(result, "Empty and non-empty strings should not be equal")
    }

    @Test
    fun validate_withWhitespaceStrings_checksExactEquality() {
        // Arrange
        val expectedValue = "  "
        val validator = IsEqualValidator<String>({ expectedValue })

        // Act & Assert
        assertTrue(
            "Exact whitespace strings should be equal",
            validator.validate("  ")
        )
        assertFalse(validator.validate(" "), "Different whitespace strings should not be equal")
        assertFalse(validator.validate(""), "Whitespace and non-whitespace should not be equal")
    }

    @Test
    fun validate_withListValues_checksEquality() {
        // Arrange
        val expectedValue = listOf(1, 2, 3)
        val validator = IsEqualValidator<List<Int>>({ expectedValue })

        // Act & Assert
        assertTrue(
            "Equal lists should pass validation",
            validator.validate(listOf(1, 2, 3))
        )
        assertFalse(validator.validate(listOf(3, 2, 1)), "Different lists should fail validation")
        assertFalse(validator.validate(listOf(1, 2)), "Different sized lists should fail validation")
    }

    @Test
    fun errorText_withDefaultMessage_returnsExpectedText() {
        // Arrange
        val validator = IsEqualValidator<String>({ "test" })

        // Act & Assert
        assertEquals(
            "This field's value is not as expected.",
            validator.errorText,
            "Default error message should match expected text"
        )
    }

    @Test
    fun errorText_withCustomMessage_returnsCustomText() {
        // Arrange
        val customMessage = "Password confirmation does not match"
        val validator = IsEqualValidator<String>({ "test" }, customMessage)

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
        val validator = IsEqualValidator<String>({ "test" }, null)

        // Act & Assert
        assertEquals(
            "This field's value is not as expected.",
            validator.errorText,
            "Null custom message should fallback to default"
        )
    }

    @Test
    fun validate_withCaseSensitiveStrings_checksExactCase() {
        // Arrange
        val expectedValue = "Test"
        val validator = IsEqualValidator<String>({ expectedValue })

        // Act & Assert
        assertTrue(
            "Exact case should pass validation",
            validator.validate("Test")
        )
        assertFalse(validator.validate("test"), "Different case should fail validation")
        assertFalse(validator.validate("TEST"), "All uppercase should fail validation")
    }

    @Test
    fun validate_withFunctionReturningDifferentValues_usesCurrentValue() {
        // Arrange
        var counter = 0
        val validator = IsEqualValidator<Int>({ ++counter })

        // Act & Assert
        // First call should increment counter to 1
        assertTrue(
            "Should pass with current counter value",
            validator.validate(1)
        )

        // Second call should increment counter to 2
        assertTrue(
            "Should pass with updated counter value",
            validator.validate(2)
        )

        // Should fail with old counter value
        assertFalse(validator.validate(1), "Should fail with old counter value")
    }
}
