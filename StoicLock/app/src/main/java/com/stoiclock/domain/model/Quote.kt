package com.stoiclock.domain.model

/** Frase estoica (de fábrica o creada por el usuario). Modelo de dominio puro. */
data class Quote(
    val id: Long = 0,
    val text: String,
    val author: String,
    val work: String? = null,
    val isUserCreated: Boolean = false,
    val isFavorite: Boolean = false,
    val includeInLockScreen: Boolean = true,
    val createdAt: Long = 0,
)

object StoicAuthors {
    const val MARCUS_AURELIUS = "Marco Aurelio"
    const val SENECA = "Séneca"
    const val EPICTETUS = "Epicteto"
    const val OWN = "Propia"
}
