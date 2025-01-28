package no.nav.tilleggsstonader.arena.sak

import no.nav.tilleggsstonader.arena.IntegrationTest
import no.nav.tilleggsstonader.arena.TestConstants.FNR
import no.nav.tilleggsstonader.kontrakter.arena.sak.Målgruppe
import no.nav.tilleggsstonader.kontrakter.arena.sak.StatusSak
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
        utilRepository.lagSak(StatusSak.LUKKET)

        val sak = sakRepository.finnSaker(setOf(FNR)).single()

        assertThat(sak.sakId).isEqualTo(100)
        assertThat(sak.personId).isEqualTo(1)
        assertThat(sak.registrertDato).isEqualTo(LocalDate.of(2020, 2, 20))
        assertThat(sak.registrertAv).isEqualTo("ABC1234")
        assertThat(sak.modifisertDato).isEqualTo(LocalDate.of(2021, 2, 20))
        assertThat(sak.modifisertAv).isEqualTo("GRENSESN")
        assertThat(sak.datoAvsluttet).isNull()
        assertThat(sak.status).isEqualTo(StatusSak.LUKKET)
        assertThat(sak.statusEndret).isEqualTo(LocalDate.of(2022, 2, 20))

        assertThat(sak.saksforhold).isNull()
    }

    @Test
    fun `skal kunne hente ut saker med saksforhold`() {
        utilRepository.lagPerson()
        utilRepository.lagSak()
        utilRepository.lagSaksforhold()

        val sak = sakRepository.finnSaker(setOf(FNR)).single()

        assertThat(sak.saksforhold).isNotNull()
        assertThat(sak.saksforhold!!.målgruppe).isEqualTo(Målgruppe.NEDSATT_ARBEIDSEVNE)
        assertThat(sak.saksforhold!!.fom).isEqualTo(LocalDate.of(2021, 2, 25))
        assertThat(sak.saksforhold!!.tom).isEqualTo(LocalDate.of(2022, 2, 25))
        assertThat(sak.saksforhold!!.kilde).isEqualTo("BRUKERREGISTRERT")
        assertThat(sak.saksforhold!!.aktivitetId).isEqualTo(200)
        assertThat(sak.saksforhold!!.erBrukerregistrert()).isTrue()
    }

    @Nested
    inner class Aktiviteter {
        @BeforeEach
        fun setUp() {
            utilRepository.lagPerson()
            utilRepository.lagSak()
            utilRepository.lagSaksforhold()
        }

        @Test
        fun `skal finne saker med aktivitetId`() {
            utilRepository.lagAktivitet()
            val aktiviteter = sakRepository.finnAktiviteter(setOf(200))
            assertThat(aktiviteter).hasSize(1)
            with(aktiviteter.single()) {
                assertThat(this.aktivitetId).isEqualTo(200)
                assertThat(this.typekode).isEqualTo("JOBBKLUBB")
                assertThat(this.type).isEqualTo("Intern jobbklubb")
                assertThat(this.statuskode).isEqualTo("GJENN")
                assertThat(this.status).isEqualTo("Gjennomføres")
                assertThat(this.beskrivelse).isEqualTo("En beskrivelse")
                assertThat(this.gjelderUtdanning).isFalse()
            }
        }

        @Test
        fun `skal mappe gjelderUtdanning til true for utdanning for Ordinær utdanning`() {
            utilRepository.lagAktivitet("OUTDEF")
            val aktiviteter = sakRepository.finnAktiviteter(setOf(200))
            with(aktiviteter.single()) {
                assertThat(this.typekode).isEqualTo("OUTDEF")
                assertThat(this.gjelderUtdanning).isTrue()
            }
        }
    }

    @Nested
    inner class HarSaker {
        @Test
        fun `har sak`() {
            utilRepository.lagPerson()
            utilRepository.lagSak()

            assertThat(sakRepository.antallSaker(setOf(FNR))).isEqualTo(1)
        }

        @Test
        fun `har ikke sak`() {
            assertThat(sakRepository.antallSaker(setOf(FNR))).isEqualTo(0)
        }
    }

    @Nested
    inner class AntallSakerUtenVedtak {
        @Test
        fun `har sak uten vedtak`() {
            utilRepository.lagPerson()
            utilRepository.lagSak()

            assertThat(sakRepository.antallSakerUtenVedtak(setOf(FNR), SAK_AKTIVE_STATUSER))
                .isEqualTo(1)
        }

        @Test
        fun `har ikke sak uten vedtak dersom status ikke er aktiv`() {
            utilRepository.lagPerson()
            utilRepository.lagSak(status = StatusSak.INAKTIV)

            assertThat(sakRepository.antallSakerUtenVedtak(setOf(FNR), SAK_AKTIVE_STATUSER))
                .isEqualTo(0)
        }

        @Test
        fun `har ikke sak uten vedtak`() {
            utilRepository.lagPerson()
            utilRepository.lagSak()
            utilRepository.lagVedtak()

            assertThat(sakRepository.antallSakerUtenVedtak(setOf(FNR), SAK_AKTIVE_STATUSER))
                .isEqualTo(0)
        }
    }
}
