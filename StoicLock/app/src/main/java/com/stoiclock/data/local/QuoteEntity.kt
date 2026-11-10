package com.stoiclock.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.stoiclock.domain.model.Quote

@Entity(
    tableName = QuoteEntity.TABLE,
    indices = [Index("author"), Index("isUserCreated"), Index("includeInLockScreen")],
)
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val author: String,
    val work: String?,
    val isUserCreated: Boolean,
    val isFavorite: Boolean = false,
    @ColumnInfo(defaultValue = "1") val includeInLockScreen: Boolean = true,
    val createdAt: Long,
) {
    companion object {
        const val TABLE = "quotes"
    }
}

fun QuoteEntity.toDomain() = Quote(
    id = id,
    text = text,
    author = author,
    work = work,
    isUserCreated = isUserCreated,
    isFavorite = isFavorite,
    includeInLockScreen = includeInLockScreen,
    createdAt = createdAt,
)

fun Quote.toEntity() = QuoteEntity(
    id = id,
    text = text,
    author = author,
    work = work,
    isUserCreated = isUserCreated,
    isFavorite = isFavorite,
    includeInLockScreen = includeInLockScreen,
    createdAt = createdAt,
)
