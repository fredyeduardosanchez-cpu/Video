package com.stoiclock.domain

import com.stoiclock.domain.model.StoicAuthors
import com.stoiclock.domain.usecase.LibraryCategory
import com.stoiclock.domain.usecase.LibraryFilter
import com.stoiclock.fakes.quote
import org.junit.Assert.assertEquals
import org.junit.Test

class LibraryFilterTest {

    private val quotes = listOf(
        quote(1, StoicAuthors.MARCUS_AURELIUS, "El alma se tiñe del color de sus pensamientos.", work = "Meditaciones"),
        quote(2, StoicAuthors.SENECA, "Mientras se aplaza, la vida pasa.", favorite = true),
        quote(3, StoicAuthors.EPICTETUS, "De las cosas existentes, unas dependen de nosotros."),
        quote(4, "Propia", "Respira antes de responder.", userCreated = true),
    )

    @Test
    fun `filtra por categoria`() {
        assertEquals(4, LibraryFilter.apply(quotes, LibraryCategory.ALL, "").size)
        assertEquals(listOf(4L), LibraryFilter.apply(quotes, LibraryCategory.MINE, "").map { it.id })
        assertEquals(listOf(2L), LibraryFilter.apply(quotes, LibraryCategory.FAVORITES, "").map { it.id })
        assertEquals(listOf(1L), LibraryFilter.apply(quotes, LibraryCategory.MARCUS, "").map { it.id })
        assertEquals(listOf(2L), LibraryFilter.apply(quotes, LibraryCategory.SENECA, "").map { it.id })
        assertEquals(listOf(3L), LibraryFilter.apply(quotes, LibraryCategory.EPICTETUS, "").map { it.id })
    }

    @Test
    fun `busca sin distinguir mayusculas ni tildes en texto autor y obra`() {
        assertEquals(listOf(2L), LibraryFilter.apply(quotes, LibraryCategory.ALL, "seneca").map { it.id })
        assertEquals(listOf(1L), LibraryFilter.apply(quotes, LibraryCategory.ALL, "MEDITACIONES").map { it.id })
        assertEquals(listOf(4L), LibraryFilter.apply(quotes, LibraryCategory.ALL, "respira").map { it.id })
    }

    @Test
    fun `combina categoria y busqueda`() {
        assertEquals(emptyList<Long>(), LibraryFilter.apply(quotes, LibraryCategory.MINE, "seneca").map { it.id })
    }
}
