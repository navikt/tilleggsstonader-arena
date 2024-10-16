package no.nav.tilleggsstonader.arena.vedtak

/**
 * @param oracletype kan brukes for å vite hvordan den skal mappes.
 * Eks DATE kan brukes for å formattere den som norsk dato
 */
data class Vedtakfakta(
    val vedtakId: Int,
    val skjermbildetekst: String,
    val vedtakverdi: String?,
    val oracletype: String?,
)
