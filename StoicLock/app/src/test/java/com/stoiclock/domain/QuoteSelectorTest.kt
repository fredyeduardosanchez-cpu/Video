package com.stoiclock.domain

import com.stoiclock.domain.usecase.QuoteSelector
import com.stoiclock.fakes.quote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.random.Random

class QuoteSelectorTest {

    @Test
    fun `devuelve null sin candidatas`() {
        assertNull(QuoteSelector.pickNext(emptyList(), currentId = null))
    }

    @Test
    fun `nunca repite la frase actual si hay alternativas`() {
        val candidates = listOf(quote(1), quote(2), quote(3))
        repeat(200) { seed ->
            val next = QuoteSelector.pickNext(candidates, currentId = 2, random = Random(seed))
            assertNotEquals(2L, next?.id)
        }
    }

    @Test
    fun `con una sola candidata la devuelve aunque sea la actual`() {
        val next = QuoteSelector.pickNext(listOf(quote(7)), currentId = 7)
        assertEquals(7L, next?.id)
    }
}
