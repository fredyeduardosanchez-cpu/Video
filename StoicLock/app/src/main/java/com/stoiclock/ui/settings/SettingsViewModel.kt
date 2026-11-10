package com.stoiclock.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoiclock.domain.model.BackgroundStyle
import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.QuoteFontSize
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.model.RotationFrequency
import com.stoiclock.domain.repository.SettingsRepository
import com.stoiclock.worker.RotationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isLoading: Boolean = true,
    val settings: LockScreenSettings = LockScreenSettings(),
    val userMessage: String? = null,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val scheduler: RotationScheduler,
) : ViewModel() {

    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(repository.settings, message) { settings, msg ->
        SettingsUiState(isLoading = false, settings = settings, userMessage = msg)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setWallpaperEnabled(enabled: Boolean) = viewModelScope.launch {
        repository.setWallpaperEnabled(enabled)
        if (enabled) {
            scheduler.refreshSoon(refreshOnly = true)
            message.value = "El fondo de bloqueo se actualizará en unos segundos"
        }
    }

    fun setNotificationEnabled(enabled: Boolean) = viewModelScope.launch {
        repository.setNotificationEnabled(enabled)
        // Publica o retira la notificación de inmediato.
        scheduler.refreshSoon(refreshOnly = true)
    }

    fun onNotificationPermissionDenied() {
        message.value = "Sin permiso de notificaciones no se puede mostrar la frase en el bloqueo"
    }

    fun setFrequency(frequency: RotationFrequency) = viewModelScope.launch {
        repository.setFrequency(frequency)
        scheduler.schedule(frequency)
    }

    fun setFilter(filter: RotationFilter) = viewModelScope.launch { repository.setFilter(filter) }

    fun setBackgroundStyle(style: BackgroundStyle) = viewModelScope.launch {
        repository.setBackgroundStyle(style)
        if (repository.current().wallpaperEnabled) scheduler.refreshSoon(refreshOnly = true)
    }

    fun setFontSize(size: QuoteFontSize) = viewModelScope.launch {
        repository.setFontSize(size)
        if (repository.current().wallpaperEnabled) scheduler.refreshSoon(refreshOnly = true)
    }

    fun onMessageShown() = message.update { null }
}
