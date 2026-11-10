package com.stoiclock.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.stoiclock.domain.usecase.QuoteValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteEditorSheet(
    state: QuoteEditorState,
    onTextChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onWorkChange: (String) -> Unit,
    onIncludeChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val errors = state.validation.takeIf { state.showErrors }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                if (state.isEditing) "Editar frase" else "Nueva frase",
                style = MaterialTheme.typography.headlineSmall,
            )

            OutlinedTextField(
                value = state.text,
                onValueChange = onTextChange,
                label = { Text("Frase *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 8,
                isError = errors?.textError != null,
                supportingText = {
                    Row {
                        Text(errors?.textError.orEmpty(), modifier = Modifier.weight(1f))
                        Text("${state.text.trim().length}/${QuoteValidator.MAX_TEXT}")
                    }
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            )

            OutlinedTextField(
                value = state.author,
                onValueChange = onAuthorChange,
                label = { Text("Autor") },
                placeholder = { Text("Propia") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errors?.authorError != null,
                supportingText = errors?.authorError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
            )

            OutlinedTextField(
                value = state.work,
                onValueChange = onWorkChange,
                label = { Text("Obra o categoría (opcional)") },
                placeholder = { Text("Nota personal") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = errors?.workError != null,
                supportingText = errors?.workError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("¿Mostrar en pantalla de bloqueo?", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Incluir esta frase en la rotación",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(checked = state.includeInLockScreen, onCheckedChange = onIncludeChange)
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = onSave, enabled = !state.isSaving) {
                    Text(if (state.isEditing) "Guardar cambios" else "Agregar")
                }
            }
        }
    }
}
