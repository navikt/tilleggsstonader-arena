package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.kontrakter.arena.VedtakStatus
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.UtfallVedtak
import java.time.LocalDate

object VedtakStatusMapper {
    fun tilVedtakStatus(alleVedtak: List<Vedtak>): VedtakStatus {
        val innvilgedeVedtak =
            alleVedtak
                .filter { it.utfall == UtfallVedtak.JA }
                .filter { it.datoInnstilt != null }
        return VedtakStatus(
            harVedtak = alleVedtak.isNotEmpty(),
            harInnvilgetVedtak = innvilgedeVedtak.isNotEmpty(),
            harAktivtVedtak = innvilgedeVedtak.any { it.tom != null && it.tom > LocalDate.now() },
            harVedtakUtenUtfall = alleVedtak.filter { it.utfall != UtfallVedtak.AVBRUTT }.any { it.datoInnstilt == null },
            vedtakTom = innvilgedeVedtak.mapNotNull { it.tom }.maxOrNull(),
        )
    }
}
