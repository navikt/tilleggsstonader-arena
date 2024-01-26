package no.nav.tilleggsstonader.arena.status

import no.nav.tilleggsstonader.arena.sak.SakRepository
import no.nav.tilleggsstonader.arena.vedtak.UtfallVedtak
import no.nav.tilleggsstonader.arena.vedtak.VedtakRepository
import no.nav.tilleggsstonader.kontrakter.arena.ArenaStatusDto
import no.nav.tilleggsstonader.kontrakter.arena.SakStatus
import no.nav.tilleggsstonader.kontrakter.arena.VedtakStatus
import no.nav.tilleggsstonader.kontrakter.felles.IdenterStønadstype
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class StatusService(
    private val vedtakRepository: VedtakRepository,
    private val sakRepository: SakRepository,
) {

    fun hentStatus(request: IdenterStønadstype): ArenaStatusDto {
        return ArenaStatusDto(
            sak = hentSakstatus(request),
            vedtak = hentVedtakstatus(request),
        )
    }

    private fun hentVedtakstatus(request: IdenterStønadstype): VedtakStatus {
        val vedtak = vedtakRepository.hentVedtak(request.identer, request.stønadstype)
        return VedtakStatus(
            harVedtak = vedtak.isNotEmpty(),
            harAktivtVedtak = vedtak
                .filter { it.utfall == UtfallVedtak.JA }
                .filter { it.tom != null && it.tom > LocalDate.now() }
                .any(),
        )
    }

    private fun hentSakstatus(request: IdenterStønadstype): SakStatus {
        val sak = sakRepository.hentSaker(request.identer)
        return SakStatus(
            harSakSomIkkeErKobletVedtak = false, // TODO
        )
    }
}
