package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.kontrakter.arena.vedtak.Rettighet
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.StatusVedtak
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.TypeVedtak
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.UtfallVedtak
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDate

data class Vedtak(
    val vedtakId: Int,
    val sakId: Int,
    @Column("vedtakstatuskode")
    val vedtakstatus: StatusVedtak,
    @Column("vedtaktypekode")
    val vedtaktype: TypeVedtak,
    @Column("reg_dato")
    val registrertDato: LocalDate,
    @Column("reg_user")
    val registrertAv: String,
    @Column("mod_dato")
    val modifisertDato: LocalDate,
    @Column("mod_user")
    val modifisertAv: String,
    @Column("utfallkode")
    val utfall: UtfallVedtak?,
    @Column("rettighetkode")
    val rettighet: Rettighet,
    val datoMottatt: LocalDate,
    val vedtakIdRelatert: Int?,
    val personId: Int,
    @Column("brukerid_ansvarlig")
    val brukerIdAnsvarlig: String?,
    @Column("brukerid_beslutter")
    val brukerIdBeslutter: String?,
    val datoInnstilt: LocalDate?,
    val erUtland: String,
    @Column("fra_dato")
    val fom: LocalDate?,
    @Column("til_dato")
    val tom: LocalDate?,
    @Column("totalbelop")
    val totalbeløp: Int?,
) {
    fun gjelderUtland() = erUtland == "J"
}
