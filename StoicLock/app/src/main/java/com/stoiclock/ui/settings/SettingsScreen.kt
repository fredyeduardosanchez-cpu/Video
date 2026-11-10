package com.stoiclock.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stoiclock.domain.model.BackgroundStyle
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.QuoteFontSize
import com.stoiclock.domain.model.RotationFilter
import com.stoiclock.domain.model.RotationFrequency
import com.stoiclock.domain.model.StoicAuthors
import com.stoiclock.ui.components.EditorialQuote
import com.stoiclock.ui.components.SectionTitle
import com.stoiclock.ui.components.brush

private val PreviewQuote = Quote(
    text = "No son las cosas las que nos perturban, sino las opiniones que tenemos sobre ellas.",
    author = StoicAuthors.EPICTETUS,
    work = "Enquiridión, 5",
)

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) viewModel.setNotificationEnabled(true) else viewModel.onNotificationPermissionDenied()
    }

    SettingsScreen(
        state = state,
        onWallpaperToggle = { viewModel.setWallpaperEnabled(it) },
        onNotificationToggle = { enabled ->
            val needsPermission = enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            if (needsPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                viewModel.setNotificationEnabled(enabled)
            }
        },
        onFrequencySelected = { viewModel.setFrequency(it) },
        onFilterSelected = { viewModel.setFilter(it) },
        onStyleSelected = { viewModel.setBackgroundStyle(it) },
        onFontSizeSelected = { viewModel.setFontSize(it) },
        onMessageShown = viewModel::onMessageShown,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onWallpaperToggle: (Boolean) -> Unit,
    onNotificationToggle: (Boolean) -> Unit,
    onFrequencySelected: (RotationFrequency) -> Unit,
    onFilterSelected: (RotationFilter) -> Unit,
    onStyleSelected: (BackgroundStyle) -> Unit,
    onFontSizeSelected: (QuoteFontSize) -> Unit,
    onMessageShown: () -> Unit,
) {
    val settings = state.settings
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbar.showSnackbar(it)
            onMessageShown()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }, contentWindowInsets = WindowInsets(0)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Configuración de bloqueo", style = MaterialTheme.typography.headlineMedium)

            SectionTitle("Método de visualización")
            SettingsCard {
                ToggleRow(
                    title = "Fondo de pantalla de bloqueo dinámico (Recomendado)",
                    subtitle = "Dibuja la frase como fondo de la pantalla de bloqueo",
                    checked = settings.wallpaperEnabled,
                    onCheckedChange = onWallpaperToggle,
                )
                if (settings.wallpaperEnabled) {
                    Text(
                        "Color de fondo",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        BackgroundStyle.entries.forEach { style ->
                            StyleSwatch(style, selected = style == settings.backgroundStyle) { onStyleSelected(style) }
                        }
                    }
                    Text(
                        "Tamaño de fuente",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    )
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        QuoteFontSize.entries.forEachIndexed { index, size ->
                            SegmentedButton(
                                selected = size == settings.fontSize,
                                onClick = { onFontSizeSelected(size) },
                                shape = SegmentedButtonDefaults.itemShape(index, QuoteFontSize.entries.size),
                            ) { Text(size.label) }
                        }
                    }
                    Text(
                        "Vista previa",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    )
                    EditorialQuote(
                        quote = PreviewQuote,
                        style = settings.backgroundStyle,
                        fontSize = settings.fontSize,
                        baseTextSize = 15.sp,
                        contentPadding = 20.dp,
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                    )
                }
            }
            SettingsCard {
                ToggleRow(
                    title = "Notificación fija en pantalla de bloqueo",
                    subtitle = "Silenciosa, con botón «Otra frase»" +
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ". Requiere permiso de notificaciones" else "",
                    checked = settings.notificationEnabled,
                    onCheckedChange = onNotificationToggle,
                )
            }

            SectionTitle("Frecuencia de cambio automático")
            SettingsCard {
                RadioGroup(
                    options = RotationFrequency.entries,
                    selected = settings.frequency,
                    label = { it.label },
                    onSelected = onFrequencySelected,
                )
            }

            SectionTitle("Frases a rotar")
            SettingsCard {
                RadioGroup(
                    options = RotationFilter.entries,
                    selected = settings.filter,
                    label = { it.label },
                    onSelected = onFilterSelected,
                )
            }

            Text(
                "StoicLock funciona sin conexión. Si un filtro no tiene frases disponibles se usarán todas las activas.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
    ) {
        Column(Modifier.padding(16.dp)) { content() }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = checked, role = Role.Switch, onClick = { onCheckedChange(!checked) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f).padding(end = 12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun <T> RadioGroup(options: List<T>, selected: T, label: (T) -> String, onSelected: (T) -> Unit) {
    Column(Modifier.selectableGroup()) {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .selectable(selected = option == selected, role = Role.RadioButton, onClick = { onSelected(option) }),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = option == selected, onClick = null)
                Text(label(option), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 12.dp))
            }
        }
    }
}

@Composable
private fun StyleSwatch(style: BackgroundStyle, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(style.brush())
            .border(
                BorderStroke(
                    if (selected) 3.dp else 1.dp,
                    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                ),
                CircleShape,
            )
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { contentDescription = "Fondo ${style.label}" },
        contentAlignment = Alignment.Center,
    ) {
        if (selected) Icon(Icons.Filled.Check, contentDescription = null, tint = Color(style.accentColor))
    }
}
