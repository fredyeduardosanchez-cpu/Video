package com.stoiclock.domain.usecase

import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.StoicAuthors
import java.text.Normalizer

/** Filtros de la Biblioteca (chips). */
enum class LibraryCategory(val label: String) {
    ALL("Todas"),
    MINE("Mis Frases"),
    FAVORITES("Favoritas"),
    MARCUS("Marco Aurelio"),
    SENECA("Séneca"),
    EPICTETUS("Epicteto"),
}

object LibraryFilter {
    fun apply(quotes: List<Quote>, category: LibraryCategory, query: String): List<Quote> {
        val q = query.normalized()
        return quotes.filter { quote ->
            val matchesCategory = when (category) {
                LibraryCategory.ALL -> true
                LibraryCategory.MINE -> quote.isUserCreated
                LibraryCategory.FAVORITES -> quote.isFavorite
                LibraryCategory.MARCUS -> quote.author == StoicAuthors.MARCUS_AURELIUS
                LibraryCategory.SENECA -> quote.author == StoicAuthors.SENECA
                LibraryCategory.EPICTETUS -> quote.author == StoicAuthors.EPICTETUS
            }
            matchesCategory && (q.isEmpty() ||
                quote.text.normalized().contains(q) ||
                quote.author.normalized().contains(q) ||
                (quote.work?.normalized()?.contains(q) == true))
        }
    }

    /** Minúsculas y sin tildes, para que "seneca" encuentre "Séneca". */
    private fun String.normalized(): String =
        Normalizer.normalize(trim().lowercase(), Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
}
