package no.nav.tilleggsstonader.arena.oppgave

import no.nav.tilleggsstonader.arena.IntegrationTest
import no.nav.tilleggsstonader.arena.TestConstants
import no.nav.tilleggsstonader.kontrakter.arena.sak.Målgruppe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalDateTime

class OppgaveRepositoryTest : IntegrationTest() {

    @Autowired
    lateinit var oppgaveRepository: OppgaveRepository

    val identer = setOf("TS:${TestConstants.FNR}")

    @Test
    fun `skal mappe felter`() {
        utilRepository.lagSaksforhold()
        utilRepository.opprettTaskInstance(medSakId = true)

        val oppgave = oppgaveRepository.hentOppgaver(identer).single()
        oppgave.assertOppgaveFelter(målgruppeMedVerdi = true)
    }

    @Test
    fun `har målgruppe null hvis ikke caseContext er registrert`() {
        utilRepository.lagSaksforhold()
        utilRepository.opprettTaskInstance(medSakId = false)

        val oppgave = oppgaveRepository.hentOppgaver(identer).single()
        oppgave.assertOppgaveFelter(målgruppeMedVerdi = false)
    }

    @Test
    fun `skal ikke få noen treff med ident som ikke eksisterer`() {
        assertThat(oppgaveRepository.hentOppgaver(setOf("TS:123"))).isEmpty()
        assertThat(oppgaveRepository.hentOppgaver(identer)).isEmpty()
    }

    private fun Oppgave.assertOppgaveFelter(målgruppeMedVerdi: Boolean = true) {
        assertThat(id).isEqualTo(300)
        assertThat(tittel).isEqualTo("tittel")
        assertThat(kommentar).isEqualTo("kommentar")
        assertThat(fristFerdigstillelse).isEqualTo(LocalDate.of(2023, 2, 21))
        assertThat(benk).isEqualTo("4462")
        assertThat(opprettetTidspunkt).isEqualTo(LocalDateTime.of(2022, 2, 20, 19, 1))

        if (målgruppeMedVerdi) {
            assertThat(målgruppe).isEqualTo(Målgruppe.NEDSATT__ARBEIDSEVNE)
        } else {
            assertThat(målgruppe).isNull()
        }
    }
}
