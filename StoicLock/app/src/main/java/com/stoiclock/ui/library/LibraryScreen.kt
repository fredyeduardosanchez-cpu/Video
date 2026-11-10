package com.stoiclock.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.usecase.LibraryCategory
import com.stoiclock.ui.components.EmptyState

@Composable
fun LibraryRoute(viewModel: LibraryViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LibraryScreen(
        state = state,
        onCategorySelected = viewModel::onCategorySelected,
        onQueryChange = viewModel::onQueryChange,
        onToggleFavorite = { viewModel.toggleFavorite(it) },
        onIncludeChange = { quote, include -> viewModel.setIncludeInLockScreen(quote, include) },
        onAdd = viewModel::openNewQuote,
        onEdit = viewModel::openEditQuote,
        onDelete = viewModel::requestDelete,
        onMessageShown = viewModel::onMessageShown,
    )

    state.editor?.let { editor ->
        QuoteEditorSheet(
            state = editor,
            onTextChange = viewModel::onEditorTextChange,
            onAuthorChange = viewModel::onEditorAuthorChange,
            onWorkChange = viewModel::onEditorWorkChange,
            onIncludeChange = viewModel::onEditorIncludeChange,
            onSave = viewModel::saveEditor,
            onDismiss = viewModel::dismissEditor,
        )
    }

    state.pendingDelete?.let { quote ->
        AlertDialog(
            onDismissRequest = viewModel::cancelDelete,
            title = { Text("¿Eliminar frase?") },
            text = { Text("«${quote.text.take(120)}»\n\nEsta acción no se puede deshacer.") },
            confirmButton = { TextButton(onClick = viewModel::confirmDelete) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = viewModel::cancelDelete) { Text("Cancelar") } },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    state: LibraryUiState,
    onCategorySelected: (LibraryCategory) -> Unit,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (Quote) -> Unit,
    onIncludeChange: (Quote, Boolean) -> Unit,
    onAdd: () -> Unit,
    onEdit: (Quote) -> Unit,
    onDelete: (Quote) -> Unit,
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAdd,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Agregar Frase") },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text("Biblioteca", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "${state.totalCount} frases · ${state.quotes.size} visibles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar por texto, autor u obra") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    trailingIcon = {
                        if (state.query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(Icons.Outlined.Clear, contentDescription = "Limpiar búsqueda")
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.extraLarge,
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(LibraryCategory.entries) { category ->
                        FilterChip(
                            selected = state.category == category,
                            onClick = { onCategorySelected(category) },
                            label = { Text(category.label) },
                        )
                    }
                }
            }
            if (!state.isLoading && state.quotes.isEmpty()) {
                item {
                    EmptyState(
                        title = if (state.category == LibraryCategory.MINE && state.query.isBlank())
                            "Aún no tienes frases propias" else "Sin resultados",
                        body = if (state.category == LibraryCategory.MINE && state.query.isBlank())
                            "Toca «Agregar Frase» para escribir la tuya." else "Prueba con otro filtro o búsqueda.",
                    )
                }
            }
            items(state.quotes, key = { it.id }) { quote ->
                QuoteListCard(
                    quote = quote,
                    onToggleFavorite = { onToggleFavorite(quote) },
                    onIncludeChange = { onIncludeChange(quote, it) },
                    onEdit = { onEdit(quote) },
                    onDelete = { onDelete(quote) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun QuoteListCard(
    quote: Quote,
    onToggleFavorite: () -> Unit,
    onIncludeChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
    ) {
        Column(Modifier.padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 8.dp)) {
            Text(
                text = "«${quote.text}»",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(end = 8.dp),
            )
            Spacer(Modifier.padding(top = 8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        quote.author,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    quote.work?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                if (quote.isUserCreated) {
                    AssistChip(onClick = onEdit, label = { Text("Mía") })
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        if (quote.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (quote.isFavorite) "Quitar de favoritas" else "Marcar como favorita",
                        tint = if (quote.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (quote.isUserCreated) {
                    IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, contentDescription = "Editar") }
                    IconButton(onClick = onDelete) { Icon(Icons.Outlined.Delete, contentDescription = "Eliminar") }
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "En bloqueo",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(8.dp))
                Box(Modifier.padding(end = 8.dp)) {
                    Switch(checked = quote.includeInLockScreen, onCheckedChange = onIncludeChange)
                }
            }
        }
    }
}
