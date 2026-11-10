package com.stoiclock.data.local

import com.stoiclock.domain.model.StoicAuthors.EPICTETUS
import com.stoiclock.domain.model.StoicAuthors.MARCUS_AURELIUS
import com.stoiclock.domain.model.StoicAuthors.SENECA

data class SeedQuote(val text: String, val author: String, val work: String)

/** 30 frases estoicas de fábrica (10 por autor), traducidas al español con su obra de origen. */
object SeedQuotes {
    private const val MEDITATIONS = "Meditaciones"
    private const val LETTERS = "Cartas a Lucilio"
    private const val BREVITY = "Sobre la brevedad de la vida"
    private const val ENCHIRIDION = "Enquiridión"
    private const val DISCOURSES = "Disertaciones"

    val all: List<SeedQuote> = listOf(
        // Marco Aurelio — Meditaciones
        SeedQuote("El alma se tiñe del color de sus pensamientos.", MARCUS_AURELIUS, "$MEDITATIONS, V, 16"),
        SeedQuote("Lo que obstaculiza la acción hace avanzar la acción. Lo que se interpone en el camino se convierte en el camino.", MARCUS_AURELIUS, "$MEDITATIONS, V, 20"),
        SeedQuote("No sigas discutiendo sobre cómo debe ser un hombre bueno. Sé uno.", MARCUS_AURELIUS, "$MEDITATIONS, X, 16"),
        SeedQuote("La mejor manera de vengarte es no parecerte a quien te ofendió.", MARCUS_AURELIUS, "$MEDITATIONS, VI, 6"),
        SeedQuote("Si te aflige algo externo, no es eso lo que te perturba, sino tu juicio sobre ello. Y está en tu poder borrar ese juicio ahora mismo.", MARCUS_AURELIUS, "$MEDITATIONS, VIII, 47"),
        SeedQuote("Muy poco se necesita para hacer una vida feliz; todo está dentro de ti, en tu manera de pensar.", MARCUS_AURELIUS, "$MEDITATIONS, VII, 67"),
        SeedQuote("No actúes como si fueras a vivir diez mil años. La muerte pende sobre ti. Mientras vivas, mientras puedas, sé bueno.", MARCUS_AURELIUS, "$MEDITATIONS, IV, 17"),
        SeedQuote("Nada le sucede a nadie que no esté en su naturaleza soportarlo.", MARCUS_AURELIUS, "$MEDITATIONS, V, 18"),
        SeedQuote("La pérdida no es otra cosa que cambio, y el cambio es el deleite de la naturaleza.", MARCUS_AURELIUS, "$MEDITATIONS, IX, 35"),
        SeedQuote("Al amanecer, dite a ti mismo: hoy me encontraré con el ingrato, el insolente y el envidioso. Pero ninguno de ellos puede dañarme.", MARCUS_AURELIUS, "$MEDITATIONS, II, 1"),

        // Séneca
        SeedQuote("Sufrimos más a menudo en la imaginación que en la realidad.", SENECA, "$LETTERS, XIII"),
        SeedQuote("No es que tengamos poco tiempo, sino que perdemos mucho.", SENECA, "$BREVITY, I"),
        SeedQuote("Mientras se aplaza, la vida pasa.", SENECA, "$LETTERS, I"),
        SeedQuote("Ningún viento es favorable para quien no sabe a qué puerto se dirige.", SENECA, "$LETTERS, LXXI"),
        SeedQuote("No es pobre el que tiene poco, sino el que mucho desea.", SENECA, "$LETTERS, II"),
        SeedQuote("Mientras vivamos, mientras estemos entre los hombres, cultivemos nuestra humanidad.", SENECA, "Sobre la ira, III, 43"),
        SeedQuote("Gran parte de la vida se nos va haciendo el mal, la mayor parte sin hacer nada y toda ella haciendo otra cosa.", SENECA, "$LETTERS, I"),
        SeedQuote("A veces, incluso vivir es un acto de valentía.", SENECA, "$LETTERS, LXXVIII"),
        SeedQuote("Hay que aprender a vivir durante toda la vida y, lo que quizá te sorprenda más, durante toda la vida hay que aprender a morir.", SENECA, "$BREVITY, VII"),
        SeedQuote("Los hombres, mientras enseñan, aprenden.", SENECA, "$LETTERS, VII"),

        // Epicteto
        SeedQuote("No son las cosas las que nos perturban, sino las opiniones que tenemos sobre ellas.", EPICTETUS, "$ENCHIRIDION, 5"),
        SeedQuote("De las cosas existentes, unas dependen de nosotros y otras no.", EPICTETUS, "$ENCHIRIDION, 1"),
        SeedQuote("No pretendas que las cosas ocurran como tú quieres; desea que ocurran tal como ocurren, y serás feliz.", EPICTETUS, "$ENCHIRIDION, 8"),
        SeedQuote("Si quieres progresar, consiente en parecer necio e insensato respecto a las cosas externas.", EPICTETUS, "$ENCHIRIDION, 13"),
        SeedQuote("Nunca digas de nada «lo he perdido», sino «lo he devuelto».", EPICTETUS, "$ENCHIRIDION, 11"),
        SeedQuote("Recuerda que eres actor de una obra cuyo papel ha elegido el autor; a ti te toca representarlo bien.", EPICTETUS, "$ENCHIRIDION, 17"),
        SeedQuote("Cuando alguien te irrite, sabe que es tu propia opinión la que te irrita.", EPICTETUS, "$ENCHIRIDION, 20"),
        SeedQuote("Primero dite a ti mismo qué quieres ser; luego haz lo que tengas que hacer.", EPICTETUS, "$DISCOURSES, III, 23"),
        SeedQuote("Es imposible que alguien aprenda lo que cree que ya sabe.", EPICTETUS, "$DISCOURSES, II, 17"),
        SeedQuote("Ningún hombre es libre si no es dueño de sí mismo.", EPICTETUS, "Fragmentos"),
    )
}
