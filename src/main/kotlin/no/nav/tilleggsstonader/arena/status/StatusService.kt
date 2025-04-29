package no.nav.tilleggsstonader.arena.status

import no.nav.tilleggsstonader.arena.infrastruktur.config.ApiFeil
import no.nav.tilleggsstonader.arena.sak.SAK_AKTIVE_STATUSER
import no.nav.tilleggsstonader.arena.sak.SakRepository
import no.nav.tilleggsstonader.arena.vedtak.VedtakRepository
import no.nav.tilleggsstonader.arena.vedtak.VedtakStatusMapper
import no.nav.tilleggsstonader.kontrakter.arena.ArenaStatusDto
import no.nav.tilleggsstonader.kontrakter.arena.ArenaStatusHarSakerDto
import no.nav.tilleggsstonader.kontrakter.arena.SakStatus
import no.nav.tilleggsstonader.kontrakter.arena.VedtakStatus
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.Rettighet
import no.nav.tilleggsstonader.kontrakter.felles.IdenterRequest
import no.nav.tilleggsstonader.kontrakter.felles.IdenterStønadstype
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class StatusService(
    private val vedtakRepository: VedtakRepository,
    private val sakRepository: SakRepository,
) {
    fun harSaker(request: IdenterRequest): ArenaStatusHarSakerDto = ArenaStatusHarSakerDto(sakRepository.antallSaker(request.identer) > 0)

    fun hentStatus(request: IdenterStønadstype): ArenaStatusDto {
        if (request.identer.isEmpty()) {
            throw ApiFeil(
                feil = "Ingen identer i request",
                httpStatus = HttpStatus.BAD_REQUEST,
            )
        }

        return ArenaStatusDto(
            sak = hentSakstatus(request),
            vedtak = hentVedtakstatus(request),
        )
    }

    private fun hentVedtakstatus(request: IdenterStønadstype): VedtakStatus =
        vedtakRepository
            .finnVedtak(request.identer, Rettighet.fraStønadstype(request.stønadstype).map { it.kodeArena })
            .let(VedtakStatusMapper::tilVedtakStatus)

    private fun hentSakstatus(request: IdenterStønadstype): SakStatus =
        SakStatus(
            harAktivSakUtenVedtak = harAktivSakUtenVedtak(request),
        )

    private fun harAktivSakUtenVedtak(request: IdenterStønadstype): Boolean =
        sakRepository.antallSakerUtenVedtak(request.identer, SAK_AKTIVE_STATUSER) > 0
}
