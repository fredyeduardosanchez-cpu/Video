package com.stoiclock.domain.usecase

data class QuoteValidationResult(
    val textError: String? = null,
    val authorError: String? = null,
    val workError: String? = null,
) {
    val isValid: Boolean get() = textError == null && authorError == null && workError == null
}

/** Validación del formulario de frases del usuario. */
object QuoteValidator {
    const val MAX_TEXT = 400
    const val MIN_TEXT = 3
    const val MAX_AUTHOR = 60
    const val MAX_WORK = 80

    fun validate(text: String, author: String, work: String): QuoteValidationResult {
        val t = text.trim()
        val a = author.trim()
        val w = work.trim()
        return QuoteValidationResult(
            textError = when {
                t.isEmpty() -> "La frase es obligatoria"
                t.length < MIN_TEXT -> "La frase es demasiado corta"
                t.length > MAX_TEXT -> "Máximo $MAX_TEXT caracteres"
                else -> null
            },
            authorError = when {
                a.isEmpty() -> "Indica un autor (o «Propia»)"
                a.length > MAX_AUTHOR -> "Máximo $MAX_AUTHOR caracteres"
                else -> null
            },
            workError = if (w.length > MAX_WORK) "Máximo $MAX_WORK caracteres" else null,
        )
    }
}
