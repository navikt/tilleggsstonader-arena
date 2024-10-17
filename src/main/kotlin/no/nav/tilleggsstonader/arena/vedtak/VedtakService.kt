package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.sak.SakRepository
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.Rettighet
import org.springframework.stereotype.Service

@Service
class VedtakService(
    private val vedtakService: VedtakRepository,
    private val sakRepository: SakRepository,
    private val vedtakRepository: VedtakRepository,
) {

    fun hentSakOgVedtak(identer: Set<String>): SakOgVedtak {
        val alleVedtak = vedtakService.finnVedtak(identer, Rettighet.entries.map { it.kodeArena })
        val saker = sakRepository.finnSaker(identer)
        val vedtakfakta = vedtakRepository.finnVedtakFakta(alleVedtak.map { it.vedtakId }.toSet())
        val vilkårsvurderinger = vedtakRepository.finnVilkårsvurderinger(alleVedtak.map { it.vedtakId }.toSet())
        val aktiviteter = sakRepository.finnAktiviteter(saker.mapNotNull { it.saksforhold?.aktivitetId }.toSet())
        return SakOgVedtak(
            saker = saker,
            vedtak = alleVedtak,
            vedtakfakta = vedtakfakta,
            vilkårsvurderinger = vilkårsvurderinger,
            aktiviteter = aktiviteter,
        )
    }
}
