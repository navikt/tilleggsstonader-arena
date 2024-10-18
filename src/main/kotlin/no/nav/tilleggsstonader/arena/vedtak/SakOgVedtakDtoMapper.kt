package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.sak.Aktivitet
import no.nav.tilleggsstonader.arena.sak.Saksforhold
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.AktivitetDto
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.ArenaSakOgVedtakDto
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.SakDto
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.VedtakDto
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.VedtakfaktaDto
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.VilkårsvurderingDto
import no.nav.tilleggsstonader.libs.log.SecureLogger.secureLogger
import org.slf4j.LoggerFactory

object SakOgVedtakDtoMapper {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val REGEX_DATO = """^\d{2}-\d{2}-\d{4}$""".toRegex()

    fun map(sakOgVedtak: SakOgVedtak): ArenaSakOgVedtakDto {
        val aktiviteter = sakOgVedtak.aktiviteter.associateBy { it.aktivitetId }
        val vedtakfakta = sakOgVedtak.vedtakfakta.groupBy { it.vedtakId }
        val vilkårsvurderinger = sakOgVedtak.vilkårsvurderinger.groupBy { it.vedtakId }
        return ArenaSakOgVedtakDto(
            vedtak = sakOgVedtak.vedtak
                .map { mapVedtak(it, vedtakfakta, vilkårsvurderinger) }
                .sortedWith(vedtaksortering),
            saker = mapSaker(sakOgVedtak, aktiviteter),
        )
    }

    private fun mapSaker(
        sakOgVedtak: SakOgVedtak,
        aktiviteter: Map<Int, Aktivitet>,
    ) = sakOgVedtak.saker.associate {
        it.sakId to SakDto(
            målgruppe = it.saksforhold?.målgruppe?.navn,
            aktivitet = mapAktivitet(it.saksforhold, aktiviteter),
        )
    }

    private fun mapVedtak(
        vedtak: Vedtak,
        vedtakfakta: Map<Int, List<Vedtakfakta>>,
        vilkårsvurderinger: Map<Int, List<Vilkårsvurdering>>,
    ) = VedtakDto(
        sakId = vedtak.sakId,
        type = vedtak.vedtaktype.navn,
        status = vedtak.vedtakstatus.navn,
        rettighet = vedtak.rettighet.navn,
        rettighetkode = vedtak.rettighet,
        fom = vedtak.fom,
        tom = vedtak.tom,
        totalbeløp = vedtak.totalbeløp,
        datoInnstillt = vedtak.datoInnstilt,
        utfall = vedtak.utfall?.navn,
        vedtakfakta = mapVedtakFakta(vedtak, vedtakfakta),
        vilkårsvurderinger = mapVilkårsvurderinger(vedtak, vilkårsvurderinger),
        datoMottatt = vedtak.datoMottatt,
        saksbehandler = vedtak.brukerIdAnsvarlig,
        beslutter = vedtak.brukerIdBeslutter,
    )

    private fun mapVilkårsvurderinger(
        vedtak: Vedtak,
        vilkårsvurderinger: Map<Int, List<Vilkårsvurdering>>,
    ): List<VilkårsvurderingDto> {
        return vilkårsvurderinger
            .getOrDefault(vedtak.vedtakId, emptyList())
            .map {
                VilkårsvurderingDto(
                    vilkår = it.skjermbildetekst,
                    vurdering = it.vilkaarstatusnavn,
                    vurdertAv = it.vurdertAv,
                )
            }.sortedBy { it.vilkår }
    }

    private fun mapVedtakFakta(
        vedtak: Vedtak,
        vedtakfakta: Map<Int, List<Vedtakfakta>>,
    ) =
        vedtakfakta
            .getOrDefault(vedtak.vedtakId, emptyList())
            .map {
                VedtakfaktaDto(
                    type = it.skjermbildetekst,
                    verdi = mapVerdi(it),
                )
            }.sortedBy { it.type }

    fun mapVerdi(vedtakfakta: Vedtakfakta): String? {
        if (vedtakfakta.oracletype == "DATE" && vedtakfakta.vedtakverdi != null && REGEX_DATO.matches(vedtakfakta.vedtakverdi)) {
            try {
                return vedtakfakta.vedtakverdi.replace("-", ".")
            } catch (e: Exception) {
                logger.warn("Feilet mapping av verdi av type=${vedtakfakta.skjermbildetekst}")
                secureLogger.warn("Feilet mapping av $vedtakfakta")
            }
        }
        return vedtakfakta.vedtakverdi
    }

    private fun mapAktivitet(
        saksforhold: Saksforhold?,
        aktiviteter: Map<Int, Aktivitet>,
    ): AktivitetDto? =
        saksforhold?.aktivitetId
            ?.let { aktiviteter[it] }
            ?.let {
                AktivitetDto(
                    aktivitetId = it.aktivitetId,
                    type = it.type,
                    status = it.status,
                    fom = it.fom,
                    tom = it.tom,
                    beskrivelse = it.beskrivelse,
                    gjelderUtdanning = it.gjelderUtdanning,
                    typekode = it.typekode,
                    statuskode = it.statuskode,
                )
            }

    private val vedtaksortering = compareByDescending<VedtakDto> { it.fom }
        .thenByDescending { it.tom }
        .thenByDescending { it.datoInnstillt }
}
