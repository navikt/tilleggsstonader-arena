package no.nav.tilleggsstonader.arena.oppgave

import no.nav.tilleggsstonader.arena.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class OppgaveRepositoryTest : IntegrationTest() {

    @Autowired
    lateinit var oppgaveRepository: OppgaveRepository

    @Test
    fun `skal kunne hente oppgaver for gitt ident`() {
        utilRepository.lagOppgave("TS:123")

        assertThat(oppgaveRepository.finnOppgaver("321")).isEmpty()
        val oppgaver = oppgaveRepository.finnOppgaver("123")
        assertThat(oppgaver).hasSize(1)
        with(oppgaver.single()) {
            assertThat(this.ident).isEqualTo("123")
        }
    }
}