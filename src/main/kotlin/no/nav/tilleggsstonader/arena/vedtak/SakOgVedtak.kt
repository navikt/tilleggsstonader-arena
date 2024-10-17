package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.sak.Aktivitet
import no.nav.tilleggsstonader.arena.sak.Sak

data class SakOgVedtak(
    val saker: List<Sak>,
    val vedtak: List<Vedtak>,
    val vedtakfakta: List<Vedtakfakta>,
    val vilkårsvurderinger: List<Vilkårsvurdering>,
    val aktiviteter: List<Aktivitet>,
)
