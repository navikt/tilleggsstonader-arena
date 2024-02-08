package no.nav.tilleggsstonader.arena.sak

import no.nav.tilleggsstonader.kontrakter.arena.sak.Målgruppe
import no.nav.tilleggsstonader.kontrakter.arena.sak.StatusSak
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
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
