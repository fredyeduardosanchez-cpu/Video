package com.stoiclock.domain.usecase

import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.repository.QuoteRepository
import com.stoiclock.domain.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Flujo reactivo de la frase actualmente mostrada en bloqueo (o null si aún no hay). */
class ObserveCurrentQuoteUseCase @Inject constructor(
    private val quotes: QuoteRepository,
    private val settings: SettingsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Quote?> = settings.settings
        .map { it.currentQuoteId }
        .distinctUntilChanged()
        .flatMapLatest { id -> if (id == null) flowOf(null) else quotes.observeQuote(id) }
}
