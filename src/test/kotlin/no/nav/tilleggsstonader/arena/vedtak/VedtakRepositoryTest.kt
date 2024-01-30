package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.IntegrationTest
import no.nav.tilleggsstonader.arena.TestConstants.FNR
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

        val vedtak = vedtakRepository.finnVedtak(setOf(FNR), Stønadstype.BARNETILSYN.rettigheter()).single()

        assertThat(vedtak.vedtakId).isEqualTo(400)
        assertThat(vedtak.sakId).isEqualTo(100)
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
}
