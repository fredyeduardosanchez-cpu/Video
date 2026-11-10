package com.stoiclock.lockscreen

import android.content.Context
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.repository.SettingsRepository
import com.stoiclock.domain.usecase.GetCurrentQuoteUseCase
import com.stoiclock.domain.usecase.SelectNextQuoteUseCase
import com.stoiclock.widget.StoicGlanceWidget
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

data class LockScreenUpdateResult(
    val quote: Quote?,
    val wallpaperApplied: Boolean,
    val notificationShown: Boolean,
)

/**
 * Orquestador del motor de bloqueo: elige la frase y la aplica a todos los mecanismos activos
 * (fondo FLAG_LOCK, notificación pública y widget Glance).
 */
@Singleton
class LockScreenUpdater @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val selectNextQuote: SelectNextQuoteUseCase,
    private val getCurrentQuote: GetCurrentQuoteUseCase,
    private val wallpaperGenerator: LockScreenWallpaperGenerator,
    private val notificationManager: QuoteNotificationManager,
) {
    private val mutex = Mutex()

    /** Cambia a una nueva frase y la aplica. */
    suspend fun rotate(): LockScreenUpdateResult = mutex.withLock { applyQuote(selectNextQuote()) }

    /** Vuelve a aplicar la frase actual (p. ej. tras cambiar el estilo o activar un mecanismo). */
    suspend fun refreshCurrent(): LockScreenUpdateResult = mutex.withLock { applyQuote(getCurrentQuote()) }

    private suspend fun applyQuote(quote: Quote?): LockScreenUpdateResult {
        val settings = settingsRepository.current()
        var wallpaper = false
        var notification = false
        if (quote != null) {
            if (settings.wallpaperEnabled) wallpaper = wallpaperGenerator.apply(quote, settings)
            if (settings.notificationEnabled) notification = notificationManager.show(quote)
        }
        if (!settings.notificationEnabled) notificationManager.cancel()
        runCatching { StoicGlanceWidget().updateAll(context) }
        return LockScreenUpdateResult(quote, wallpaper, notification)
    }
}
