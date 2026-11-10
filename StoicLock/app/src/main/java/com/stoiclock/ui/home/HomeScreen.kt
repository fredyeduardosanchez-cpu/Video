package com.stoiclock.ui.home

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stoiclock.ui.components.EditorialQuote
import com.stoiclock.ui.components.EmptyState

@Composable
fun HomeRoute(
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.shareUri) {
        val uri = state.shareUri ?: return@LaunchedEffect
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            state.quote?.let { putExtra(Intent.EXTRA_TEXT, "«${it.text}» — ${it.author}") }
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(send, "Compartir frase"))
        viewModel.onShareHandled()
    }

    HomeScreen(
        state = state,
        onUpdateNow = viewModel::updateLockScreenNow,
        onNextQuote = viewModel::nextQuote,
        onToggleFavorite = viewModel::toggleFavorite,
        onShare = viewModel::share,
        onOpenSettings = onOpenSettings,
        onMessageShown = viewModel::onMessageShown,
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onUpdateNow: () -> Unit,
    onNextQuote: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    onOpenSettings: () -> Unit,
    onMessageShown: () -> Unit,
) {
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbar.showSnackbar(it)
            onMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Hoy", style = MaterialTheme.typography.headlineMedium)
            Text(
                "La frase que acompaña tu pantalla de bloqueo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            when {
                state.isLoading -> Box(Modifier.fillMaxWidth().height(320.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.quote == null -> EmptyState(
                    title = "Sin frases activas",
                    body = "Activa alguna frase en la Biblioteca para mostrarla en el bloqueo.",
                )
                else -> EditorialQuote(
                    quote = state.quote,
                    style = state.settings.backgroundStyle,
                    fontSize = state.settings.fontSize,
                    modifier = Modifier.fillMaxWidth().height(420.dp),
                )
            }

            if (state.quote != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilledTonalIconButton(onClick = onToggleFavorite) {
                        Icon(
                            if (state.quote.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (state.quote.isFavorite) "Quitar de favoritas" else "Marcar como favorita",
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    FilledTonalIconButton(onClick = onShare) {
                        Icon(Icons.Outlined.Share, contentDescription = "Compartir como imagen")
                    }
                    Spacer(Modifier.width(16.dp))
                    FilledTonalIconButton(onClick = onNextQuote, enabled = !state.isUpdating) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Otra frase")
                    }
                }
            }

            Button(
                onClick = onUpdateNow,
                enabled = !state.isUpdating && state.quote != null,
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                if (state.isUpdating) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.Lock, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Actualizar pantalla de bloqueo ahora")
                }
            }

            if (!state.isLoading && !state.settings.anyMechanismEnabled) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Aún no hay nada en tu bloqueo", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Activa el fondo dinámico o la notificación fija.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        OutlinedButton(onClick = onOpenSettings) { Text("Configurar") }
                    }
                }
            }
        }
    }
}
