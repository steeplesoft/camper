package com.steeplesoft.camper.validators

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.DefaultAsserter.assertTrue


class EmailValidatorTest {

    @Test
    fun validate_withValidBasicEmail_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test@example.com")

        // Assert
        assertTrue("Valid basic email should pass validation", result)
    }

    @Test
    fun validate_withValidEmailWithSubdomain_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("user@mail.example.com")

        // Assert
        assertTrue("Valid email with subdomain should pass validation", result)
    }

    @Test
    fun validate_withValidEmailWithNumbers_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("user123@example123.com")

        // Assert
        assertTrue("Valid email with numbers should pass validation", result)
    }

    @Test
    fun validate_withValidEmailWithSpecialCharacters_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test.user+tag@example.com")

        // Assert
        assertTrue("Valid email with special characters should pass validation", result)
    }

    @Test
    fun validate_withValidEmailWithDashes_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("user@test-domain.co.uk")

        // Assert
        assertTrue("Valid email with dashes in domain should pass validation", result)
    }

    @Test
    fun validate_withUppercaseAddress_returnsTrue() {
        assertTrue("Uppercase email addresses should pass validation", EmailValidator().validate("User@Example.COM"))
    }

    @Test
    fun validate_withValidEmailWithUnderscores_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test_user@example.com")

        // Assert
        assertTrue("Valid email with underscores in local part should pass validation", result)
    }

    @Test
    fun validate_withEmptyString_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("")

        // Assert
        assertFalse(result, "Empty string should fail email validation")
    }

    @Test
    fun validate_withNullValue_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate(null)

        // Assert
        assertFalse(result, "Null value should fail email validation")
    }

    @Test
    fun validate_withInvalidEmailMissingAtSymbol_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("testexample.com")

        // Assert
        assertFalse(result, "Email missing @ symbol should fail validation")
    }

    @Test
    fun validate_withInvalidEmailMissingDomain_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test@")

        // Assert
        assertFalse(result, "Email missing domain should fail validation")
    }

    @Test
    fun validate_withInvalidEmailMissingLocalPart_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("@example.com")

        // Assert
        assertFalse(result, "Email missing local part should fail validation")
    }

    @Test
    fun validate_withInvalidEmailMultipleAtSymbols_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test@example@com")

        // Assert
        assertFalse(result, "Email with multiple @ symbols should fail validation")
    }

    @Test
    fun validate_withInvalidEmailSpaces_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test user@example.com")

        // Assert
        assertFalse(result, "Email with spaces should fail validation")
    }

    @Test
    fun validate_withInvalidEmailNoTLD_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test@example")

        // Assert
        assertFalse(result, "Email without TLD should fail validation")
    }

    @Test
    fun validate_withInvalidEmailStartingWithDot_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate(".test@example.com")

        // Assert
        assertFalse(result, "Email starting with dot should fail validation")
    }

    @Test
    fun validate_withInvalidEmailEndingWithDot_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test.@example.com")

        // Assert
        assertFalse(result, "Email ending with dot should fail validation")
    }

    @Test
    fun validate_withInvalidEmailConsecutiveDots_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test..user@example.com")

        // Assert
        assertFalse(result, "Email with consecutive dots should fail validation")
    }

    @Test
    fun validate_withInvalidEmailSpecialCharactersInDomain_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test@exam!ple.com")

        // Assert
        assertFalse(result, "Email with special characters in domain should fail validation")
    }

    @Test
    fun validate_withPlainText_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("plaintext")

        // Assert
        assertFalse(result, "Plain text should fail email validation")
    }

    @Test
    fun validate_withWhitespaceOnly_returnsFalse() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("   ")

        // Assert
        assertFalse(result, "Whitespace only should fail email validation")
    }

    @Test
    fun errorText_withDefaultMessage_returnsExpectedText() {
        // Arrange
        val validator = EmailValidator()

        // Act & Assert
        assertEquals(
            "Please enter a valid e-mail address.",
            validator.errorText,
            "Default error message should match expected text"
        )
    }

    @Test
    fun errorText_withCustomMessage_returnsCustomText() {
        // Arrange
        val customMessage = "Invalid email format"
        val validator = EmailValidator(customMessage)

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
        val validator = EmailValidator(null)

        // Act & Assert
        assertEquals(
            "Please enter a valid e-mail address.",
            validator.errorText,
            "Null custom message should fallback to default"
        )
    }

    @Test
    fun validate_withValidQuotedEmail_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("\"test.email.with+symbol\"@example.com")

        // Assert
        assertTrue("Valid quoted email should pass validation", result)
    }

    @Test
    fun validate_withValidIPAddressInBrackets_returnsTrue() {
        // Arrange
        val validator = EmailValidator()

        // Act
        val result = validator.validate("test@[192.168.1.1]")

        // Assert
        assertTrue("Valid email with IP address should pass validation", result)
    }
}
