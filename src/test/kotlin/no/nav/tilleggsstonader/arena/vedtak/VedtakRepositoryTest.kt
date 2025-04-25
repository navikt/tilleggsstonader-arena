package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.IntegrationTest
import no.nav.tilleggsstonader.arena.TestConstants.FNR
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.Rettighet
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.StatusVedtak
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.TypeVedtak
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.UtfallVedtak
import no.nav.tilleggsstonader.kontrakter.felles.Stønadstype
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class VedtakRepositoryTest : IntegrationTest() {
    @Autowired
    lateinit var vedtakRepository: VedtakRepository

    @Test
    fun `skal kunne hente ut vedtak`() {
        utilRepository.lagPerson()
        utilRepository.lagSak()
        utilRepository.lagVedtak()

        val rettigheter = Rettighet.fraStønadstype(Stønadstype.BARNETILSYN).map { it.kodeArena }
        val vedtak = vedtakRepository.finnVedtak(setOf(FNR), rettigheter).single()

        assertThat(vedtak.vedtakId).isEqualTo(400)
        assertThat(vedtak.sakId).isEqualTo(100)
        assertThat(vedtak.begrunnelse).isEqualTo("Syntetisert rettighet")
        assertThat(vedtak.vedtakstatus).isEqualTo(StatusVedtak.AVSLUTTET)
        assertThat(vedtak.vedtaktype).isEqualTo(TypeVedtak.NY_RETTIGHET)
        assertThat(vedtak.registrertDato).isEqualTo(LocalDate.of(2016, 1, 12))
        assertThat(vedtak.registrertAv).isEqualTo("ABC1234")
        assertThat(vedtak.modifisertDato).isEqualTo(LocalDate.of(2022, 2, 20))
        assertThat(vedtak.modifisertAv).isEqualTo("GRENSESN")
        assertThat(vedtak.utfall).isEqualTo(UtfallVedtak.JA)
        assertThat(vedtak.rettighet).isEqualTo(Rettighet.TILSYN_BARN)
        assertThat(vedtak.datoMottatt).isEqualTo(LocalDate.of(2016, 1, 14))
        assertThat(vedtak.vedtakIdRelatert).isNull()
        assertThat(vedtak.personId).isEqualTo(1)
        assertThat(vedtak.brukerIdBeslutter).isEqualTo("AA1234")
        assertThat(vedtak.datoInnstilt).isEqualTo(LocalDate.of(2022, 2, 26))
        assertThat(vedtak.erUtland).isEqualTo("N")
        assertThat(vedtak.fom).isEqualTo(LocalDate.of(2023, 1, 19))
        assertThat(vedtak.tom).isEqualTo(LocalDate.of(2023, 6, 4))
        assertThat(vedtak.totalbeløp).isEqualTo(20130)
    }

    @Test
    fun `skal finne vedtakfakta til vedtak`() {
        utilRepository.lagVedtakFakta()

        val vedtakfakta = vedtakRepository.finnVedtakFakta(setOf(400))
        assertThat(vedtakfakta).hasSize(1)

        with(vedtakfakta.single()) {
            assertThat(this.vedtakId).isEqualTo(400)
            assertThat(this.skjermbildetekst).isEqualTo("Justert fra dato")
            assertThat(this.vedtakverdi).isEqualTo("2023-01-01")
            assertThat(this.oracletype).isEqualTo("DATE")
        }
    }

    @Test
    fun `skal finne vilkårsvurdering til vedtak`() {
        utilRepository.lagVilkårsvurdering()

        val vilkårsvurderinger = vedtakRepository.finnVilkårsvurderinger(setOf(400))
        assertThat(vilkårsvurderinger).hasSize(1)

        with(vilkårsvurderinger.single()) {
            assertThat(this.vedtakId).isEqualTo(400)
            assertThat(this.skjermbildetekst).contains("Dagpenger - Stønadsmottakeren")
            assertThat(this.vilkaarstatusnavn).isEqualTo("Oppfylt")
            assertThat(this.vurdertAv).isEqualTo("Nissen")
        }
    }

    @Test
    fun `skal finne spesialutbetalinger til vedtak`() {
        utilRepository.lagSpesialutbetaling()

        val spesialutbetaling = vedtakRepository.finnSpesialutbetalinger(setOf(400))
        assertThat(spesialutbetaling).hasSize(1)

        with(spesialutbetaling.single()) {
            assertThat(this.vedtakId).isEqualTo(400)
            assertThat(this.brukerIdSaksbehandler).isEqualTo("saksbeh")
            assertThat(this.brukerIdBeslutter).isEqualTo("besl")
            assertThat(this.datoUtbetaling).isEqualTo(LocalDate.of(2025, 2, 1))
            assertThat(this.begrunnelse).isEqualTo("Begrunnelse")
            assertThat(this.belop).isEqualTo(1000)
            assertThat(this.datoFra).isEqualTo(LocalDate.of(2025, 1, 1))
            assertThat(this.datoTil).isEqualTo(LocalDate.of(2025, 1, 31))
        }
    }
}
