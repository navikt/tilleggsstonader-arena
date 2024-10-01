package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.kontrakter.arena.VedtakStatus
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.UtfallVedtak
import no.nav.tilleggsstonader.libs.utils.osloDateNow

object VedtakStatusMapper {
    fun tilVedtakStatus(alleVedtak: List<Vedtak>): VedtakStatus {
        val innvilgedeVedtak = alleVedtak.filter { it.utfall == UtfallVedtak.JA }
        return VedtakStatus(
            harVedtak = alleVedtak.isNotEmpty(),
            harInnvilgetVedtak = innvilgedeVedtak.isNotEmpty(),
            harAktivtVedtak = innvilgedeVedtak.any { it.tom != null && it.tom > osloDateNow() },
            harVedtakUtenUtfall = alleVedtak.any { it.utfall == null },
            vedtakTom = innvilgedeVedtak.mapNotNull { it.tom }.maxOrNull(),
        )
    }
}
