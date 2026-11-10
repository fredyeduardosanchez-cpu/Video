package com.stoiclock.domain

import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.usecase.GetCurrentQuoteUseCase
import com.stoiclock.domain.usecase.SelectNextQuoteUseCase
import com.stoiclock.fakes.FakeQuoteRepository
import com.stoiclock.fakes.FakeSettingsRepository
import com.stoiclock.fakes.quote
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SelectNextQuoteUseCaseTest {

    @Test
    fun `respeta el filtro solo mis frases y guarda la frase actual`() = runTest {
        val quotes = FakeQuoteRepository(listOf(quote(1), quote(2, userCreated = true), quote(3, userCreated = true, include = false)))
        val settings = FakeSettingsRepository(LockScreenSettings(filter = RotationFilter.USER_ONLY))
        val next = SelectNextQuoteUseCase(quotes, settings)()
        assertEquals(2L, next?.id)
        assertEquals(2L, settings.current().currentQuoteId)
    }

    @Test
    fun `sin favoritas recurre a todas las activas`() = runTest {
        val quotes = FakeQuoteRepository(listOf(quote(1), quote(2, include = false)))
        val settings = FakeSettingsRepository(LockScreenSettings(filter = RotationFilter.FAVORITES_ONLY))
        assertEquals(1L, SelectNextQuoteUseCase(quotes, settings)()?.id)
    }

    @Test
    fun `frases excluidas del bloqueo nunca se eligen`() = runTest {
        val quotes = FakeQuoteRepository(listOf(quote(1), quote(2, include = false), quote(3)))
        val settings = FakeSettingsRepository()
        val useCase = SelectNextQuoteUseCase(quotes, settings)
        repeat(50) { assertTrue(useCase()?.id != 2L) }
    }

    @Test
    fun `si la frase actual fue eliminada elige otra`() = runTest {
        val quotes = FakeQuoteRepository(listOf(quote(1), quote(5, userCreated = true)))
        val settings = FakeSettingsRepository(LockScreenSettings(currentQuoteId = 5))
        quotes.deleteUserQuote(5)
        val select = SelectNextQuoteUseCase(quotes, settings)
        assertEquals(1L, GetCurrentQuoteUseCase(quotes, settings, select)()?.id)
    }
}
