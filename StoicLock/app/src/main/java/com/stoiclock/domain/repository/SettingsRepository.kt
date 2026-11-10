package com.stoiclock.domain.repository

import com.stoiclock.domain.model.BackgroundStyle
import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.QuoteFontSize
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.model.RotationFrequency
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<LockScreenSettings>
    suspend fun current(): LockScreenSettings
    suspend fun setWallpaperEnabled(enabled: Boolean)
    suspend fun setNotificationEnabled(enabled: Boolean)
    suspend fun setFrequency(frequency: RotationFrequency)
    suspend fun setFilter(filter: RotationFilter)
    suspend fun setBackgroundStyle(style: BackgroundStyle)
    suspend fun setFontSize(size: QuoteFontSize)
    suspend fun setCurrentQuoteId(id: Long?)
}
