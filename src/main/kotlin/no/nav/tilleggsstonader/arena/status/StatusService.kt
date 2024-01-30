package no.nav.tilleggsstonader.arena.status

import no.nav.tilleggsstonader.arena.sak.SAK_AKTIVE_STATUSER
import no.nav.tilleggsstonader.arena.sak.SakRepository
import no.nav.tilleggsstonader.arena.vedtak.UtfallVedtak
import no.nav.tilleggsstonader.arena.vedtak.VedtakRepository
import no.nav.tilleggsstonader.arena.vedtak.rettigheter
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
        val vedtak = vedtakRepository.finnVedtak(request.identer, request.stønadstype.rettigheter())
        return VedtakStatus(
            harVedtak = vedtak.isNotEmpty(),
            harAktivtVedtak = vedtak
                .filter { it.utfall == UtfallVedtak.JA }
                .filter { it.tom != null && it.tom > LocalDate.now() }
                .any(),
        )
    }

    private fun hentSakstatus(request: IdenterStønadstype): SakStatus {
        return SakStatus(
            harAktivSakUtenVedtak = harAktivSakUtenVedtak(request),
        )
    }

    private fun harAktivSakUtenVedtak(request: IdenterStønadstype): Boolean {
        return sakRepository.antallSakerUtenVedtak(request.identer, SAK_AKTIVE_STATUSER) > 0
    }
}
