package no.nav.tilleggsstonader.arena.oppgave

import no.nav.tilleggsstonader.arena.IntegrationTest
import no.nav.tilleggsstonader.arena.TestConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class OppgaveRepositoryTest : IntegrationTest() {

    @Autowired
    lateinit var oppgaveRepository: OppgaveRepository

    val identer = setOf("TS:${TestConstants.FNR}")

    @Test
    fun `skal mappe felter`() {
        utilRepository.opprettTaskInstance()

        val oppgave = oppgaveRepository.hentOppgaver(identer).single()
        oppgave.assertOppgaveFelter()
    }

    @Test
    fun `har målgruppe null hvis ikke caseContext er registrert`() {
        utilRepository.opprettTaskInstance()

        val oppgave = oppgaveRepository.hentOppgaver(identer).single()
        oppgave.assertOppgaveFelter()
    }

    @Test
    fun `skal ikke få noen treff med ident som ikke eksisterer`() {
        assertThat(oppgaveRepository.hentOppgaver(setOf("TS:123"))).isEmpty()
        assertThat(oppgaveRepository.hentOppgaver(identer)).isEmpty()
    }

    private fun Oppgave.assertOppgaveFelter() {
        assertThat(id).isEqualTo(300)
        assertThat(tittel).isEqualTo("tittel")
        assertThat(kommentar).isEqualTo("kommentar")
        assertThat(benk).isEqualTo("4462")
        assertThat(opprettetTidspunkt).isEqualTo(LocalDateTime.of(2022, 2, 20, 19, 1))
    }
}
