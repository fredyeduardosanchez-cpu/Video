package com.stoiclock.fakes

import com.stoiclock.domain.model.BackgroundStyle
import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.QuoteFontSize
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.model.RotationFrequency
import com.stoiclock.domain.repository.QuoteRepository
import com.stoiclock.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeQuoteRepository(initial: List<Quote> = emptyList()) : QuoteRepository {
    val quotes = MutableStateFlow(initial)
    private var nextId = (initial.maxOfOrNull { it.id } ?: 0) + 1

    override fun observeQuotes(): Flow<List<Quote>> = quotes
    override fun observeQuote(id: Long): Flow<Quote?> = quotes.map { list -> list.firstOrNull { it.id == id } }
    override suspend fun getQuote(id: Long): Quote? = quotes.value.firstOrNull { it.id == id }
    override suspend fun getRotationCandidates(filter: RotationFilter): List<Quote> = quotes.value.filter {
        it.includeInLockScreen && when (filter) {
            RotationFilter.ALL_ACTIVE -> true
            RotationFilter.USER_ONLY -> it.isUserCreated
            RotationFilter.FAVORITES_ONLY -> it.isFavorite
        }
    }
    override suspend fun getAnyQuote(): Quote? = quotes.value.firstOrNull()

    override suspend fun addUserQuote(text: String, author: String, work: String?, includeInLockScreen: Boolean): Long {
        val id = nextId++
        quotes.update {
            it + Quote(id, text.trim(), author.trim(), work, isUserCreated = true, includeInLockScreen = includeInLockScreen, createdAt = id)
        }
        return id
    }

    override suspend fun updateUserQuote(quote: Quote) = quotes.update { list ->
        list.map { if (it.id == quote.id && it.isUserCreated) quote else it }
    }

    override suspend fun deleteUserQuote(id: Long) = quotes.update { list -> list.filterNot { it.id == id && it.isUserCreated } }
    override suspend fun setFavorite(id: Long, favorite: Boolean) = quotes.update { list ->
        list.map { if (it.id == id) it.copy(isFavorite = favorite) else it }
    }
    override suspend fun setIncludeInLockScreen(id: Long, include: Boolean) = quotes.update { list ->
        list.map { if (it.id == id) it.copy(includeInLockScreen = include) else it }
    }
}

class FakeSettingsRepository(initial: LockScreenSettings = LockScreenSettings()) : SettingsRepository {
    private val state = MutableStateFlow(initial)
    override val settings: Flow<LockScreenSettings> = state
    override suspend fun current(): LockScreenSettings = state.value
    override suspend fun setWallpaperEnabled(enabled: Boolean) = state.update { it.copy(wallpaperEnabled = enabled) }
    override suspend fun setNotificationEnabled(enabled: Boolean) = state.update { it.copy(notificationEnabled = enabled) }
    override suspend fun setFrequency(frequency: RotationFrequency) = state.update { it.copy(frequency = frequency) }
    override suspend fun setFilter(filter: RotationFilter) = state.update { it.copy(filter = filter) }
    override suspend fun setBackgroundStyle(style: BackgroundStyle) = state.update { it.copy(backgroundStyle = style) }
    override suspend fun setFontSize(size: QuoteFontSize) = state.update { it.copy(fontSize = size) }
    override suspend fun setCurrentQuoteId(id: Long?) = state.update { it.copy(currentQuoteId = id) }
}

fun quote(
    id: Long,
    author: String = "Séneca",
    text: String = "Frase $id",
    userCreated: Boolean = false,
    favorite: Boolean = false,
    include: Boolean = true,
    work: String? = null,
) = Quote(id, text, author, work, userCreated, favorite, include, createdAt = id)
