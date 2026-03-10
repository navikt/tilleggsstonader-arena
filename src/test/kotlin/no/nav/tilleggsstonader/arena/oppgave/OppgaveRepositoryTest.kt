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

    val identerTS = setOf("TS:${TestConstants.FNR}")
    val identerTA = setOf("TA:${TestConstants.FNR}")

    @Test
    fun `skal mappe felter`() {
        utilRepository.opprettTaskInstance()
        utilRepository.opprettTaskInstanceTA()

        val oppgave = oppgaveRepository.hentOppgaver(identerTS).single()
        oppgave.assertOppgaveFelter()

        val oppgaveTA = oppgaveRepository.hentOppgaver(identerTA).single()
        oppgaveTA.assertOppgaveFelterTA()
    }

    @Test
    fun `skal ikke få noen treff med ident som ikke eksisterer`() {
        assertThat(oppgaveRepository.hentOppgaver(setOf("TS:123"))).isEmpty()
        assertThat(oppgaveRepository.hentOppgaver(identerTS)).isEmpty()
        assertThat(oppgaveRepository.hentOppgaver(setOf("TA:123"))).isEmpty()
        assertThat(oppgaveRepository.hentOppgaver(identerTA)).isEmpty()
    }

    @Test
    fun `kommentar kan være null`() {
        utilRepository.opprettTaskInstance(kommentar = null)
        utilRepository.opprettTaskInstanceTA(kommentar = null)

        val oppgave = oppgaveRepository.hentOppgaver(identerTS).single()
        assertThat(oppgave.kommentar).isNull()

        val oppgaveTA = oppgaveRepository.hentOppgaver(identerTA).single()
        assertThat(oppgaveTA.kommentar).isNull()
    }

    private fun Oppgave.assertOppgaveFelter() {
        assertThat(id).isEqualTo(300)
        assertThat(tittel).isEqualTo("tittel")
        assertThat(kommentar).isEqualTo("kommentar")
        assertThat(benk).isEqualTo("4462")
        assertThat(opprettetTidspunkt).isEqualTo(LocalDateTime.of(2022, 2, 20, 19, 1))
    }

    private fun Oppgave.assertOppgaveFelterTA() {
        assertThat(id).isEqualTo(500)
        assertThat(tittel).isEqualTo("tittel TA")
        assertThat(kommentar).isEqualTo("kommentar TA")
        assertThat(benk).isEqualTo("5562")
        assertThat(opprettetTidspunkt).isEqualTo(LocalDateTime.of(2024, 3, 20, 15, 1))
    }
}
