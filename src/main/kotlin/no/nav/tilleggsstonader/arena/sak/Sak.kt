package no.nav.tilleggsstonader.arena.sak

import no.nav.tilleggsstonader.arena.felles.Arenakode
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.MappedCollection
import java.time.LocalDate

data class Sak(
    @Id
    val sakId: Int,
    val personId: Int,
    @Column("reg_dato")
    val registrertDato: LocalDate,
    @Column("reg_user")
    val registrertAv: String,
    @Column("mod_dato")
    val modifisertDato: LocalDate,
    @Column("mod_user")
    val modifisertAv: String,
    val datoAvsluttet: LocalDate?,
    @Column("sakstatuskode")
    val status: StatusSak,
    val statusEndret: LocalDate,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    val saksforhold: Saksforhold?,
)

data class Saksforhold(
    @Column("maalgruppekode")
    val målgruppe: Målgruppe?,
    @Column("dato_fra")
    val fom: LocalDate?,
    @Column("dato_til")
    val tom: LocalDate?,
    val kilde: String,
) {
    fun erBrukerregistrert() = kilde == "BRUKERREGISTRERT"
}

enum class Målgruppe(override val kodeArena: String, val navn: String): Arenakode {
    ARBEIDSSØKER("ARBSOKERE", "Arbeidssøker"),
    ENSLIG_FORSØRGER_SØKER_ARBEID("ENSFORARBS", "Enslig forsørger som søker arbeid"),
    ENSLIG_FORSØRGER_UTDANNING("ENSFORUTD", "Enslig forsørger under utdanning"),
    GJENLEVENDE_SØKER_ARBEID("GJENEKARBS", "Gjenlevende ektefelle som søker arbeid"),
    GJENLEVENDE_UTDANNING("GJENEKUTD", "Gjenlevende ektefelle under utdanning"),
    DAGPENGER("MOTDAGPEN", "Mottaker av dagpenger"),
    TILTAKSPENGER("MOTTILTPEN", "Mottaker av tiltakspenger"),
    NEDSATT__ARBEIDSEVNE("NEDSARBEVN", "Person med nedsatt arbeidsevne pga. sykdom"),
    TIDLIGERE_FAMILIEPLEIER_UTDANNING("TIDLFAMPL", "Tidligere familiepleier under utdanning"),
}

enum class StatusSak(override val kodeArena: String, val navn: String): Arenakode {
    AKTIV("AKTIV", "Aktiv"),
    LUKKET("AVSLU", "Lukket"),
    HISTORISERT("HIST", "Historisert"),
    INAKTIV("INAKT", "Inaktiv"),
    OPPRETTET("OPRTV", "Opprettet (RTV)"),
}

