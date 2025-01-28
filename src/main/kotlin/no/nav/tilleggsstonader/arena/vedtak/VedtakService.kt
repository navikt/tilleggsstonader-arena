package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.sak.Aktivitet
import no.nav.tilleggsstonader.arena.sak.Sak
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
        val vedtakIder = alleVedtak.map { it.vedtakId }.toSet()

        val aktiviteter = hentAktiviteter(saker)
        return SakOgVedtak(
            saker = saker,
            vedtak = alleVedtak,
            vedtakfakta = hentVedtakfakta(vedtakIder),
            vilkårsvurderinger = hentVilkårsvurderinger(vedtakIder),
            aktiviteter = aktiviteter,
        )
    }

    private fun hentAktiviteter(saker: List<Sak>): List<Aktivitet> {
        val aktivitetIder = saker.mapNotNull { it.saksforhold?.aktivitetId }.toSet()
        if (aktivitetIder.isEmpty()) {
            return emptyList()
        }
        return sakRepository.finnAktiviteter(aktivitetIder)
    }

    private fun hentVilkårsvurderinger(vedtakIder: Set<Int>): List<Vilkårsvurdering> {
        if (vedtakIder.isEmpty()) {
            return emptyList()
        }
        return vedtakRepository.finnVilkårsvurderinger(vedtakIder)
    }

    private fun hentVedtakfakta(vedtakIder: Set<Int>): List<Vedtakfakta> {
        if (vedtakIder.isEmpty()) {
            return emptyList()
        }
        return vedtakRepository.finnVedtakFakta(vedtakIder)
    }
}
