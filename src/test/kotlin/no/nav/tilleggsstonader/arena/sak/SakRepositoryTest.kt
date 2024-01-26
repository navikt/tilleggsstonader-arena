package no.nav.tilleggsstonader.arena.sak

import no.nav.tilleggsstonader.arena.IntegrationTest
import no.nav.tilleggsstonader.arena.TestConstants.FNR
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class SakRepositoryTest : IntegrationTest() {

    @Autowired
    lateinit var sakRepository: SakRepository

    @Test
    fun `skal kunne hente ut saker uten saksforhold`() {
        utilRepository.lagPerson()
        utilRepository.lagSak()

        val sak = sakRepository.hentSaker(setOf(FNR)).single()

        assertThat(sak.sakId).isEqualTo(100)
        assertThat(sak.personId).isEqualTo(1)
        assertThat(sak.registrertDato).isEqualTo(LocalDate.of(2020, 2, 20))
        assertThat(sak.registrertAv).isEqualTo("ABC1234")
        assertThat(sak.modifisertDato).isEqualTo(LocalDate.of(2021, 2, 20))
        assertThat(sak.modifisertAv).isEqualTo("GRENSESN")
        assertThat(sak.datoAvsluttet).isNull()
        assertThat(sak.status).isEqualTo(StatusSak.AKTIV)
        assertThat(sak.statusEndret).isEqualTo(LocalDate.of(2022, 2, 20))

        assertThat(sak.saksforhold).isNull()
    }

    @Test
    fun `skal kunne hente ut saker med saksforhold`() {
        utilRepository.lagPerson()
        utilRepository.lagSak()
        utilRepository.lagSaksforhold()

        val sak = sakRepository.hentSaker(setOf(FNR)).single()

        assertThat(sak.saksforhold).isNotNull()
        assertThat(sak.saksforhold!!.målgruppe).isEqualTo(Målgruppe.NEDSATT__ARBEIDSEVNE)
        assertThat(sak.saksforhold!!.fom).isEqualTo(LocalDate.of(2021, 2, 25))
        assertThat(sak.saksforhold!!.tom).isEqualTo(LocalDate.of(2022, 2, 25))
        assertThat(sak.saksforhold!!.kilde).isEqualTo("BRUKERREGISTRERT")
        assertThat(sak.saksforhold!!.erBrukerregistrert()).isTrue()
    }

    @Nested
    inner class HarSakerUtenVedtak {

        @Test
        fun `har sak uten vedtak`() {
            utilRepository.lagPerson()
            utilRepository.lagSak()

            assertThat(sakRepository.harAktiveSakerUtenVedtak(setOf(FNR))).isTrue()
        }

        @Test
        fun `har ikke sak uten vedtak dersom status ikke er aktiv`() {
            utilRepository.lagPerson()
            utilRepository.lagSak(status = StatusSak.INAKTIV)

            assertThat(sakRepository.harAktiveSakerUtenVedtak(setOf(FNR))).isFalse()
        }

        @Test
        fun `har ikke sak uten vedtak`() {
            utilRepository.lagPerson()
            utilRepository.lagSak()
            utilRepository.lagVedtak()

            assertThat(sakRepository.harAktiveSakerUtenVedtak(setOf(FNR))).isFalse()
        }
    }
}
