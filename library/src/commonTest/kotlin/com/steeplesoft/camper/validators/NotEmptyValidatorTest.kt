package com.steeplesoft.camper.validators

import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class NotEmptyValidatorTest {

    @Test
    fun validate_withNullValue_returnsFalse() {
        // Arrange
        val validator = NotEmptyValidator<String>()

        // Act
        val result = validator.validate(null)

        // Assert
        assertFalse(result, "Null value should be considered empty")
    }

    @Test
    fun validate_withNonNullStringValue_returnsTrue() {
        // Arrange
        val validator = NotEmptyValidator<String>()

        // Act
        val result = validator.validate("test")

        // Assert
        assertTrue("Non-null string should be considered non-empty", result)
    }

    @Test
    fun validate_withEmptyString_returnsTrue() {
        // Arrange
        val validator = NotEmptyValidator<String>()

        // Act
        val result = validator.validate("")

        // Assert
        assertTrue("Empty string should still be considered non-empty (not null)", result)
    }

    @Test
    fun validate_withWhitespaceString_returnsTrue() {
        // Arrange
        val validator = NotEmptyValidator<String>()

        // Act
        val result = validator.validate("   ")

        // Assert
        assertTrue("Whitespace string should be considered non-empty", result)
    }

    @Test
    fun validate_withNonNullIntegerValue_returnsTrue() {
        // Arrange
        val validator = NotEmptyValidator<Int>()

        // Act
        val result = validator.validate(42)

        // Assert
        assertTrue("Non-null integer should be considered non-empty", result)
    }

    @Test
    fun validate_withZeroValue_returnsTrue() {
        // Arrange
        val validator = NotEmptyValidator<Int>()

        // Act
        val result = validator.validate(0)

        // Assert
        assertTrue("Zero value should be considered non-empty", result)
    }

    @Test
    fun validate_withNullIntegerValue_returnsFalse() {
        // Arrange
        val validator = NotEmptyValidator<Int>()

        // Act
        val result = validator.validate(null)

        // Assert
        assertFalse(result, "Null integer should be considered empty")
    }

    @Test
    fun validate_withCustomObjectValue_returnsTrue() {
        // Arrange
        val validator = NotEmptyValidator<Any>()
        val testObject = Any()

        // Act
        val result = validator.validate(testObject)

        // Assert
        assertTrue("Non-null object should be considered non-empty", result)
    }

    @Test
    fun errorText_withDefaultMessage_returnsExpectedText() {
        // Arrange
        val validator = NotEmptyValidator<String>()

        // Act & Assert
        assertEquals(
            validator.errorText, "This field should not be empty",
            "Default error message should match expected text"
        )
    }

    @Test
    fun errorText_withCustomMessage_returnsCustomText() {
        // Arrange
        val customMessage = "Custom error message"
        val validator = NotEmptyValidator<String>(customMessage)

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
        val validator = NotEmptyValidator<String>(null)

        // Act & Assert
        assertEquals(
            "This field should not be empty",
            validator.errorText,
            "Null custom message should fallback to default"
        )
    }

    @Test
    fun validate_withEmptyList_returnsTrue() {
        // Arrange
        val validator = NotEmptyValidator<List<String>>()
        val emptyList = emptyList<String>()

        // Act
        val result = validator.validate(emptyList)

        // Assert
        assertTrue("Empty list should be considered non-empty (not null)", result)
    }

    @Test
    fun validate_withNullList_returnsFalse() {
        // Arrange
        val validator = NotEmptyValidator<List<String>>()

        // Act
        val result = validator.validate(null)

        // Assert
        assertFalse(result, "Null list should be considered empty")
    }
}
