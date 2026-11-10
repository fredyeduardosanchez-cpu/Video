package com.stoiclock.data.repository

import com.stoiclock.data.local.QuoteDao
import com.stoiclock.data.local.QuoteEntity
import com.stoiclock.data.local.toDomain
import com.stoiclock.data.local.toEntity
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepositoryImpl @Inject constructor(
    private val dao: QuoteDao,
) : QuoteRepository {

    override fun observeQuotes(): Flow<List<Quote>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeQuote(id: Long): Flow<Quote?> = dao.observeById(id).map { it?.toDomain() }

    override suspend fun getQuote(id: Long): Quote? = dao.getById(id)?.toDomain()

    override suspend fun getRotationCandidates(filter: RotationFilter): List<Quote> = when (filter) {
        RotationFilter.ALL_ACTIVE -> dao.getActive()
        RotationFilter.USER_ONLY -> dao.getActiveUserCreated()
        RotationFilter.FAVORITES_ONLY -> dao.getActiveFavorites()
    }.map { it.toDomain() }

    override suspend fun getAnyQuote(): Quote? = dao.getRandom()?.toDomain()

    override suspend fun addUserQuote(
        text: String,
        author: String,
        work: String?,
        includeInLockScreen: Boolean,
    ): Long = dao.insert(
        QuoteEntity(
            text = text.trim(),
            author = author.trim(),
            work = work?.trim()?.takeIf { it.isNotEmpty() },
            isUserCreated = true,
            includeInLockScreen = includeInLockScreen,
            createdAt = System.currentTimeMillis(),
        )
    )

    override suspend fun updateUserQuote(quote: Quote) {
        val existing = dao.getById(quote.id) ?: return
        if (!existing.isUserCreated) return
        dao.update(
            quote.copy(
                text = quote.text.trim(),
                author = quote.author.trim(),
                work = quote.work?.trim()?.takeIf { it.isNotEmpty() },
                isUserCreated = true,
                createdAt = existing.createdAt,
            ).toEntity()
        )
    }

    override suspend fun deleteUserQuote(id: Long) = dao.deleteUserCreated(id)

    override suspend fun setFavorite(id: Long, favorite: Boolean) = dao.setFavorite(id, favorite)

    override suspend fun setIncludeInLockScreen(id: Long, include: Boolean) =
        dao.setIncludeInLockScreen(id, include)
}
