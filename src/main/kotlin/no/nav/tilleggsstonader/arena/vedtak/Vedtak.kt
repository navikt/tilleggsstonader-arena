package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.felles.KodeArena
import no.nav.tilleggsstonader.kontrakter.felles.Stønadstype
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

enum class UtfallVedtak {
    NEI,
    JA,
    AVBRUTT,
}

enum class StatusVedtak(override val kodeArena: String, val navn: String) : KodeArena {
    GODKJENT("GODKJ", "Godkjent"),
    REGISTRERT("REGIS", "Registrert"),
    OPPRETTET("OPPRE", "Opprettet"),
    AVSLUTTET("AVSLU", "Avsluttet"),
    INNSTILT("INNST", "Innstilt"),
    IVERKSATT("IVERK", "Iverksatt"),
    MOTTATT("MOTAT", "Mottatt"),
}

enum class TypeVedtak(override val kodeArena: String, val navn: String) : KodeArena {
    ENDRING("E", "Endring"),
    NY_RETTIGHET("O", "Ny rettighet"),
    STANS("S", "Stans"),
}

enum class Rettighet(override val kodeArena: String, val navn: String) : KodeArena {
    BOUTGIFTER_ARBEIDSSØKERE("TSRBOUTG", "Boutgifter arbeidssøkere"),
    BOUTGIFTER("TSOBOUTG", "Boutgifter tilleggsstønad"),
    DAGLIG_REISE_ARBEIDSSØKERE("TSRDAGREIS", "Daglig reise arbeidssøkere"),
    DAGLIG_REISE("TSODAGREIS", "Daglig reise tilleggsstønad"),
    FLYTTING_ARBEIDSSSØKERE("TSRFLYTT", "Flytting arbeidssøkere"),
    FLYTTING("TSOFLYTT", "Flytting tilleggsstønad"),
    LÆREMIDLER_ARBEIDSSSØKERE("TSRLMIDLER", "Læremidler arbeidssøkere"),
    LÆREMIDLER("TSOLMIDLER", "Læremidler tilleggsstønad"),
    REISE_OBLIGATORISK_SAMLING_ARBEIDSSSØKERE("TSRREISOBL", "Reise til obligatorisk samling arbeidssøkere"),
    REISE_OBLIGATORISK_SAMLING("TSOREISOBL", "Reise til obligatorisk samling tilleggsstønad"),
    REISE_AKTIVITET_HJEMREISE_ARBEIDSSSØKERE(
        "TSRREISAKT",
        "Reise ved start/slutt aktivitet og hjemreiser arbeidssøkere",
    ),
    REISE_AKTIVITET_HJEMREISE("TSOREISAKT", "Reise ved start/slutt aktivitet og hjemreiser tilleggsstønad"),
    REISE_ARBEIDSSSØKERE("TSRREISARB", "Reisestønader til arbeidssøkere"),
    REISE("TSOREISARB", "Reisestønader til arbeidssøkere tilleggsstønad"),
    TILSYN_BARN_ARBEIDSSSØKERE("TSRTILBARN", "Tilsyn av barn arbeidssøkere"),
    TILSYN_BARN("TSOTILBARN", "Tilsyn av barn tilleggsstønad"),
    TILSYN_FAMILIEMEDLEMMER_ARBEIDSSSØKERE("TSRTILFAM", "Tilsyn av familiemedlemmer arbeidssøkere"),
    TILSYN_FAMILIEMEDLEMMER("TSOTILFAM", "Tilsyn av familiemedlemmer tilleggsstønad"),
}

fun Stønadstype.rettigheter(): List<String> = when (this) {
    Stønadstype.BARNETILSYN -> setOf(Rettighet.TILSYN_BARN, Rettighet.TILSYN_BARN_ARBEIDSSSØKERE).map { it.kodeArena }
}
