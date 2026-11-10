package com.stoiclock.domain.usecase

import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.repository.QuoteRepository
import com.stoiclock.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Elige la siguiente frase según el filtro de rotación y la guarda como frase actual.
 * Si el filtro no tiene candidatas (p. ej. "Solo favoritas" sin favoritas) recurre a todas las activas
 * y, en último caso, a cualquier frase.
 */
class SelectNextQuoteUseCase @Inject constructor(
    private val quotes: QuoteRepository,
    private val settings: SettingsRepository,
) {
    suspend operator fun invoke(): Quote? {
        val current = settings.current()
        val candidates = quotes.getRotationCandidates(current.filter)
            .ifEmpty { quotes.getRotationCandidates(RotationFilter.ALL_ACTIVE) }
        val next = QuoteSelector.pickNext(candidates, current.currentQuoteId) ?: quotes.getAnyQuote()
        settings.setCurrentQuoteId(next?.id)
        return next
    }
}
