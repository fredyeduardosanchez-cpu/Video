package com.stoiclock.data

import com.stoiclock.data.local.SeedQuotes
import com.stoiclock.domain.model.StoicAuthors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeedQuotesTest {

    @Test
    fun `contiene 30 frases 10 por autor`() {
        assertEquals(30, SeedQuotes.all.size)
        val byAuthor = SeedQuotes.all.groupingBy { it.author }.eachCount()
        assertEquals(10, byAuthor[StoicAuthors.MARCUS_AURELIUS])
        assertEquals(10, byAuthor[StoicAuthors.SENECA])
        assertEquals(10, byAuthor[StoicAuthors.EPICTETUS])
    }

    @Test
    fun `todas tienen obra y no hay duplicadas`() {
        assertTrue(SeedQuotes.all.all { it.work.isNotBlank() && it.text.isNotBlank() })
        assertEquals(SeedQuotes.all.size, SeedQuotes.all.map { it.text }.toSet().size)
    }
}
