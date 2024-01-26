package no.nav.tilleggsstonader.arena.sak

import java.time.LocalDate

data class Sak(
    val sakId: Int,
    val personId: Int,
    val registrertDato: LocalDate,
    val registrertAv: String,
    val modifisertDato: LocalDate,
    val modifisertAv: String,
    val datoAvsluttet: LocalDate?,
    val status: StatusSak,
    val statusEndret: LocalDate,
    val saksforhold: Saksforhold?,
)

data class Saksforhold(
    val målgruppe: Målgruppe?,
    val fom: LocalDate?,
    val tom: LocalDate?,
    val kilde: String,
) {
    fun erBrukerregistrert() = kilde == "BRUKERREGISTRERT"
}

enum class Målgruppe(val kode: String, val navn: String) {
    ARBEIDSSØKER("ARBSOKERE", "Arbeidssøker"),
    ENSLIG_FORSØRGER_SØKER_ARBEID("ENSFORARBS", "Enslig forsørger som søker arbeid"),
    ENSLIG_FORSØRGER_UTDANNING("ENSFORUTD", "Enslig forsørger under utdanning"),
    GJENLEVENDE_SØKER_ARBEID("GJENEKARBS", "Gjenlevende ektefelle som søker arbeid"),
    GJENLEVENDE_UTDANNING("GJENEKUTD", "Gjenlevende ektefelle under utdanning"),
    DAGPENGER("MOTDAGPEN", "Mottaker av dagpenger"),
    TILTAKSPENGER("MOTTILTPEN", "Mottaker av tiltakspenger"),
    NEDSATT__ARBEIDSEVNE("NEDSARBEVN", "Person med nedsatt arbeidsevne pga. sykdom"),
    TIDLIGERE_FAMILIEPLEIER_UTDANNING("TIDLFAMPL", "Tidligere familiepleier under utdanning"),
    ;

    companion object {
        val values = Målgruppe.entries.associateBy { it.kode }

        fun fraKode(kode: String) = values[kode] ?: error("Finner ikke ${this::class.simpleName}.$kode")
    }
}

enum class StatusSak(val kode: String, val navn: String) {
    AKTIV("AKTIV", "Aktiv"),
    LUKKET("AVSLU", "Lukket"),
    HISTORISERT("HIST", "Historisert"),
    INAKTIV("INAKT", "Inaktiv"),
    OPPRETTET("OPRTV", "Opprettet (RTV)"),
    ;
    companion object {
        val values = StatusSak.entries.associateBy { it.kode }

        fun fraKode(kode: String) = values[kode] ?: error("Finner ikke ${this::class.simpleName}.$kode")
    }
}
