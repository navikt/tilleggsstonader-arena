package no.nav.tilleggsstonader.arena.sak

import java.time.LocalDate

data class Aktivitet(
    val aktivitetId: Int,
    val typekode: String,
    val type: String,
    val fom: LocalDate?,
    val tom: LocalDate?,
    val statuskode: String,
    val status: String,
    val beskrivelse: String?,
    val gjelderUtdanning: Boolean,
)
