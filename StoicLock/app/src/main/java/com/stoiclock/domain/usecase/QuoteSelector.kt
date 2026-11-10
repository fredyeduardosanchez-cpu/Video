package com.stoiclock.domain.usecase

import com.stoiclock.domain.model.Quote
import kotlin.random.Random

/** Lógica pura para elegir la siguiente frase de la rotación. */
object QuoteSelector {
    /**
     * Elige una frase al azar entre [candidates] evitando repetir [currentId] cuando hay alternativas.
     * Devuelve null si no hay candidatas.
     */
    fun pickNext(candidates: List<Quote>, currentId: Long?, random: Random = Random.Default): Quote? {
        if (candidates.isEmpty()) return null
        val pool = candidates.filter { it.id != currentId }.ifEmpty { candidates }
        return pool[random.nextInt(pool.size)]
    }
}
