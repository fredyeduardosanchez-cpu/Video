package com.stoiclock.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.StoicAuthors
import com.stoiclock.domain.repository.QuoteRepository
import com.stoiclock.domain.usecase.LibraryCategory
import com.stoiclock.domain.usecase.LibraryFilter
import com.stoiclock.domain.usecase.QuoteValidationResult
import com.stoiclock.domain.usecase.QuoteValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Estado del formulario de crear/editar frase. [editingId] == null => nueva frase. */
data class QuoteEditorState(
    val editingId: Long? = null,
    val text: String = "",
    val author: String = StoicAuthors.OWN,
    val work: String = "",
    val includeInLockScreen: Boolean = true,
    val validation: QuoteValidationResult = QuoteValidationResult(),
    val showErrors: Boolean = false,
    val isSaving: Boolean = false,
) {
    val isEditing: Boolean get() = editingId != null
}

data class LibraryUiState(
    val isLoading: Boolean = true,
    val quotes: List<Quote> = emptyList(),
    val totalCount: Int = 0,
    val category: LibraryCategory = LibraryCategory.ALL,
    val query: String = "",
    val editor: QuoteEditorState? = null,
    val pendingDelete: Quote? = null,
    val userMessage: String? = null,
)

/** Estado local de la pantalla (lo que no viene de la base de datos). */
private data class LibraryLocalState(
    val category: LibraryCategory = LibraryCategory.ALL,
    val query: String = "",
    val editor: QuoteEditorState? = null,
    val pendingDelete: Quote? = null,
    val userMessage: String? = null,
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: QuoteRepository,
) : ViewModel() {

    private val local = MutableStateFlow(LibraryLocalState())

    val uiState: StateFlow<LibraryUiState> = combine(repository.observeQuotes(), local) { all, l ->
        LibraryUiState(
            isLoading = false,
            quotes = LibraryFilter.apply(all, l.category, l.query),
            totalCount = all.size,
            category = l.category,
            query = l.query,
            editor = l.editor,
            pendingDelete = l.pendingDelete,
            userMessage = l.userMessage,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LibraryUiState())

    // --- Filtros ---
    fun onCategorySelected(category: LibraryCategory) = local.update { it.copy(category = category) }
    fun onQueryChange(query: String) = local.update { it.copy(query = query) }

    // --- Acciones rápidas en tarjetas ---
    fun toggleFavorite(quote: Quote) = viewModelScope.launch {
        repository.setFavorite(quote.id, !quote.isFavorite)
    }

    fun setIncludeInLockScreen(quote: Quote, include: Boolean) = viewModelScope.launch {
        repository.setIncludeInLockScreen(quote.id, include)
    }

    // --- Formulario ---
    fun openNewQuote() = local.update { it.copy(editor = QuoteEditorState()) }

    fun openEditQuote(quote: Quote) {
        if (!quote.isUserCreated) return
        local.update {
            it.copy(
                editor = QuoteEditorState(
                    editingId = quote.id,
                    text = quote.text,
                    author = quote.author,
                    work = quote.work.orEmpty(),
                    includeInLockScreen = quote.includeInLockScreen,
                )
            )
        }
    }

    fun onEditorTextChange(value: String) = updateEditor { it.copy(text = value) }
    fun onEditorAuthorChange(value: String) = updateEditor { it.copy(author = value) }
    fun onEditorWorkChange(value: String) = updateEditor { it.copy(work = value) }
    fun onEditorIncludeChange(value: Boolean) = updateEditor { it.copy(includeInLockScreen = value) }

    fun dismissEditor() = local.update { it.copy(editor = null) }

    fun saveEditor() {
        val editor = local.value.editor ?: return
        if (editor.isSaving) return
        val validation = QuoteValidator.validate(editor.text, editor.author, editor.work)
        if (!validation.isValid) {
            updateEditor { it.copy(validation = validation, showErrors = true) }
            return
        }
        updateEditor { it.copy(isSaving = true) }
        viewModelScope.launch {
            val work = editor.work.trim().ifEmpty { null }
            val message = runCatching {
                val id = editor.editingId
                if (id == null) {
                    repository.addUserQuote(editor.text, editor.author, work, editor.includeInLockScreen)
                    "Frase agregada"
                } else {
                    val original = repository.getQuote(id) ?: error("Frase no encontrada")
                    repository.updateUserQuote(
                        original.copy(
                            text = editor.text,
                            author = editor.author,
                            work = work,
                            includeInLockScreen = editor.includeInLockScreen,
                        )
                    )
                    "Frase actualizada"
                }
            }.getOrElse { "No se pudo guardar la frase" }
            local.update { it.copy(editor = null, userMessage = message) }
        }
    }

    private fun updateEditor(transform: (QuoteEditorState) -> QuoteEditorState) = local.update { state ->
        val editor = state.editor ?: return@update state
        val updated = transform(editor)
        // Revalida en vivo sólo después del primer intento de guardar.
        val validated = if (updated.showErrors) {
            updated.copy(validation = QuoteValidator.validate(updated.text, updated.author, updated.work))
        } else updated
        state.copy(editor = validated)
    }

    // --- Eliminación ---
    fun requestDelete(quote: Quote) {
        if (quote.isUserCreated) local.update { it.copy(pendingDelete = quote) }
    }

    fun cancelDelete() = local.update { it.copy(pendingDelete = null) }

    fun confirmDelete() {
        val quote = local.value.pendingDelete ?: return
        local.update { it.copy(pendingDelete = null) }
        viewModelScope.launch {
            repository.deleteUserQuote(quote.id)
            local.update { it.copy(userMessage = "Frase eliminada") }
        }
    }

    fun onMessageShown() = local.update { it.copy(userMessage = null) }
}
