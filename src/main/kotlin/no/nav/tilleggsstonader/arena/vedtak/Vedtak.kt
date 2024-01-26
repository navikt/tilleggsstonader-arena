package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.kontrakter.felles.Stønadstype
import java.time.LocalDate

data class Vedtak(
    val vedtakId: Int,
    val sakId: Int,
    val vedtakstatus: StatusVedtak,
    val vedtaktype: TypeVedtak,
    val registrertDato: LocalDate,
    val registrertAv: String,
    val modifisertDato: LocalDate,
    val modifisertAv: String,
    val utfall: UtfallVedtak?,
    val rettighet: Rettighet,
    val datoMottatt: LocalDate,
    val vedtakIdRelatert: Int?,
    val personId: Int,
    val brukerIdBeslutter: String?,
    val datoInnstilt: LocalDate?,
    val erUtland: Boolean,
    val fom: LocalDate?,
    val tom: LocalDate?,
    val totalbeløp: Int?,
)

enum class UtfallVedtak {
    NEI,
    JA,
    AVBRUTT,
}

enum class StatusVedtak(val kode: String, val navn: String) {
    GODKJENT("GODKJ", "Godkjent"),
    REGISTRERT("REGIS", "Registrert"),
    OPPRETTET("OPPRE", "Opprettet"),
    AVSLUTTET("AVSLU", "Avsluttet"),
    INNSTILT("INNST", "Innstilt"),
    IVERKSATT("IVERK", "Iverksatt"),
    MOTTATT("MOTAT", "Mottatt"),
    ;

    companion object {
        val values = StatusVedtak.entries.associateBy { it.kode }

        fun fraKode(kode: String) = values[kode] ?: error("Finner ikke ${this::class.simpleName}.$kode")
    }
}

enum class TypeVedtak(val kode: String, val navn: String) {
    ENDRING("E", "Endring"),
    NY_RETTIGHET("O", "Ny rettighet"),
    STANS("S", "Stans"),
    ;

    companion object {
        val values = TypeVedtak.entries.associateBy { it.kode }

        fun fraKode(kode: String) = values[kode] ?: error("Finner ikke ${this::class.simpleName}.$kode")
    }
}

enum class Rettighet(val kode: String, val navn: String) {
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
    ;

    companion object {
        val values = Rettighet.entries.associateBy { it.kode }

        fun fraKode(kode: String) = values[kode] ?: error("Finner ikke ${this::class.simpleName}.$kode")
    }
}

fun Stønadstype.rettigheter(): Set<Rettighet> = when (this) {
    Stønadstype.BARNETILSYN -> setOf(Rettighet.TILSYN_BARN, Rettighet.TILSYN_BARN_ARBEIDSSSØKERE)
}
