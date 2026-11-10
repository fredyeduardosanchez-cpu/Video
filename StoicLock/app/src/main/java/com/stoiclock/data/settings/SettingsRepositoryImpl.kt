package com.stoiclock.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.stoiclock.domain.model.BackgroundStyle
import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.QuoteFontSize
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.model.RotationFrequency
import com.stoiclock.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    private object Keys {
        val WALLPAPER = booleanPreferencesKey("wallpaper_enabled")
        val NOTIFICATION = booleanPreferencesKey("notification_enabled")
        val FREQUENCY = stringPreferencesKey("rotation_frequency")
        val FILTER = stringPreferencesKey("rotation_filter")
        val BACKGROUND = stringPreferencesKey("background_style")
        val FONT_SIZE = stringPreferencesKey("font_size")
        val CURRENT_QUOTE = longPreferencesKey("current_quote_id")
    }

    override val settings: Flow<LockScreenSettings> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it.toSettings() }

    override suspend fun current(): LockScreenSettings = settings.first()

    override suspend fun setWallpaperEnabled(enabled: Boolean) = edit { it[Keys.WALLPAPER] = enabled }
    override suspend fun setNotificationEnabled(enabled: Boolean) = edit { it[Keys.NOTIFICATION] = enabled }
    override suspend fun setFrequency(frequency: RotationFrequency) = edit { it[Keys.FREQUENCY] = frequency.name }
    override suspend fun setFilter(filter: RotationFilter) = edit { it[Keys.FILTER] = filter.name }
    override suspend fun setBackgroundStyle(style: BackgroundStyle) = edit { it[Keys.BACKGROUND] = style.name }
    override suspend fun setFontSize(size: QuoteFontSize) = edit { it[Keys.FONT_SIZE] = size.name }
    override suspend fun setCurrentQuoteId(id: Long?) = edit {
        if (id == null) it.remove(Keys.CURRENT_QUOTE) else it[Keys.CURRENT_QUOTE] = id
    }

    private suspend fun edit(block: (MutablePreferences) -> Unit) {
        dataStore.edit { block(it) }
    }

    private fun Preferences.toSettings() = LockScreenSettings(
        wallpaperEnabled = this[Keys.WALLPAPER] ?: false,
        notificationEnabled = this[Keys.NOTIFICATION] ?: false,
        frequency = enumOrDefault(this[Keys.FREQUENCY], RotationFrequency.DAILY),
        filter = enumOrDefault(this[Keys.FILTER], RotationFilter.ALL_ACTIVE),
        backgroundStyle = enumOrDefault(this[Keys.BACKGROUND], BackgroundStyle.OBSIDIAN),
        fontSize = enumOrDefault(this[Keys.FONT_SIZE], QuoteFontSize.MEDIUM),
        currentQuoteId = this[Keys.CURRENT_QUOTE],
    )

    private inline fun <reified T : Enum<T>> enumOrDefault(name: String?, default: T): T =
        name?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: default
}
