package com.stoiclock.domain.usecase

import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.repository.QuoteRepository
import com.stoiclock.domain.repository.SettingsRepository
import javax.inject.Inject

/** Devuelve la frase actual; si no hay ninguna (o fue eliminada) elige una nueva. */
class GetCurrentQuoteUseCase @Inject constructor(
    private val quotes: QuoteRepository,
    private val settings: SettingsRepository,
    private val selectNext: SelectNextQuoteUseCase,
) {
    suspend operator fun invoke(): Quote? {
        val id = settings.current().currentQuoteId
        return id?.let { quotes.getQuote(it) } ?: selectNext()
    }
}
