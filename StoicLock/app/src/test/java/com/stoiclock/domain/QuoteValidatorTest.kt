package com.stoiclock.domain

import com.stoiclock.domain.usecase.QuoteValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuoteValidatorTest {

    @Test
    fun `frase valida con autor por defecto y sin obra`() {
        assertTrue(QuoteValidator.validate("Haz hoy lo que dependa de ti.", "Propia", "").isValid)
    }

    @Test
    fun `frase vacia o solo espacios es invalida`() {
        val result = QuoteValidator.validate("   ", "Propia", "")
        assertFalse(result.isValid)
        assertNotNull(result.textError)
    }

    @Test
    fun `autor vacio es invalido`() {
        val result = QuoteValidator.validate("Una frase válida", " ", "")
        assertNotNull(result.authorError)
        assertNull(result.textError)
    }

    @Test
    fun `respeta los limites de longitud`() {
        val long = "a".repeat(QuoteValidator.MAX_TEXT + 1)
        assertNotNull(QuoteValidator.validate(long, "Propia", "").textError)
        assertNotNull(QuoteValidator.validate("Frase", "Propia", "o".repeat(QuoteValidator.MAX_WORK + 1)).workError)
    }
}
