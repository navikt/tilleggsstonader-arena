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
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.Rettighet.Companion.tilArenaKoder
import no.nav.tilleggsstonader.kontrakter.felles.IdenterRequest
import no.nav.tilleggsstonader.kontrakter.felles.IdenterStønadstype
import no.nav.tilleggsstonader.kontrakter.felles.IdenterStønadstyper
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
            sak = hentSakstatus(request.identer),
            vedtak = hentVedtakstatus(request),
        )
    }

    fun hentStatus(request: IdenterStønadstyper): ArenaStatusDto {
        if (request.identer.isEmpty()) {
            throw ApiFeil(
                feil = "Ingen identer i request",
                httpStatus = HttpStatus.BAD_REQUEST,
            )
        }
        if (request.stønadstyper.isEmpty()) {
            throw ApiFeil(
                feil = "Ingen stønadstyper i request",
                httpStatus = HttpStatus.BAD_REQUEST,
            )
        }
        return ArenaStatusDto(
            sak = hentSakstatus(request.identer),
            vedtak = hentVedtakstatus(request),
        )
    }

    private fun hentVedtakstatus(request: IdenterStønadstype): VedtakStatus =
        vedtakRepository
            .finnVedtak(request.identer, Rettighet.fraStønadstype(request.stønadstype).map { it.kodeArena })
            .let(VedtakStatusMapper::tilVedtakStatus)

    private fun hentVedtakstatus(request: IdenterStønadstyper): VedtakStatus =
        vedtakRepository
            .finnVedtak(request.identer, request.stønadstyper.tilArenaKoder())
            .let(VedtakStatusMapper::tilVedtakStatus)

    private fun hentSakstatus(identer: Set<String>): SakStatus =
        SakStatus(
            harAktivSakUtenVedtak = harAktivSakUtenVedtak(identer),
        )

    private fun harAktivSakUtenVedtak(identer: Set<String>): Boolean =
        sakRepository
            .antallSakerUtenVedtak(identer, SAK_AKTIVE_STATUSER) > 0
}
