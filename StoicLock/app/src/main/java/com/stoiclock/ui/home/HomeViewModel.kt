package com.stoiclock.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.repository.QuoteRepository
import com.stoiclock.domain.repository.SettingsRepository
import com.stoiclock.domain.usecase.GetCurrentQuoteUseCase
import com.stoiclock.domain.usecase.ObserveCurrentQuoteUseCase
import com.stoiclock.lockscreen.LockScreenUpdateResult
import com.stoiclock.lockscreen.LockScreenUpdater
import com.stoiclock.lockscreen.QuoteShareHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val quote: Quote? = null,
    val settings: LockScreenSettings = LockScreenSettings(),
    val isUpdating: Boolean = false,
    val userMessage: String? = null,
    val shareUri: Uri? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeCurrentQuote: ObserveCurrentQuoteUseCase,
    settingsRepository: SettingsRepository,
    private val getCurrentQuote: GetCurrentQuoteUseCase,
    private val quoteRepository: QuoteRepository,
    private val updater: LockScreenUpdater,
    private val shareHelper: QuoteShareHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(observeCurrentQuote(), settingsRepository.settings) { quote, settings -> quote to settings }
                .collect { (quote, settings) ->
                    _uiState.update { it.copy(isLoading = false, quote = quote, settings = settings) }
                    // Primer arranque o frase actual eliminada: elegir una nueva.
                    if (quote == null) getCurrentQuote()
                }
        }
    }

    /** Vuelve a aplicar la frase actual al fondo de bloqueo y/o la notificación. */
    fun updateLockScreenNow() = runUpdate { updater.refreshCurrent() }

    /** Cambia a otra frase y la aplica inmediatamente. */
    fun nextQuote() = runUpdate { updater.rotate() }

    private fun runUpdate(block: suspend () -> LockScreenUpdateResult) {
        if (_uiState.value.isUpdating) return
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true) }
            val result = runCatching { block() }.getOrNull()
            _uiState.update { it.copy(isUpdating = false, userMessage = result.toMessage(it.settings)) }
        }
    }

    private fun LockScreenUpdateResult?.toMessage(settings: LockScreenSettings): String = when {
        this == null || quote == null -> "No hay frases disponibles para mostrar"
        !settings.anyMechanismEnabled -> "Frase actualizada. Activa el fondo o la notificación en Configuración"
        settings.wallpaperEnabled && !wallpaperApplied -> "No se pudo cambiar el fondo de bloqueo en este dispositivo"
        settings.notificationEnabled && !notificationShown -> "Concede el permiso de notificaciones para verla en el bloqueo"
        else -> "Pantalla de bloqueo actualizada"
    }

    fun toggleFavorite() {
        val quote = _uiState.value.quote ?: return
        viewModelScope.launch { quoteRepository.setFavorite(quote.id, !quote.isFavorite) }
    }

    fun share() {
        val state = _uiState.value
        val quote = state.quote ?: return
        viewModelScope.launch {
            runCatching { shareHelper.createShareImage(quote, state.settings) }
                .onSuccess { uri -> _uiState.update { it.copy(shareUri = uri) } }
                .onFailure { _uiState.update { it.copy(userMessage = "No se pudo generar la imagen") } }
        }
    }

    fun onShareHandled() = _uiState.update { it.copy(shareUri = null) }

    fun onMessageShown() = _uiState.update { it.copy(userMessage = null) }
}
