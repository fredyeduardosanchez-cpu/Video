package com.stoiclock.domain.repository

import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.RotationFilter
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun observeQuotes(): Flow<List<Quote>>
    fun observeQuote(id: Long): Flow<Quote?>
    suspend fun getQuote(id: Long): Quote?
    suspend fun getRotationCandidates(filter: RotationFilter): List<Quote>
    suspend fun getAnyQuote(): Quote?

    /** Inserta una frase del usuario y devuelve su id. */
    suspend fun addUserQuote(text: String, author: String, work: String?, includeInLockScreen: Boolean): Long

    /** Actualiza una frase del usuario. Las frases de fábrica no se editan. */
    suspend fun updateUserQuote(quote: Quote)

    /** Elimina una frase del usuario. Las frases de fábrica no se eliminan. */
    suspend fun deleteUserQuote(id: Long)

    suspend fun setFavorite(id: Long, favorite: Boolean)
    suspend fun setIncludeInLockScreen(id: Long, include: Boolean)
}
