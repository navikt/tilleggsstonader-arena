package no.nav.tilleggsstonader.arena.sak

data class Aktivitet(
    val aktivitetId: Int,
    val typekode: String,
    val type: String,

    val statuskode: String,
    val status: String,
    val beskrivelse: String?,
    val gjelderUtdanning: Boolean,
)
