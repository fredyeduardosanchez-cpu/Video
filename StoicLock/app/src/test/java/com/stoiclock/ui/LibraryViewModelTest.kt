package com.stoiclock.ui

import com.stoiclock.domain.usecase.LibraryCategory
import com.stoiclock.fakes.FakeQuoteRepository
import com.stoiclock.fakes.quote
import com.stoiclock.ui.library.LibraryViewModel
import com.stoiclock.ui.library.LibraryViewModel as VM
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LibraryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `no guarda una frase vacia y muestra errores`() = runTest {
        val repo = FakeQuoteRepository(listOf(quote(1)))
        val vm = LibraryViewModel(repo)
        vm.subscribe(this)

        vm.openNewQuote()
        vm.saveEditor()

        val editor = vm.uiState.value.editor
        assertNotNull(editor)
        assertTrue(editor!!.showErrors)
        assertNotNull(editor.validation.textError)
        assertEquals(1, repo.quotes.value.size)
    }

    @Test
    fun `agrega una frase del usuario con autor por defecto Propia`() = runTest {
        val repo = FakeQuoteRepository(listOf(quote(1)))
        val vm = LibraryViewModel(repo)
        vm.subscribe(this)

        vm.openNewQuote()
        vm.onEditorTextChange("  Lo que depende de mí, lo hago bien.  ")
        vm.onEditorIncludeChange(false)
        vm.saveEditor()

        val created = repo.quotes.value.last()
        assertEquals("Lo que depende de mí, lo hago bien.", created.text)
        assertEquals("Propia", created.author)
        assertTrue(created.isUserCreated)
        assertFalse(created.includeInLockScreen)
        assertNull(vm.uiState.value.editor)
        assertEquals("Frase agregada", vm.uiState.value.userMessage)
    }

    @Test
    fun `no permite editar ni eliminar frases de fabrica`() = runTest {
        val factory = quote(1)
        val repo = FakeQuoteRepository(listOf(factory))
        val vm = LibraryViewModel(repo)
        vm.subscribe(this)

        vm.openEditQuote(factory)
        vm.requestDelete(factory)

        assertNull(vm.uiState.value.editor)
        assertNull(vm.uiState.value.pendingDelete)
    }

    @Test
    fun `edita y elimina frases del usuario`() = runTest {
        val mine = quote(9, author = "Propia", text = "Original", userCreated = true)
        val repo = FakeQuoteRepository(listOf(quote(1), mine))
        val vm = LibraryViewModel(repo)
        vm.subscribe(this)

        vm.openEditQuote(mine)
        vm.onEditorTextChange("Editada")
        vm.onEditorWorkChange("Nota personal")
        vm.saveEditor()
        val edited = repo.quotes.value.first { it.id == 9L }
        assertEquals("Editada", edited.text)
        assertEquals("Nota personal", edited.work)

        vm.requestDelete(edited)
        vm.confirmDelete()
        assertTrue(repo.quotes.value.none { it.id == 9L })
    }

    @Test
    fun `filtra por chips y busqueda`() = runTest {
        val repo = FakeQuoteRepository(listOf(quote(1), quote(2, userCreated = true, text = "Mi frase")))
        val vm = LibraryViewModel(repo)
        vm.subscribe(this)

        vm.onCategorySelected(LibraryCategory.MINE)
        assertEquals(listOf(2L), vm.uiState.value.quotes.map { it.id })

        vm.onCategorySelected(LibraryCategory.ALL)
        vm.onQueryChange("frase 1")
        assertEquals(listOf(1L), vm.uiState.value.quotes.map { it.id })
        assertEquals(2, vm.uiState.value.totalCount)
    }

    @Test
    fun `switch de inclusion en bloqueo actualiza el repositorio`() = runTest {
        val repo = FakeQuoteRepository(listOf(quote(1)))
        val vm = LibraryViewModel(repo)
        vm.setIncludeInLockScreen(repo.quotes.value.first(), false)
        assertFalse(repo.quotes.value.first().includeInLockScreen)
    }
}

/** Mantiene una suscripción activa a uiState (SharingStarted.WhileSubscribed) durante el test. */
@OptIn(ExperimentalCoroutinesApi::class)
private fun VM.subscribe(scope: TestScope) {
    scope.backgroundScope.launch(UnconfinedTestDispatcher(scope.testScheduler)) { uiState.collect {} }
}
