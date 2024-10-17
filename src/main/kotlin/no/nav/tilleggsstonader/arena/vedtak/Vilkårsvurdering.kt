package no.nav.tilleggsstonader.arena.vedtak

data class Vilkårsvurdering(
    val vedtakId: Int,
    val skjermbildetekst: String,
    val vilkaarstatusnavn: String,
    val vurdertAv: String?,
)
