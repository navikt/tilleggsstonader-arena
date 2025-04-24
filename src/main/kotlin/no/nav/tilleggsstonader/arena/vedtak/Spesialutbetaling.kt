package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.kontrakter.arena.vedtak.StatusVedtak
import java.time.LocalDate

data class Spesialutbetaling(
    val spesutbetalingId: Int,
    val vedtakId: Int,
    val brukerIdSaksbehandler: String,
    val brukerIdBeslutter: String?,
    val datoUtbetaling: LocalDate,
    val begrunnelse: String?,
    val belop: Int,
    val datoFra: LocalDate,
    val datoTil: LocalDate,
    val vedtakstatuskode: StatusVedtak,
    val regDato: LocalDate,
    val modDato: LocalDate,
)
