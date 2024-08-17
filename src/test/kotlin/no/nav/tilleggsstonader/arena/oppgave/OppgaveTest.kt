package no.nav.tilleggsstonader.arena.oppgave

import no.nav.tilleggsstonader.kontrakter.arena.oppgave.ArenaOppgaveDto
import no.nav.tilleggsstonader.kontrakter.arena.sak.Målgruppe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class OppgaveTest {

    @Test
    fun `skal mappe felter`() {
        val oppgave = ArenaOppgaveDto(
            tittel = "tittel",
            kommentar = "kommentar",
            fristFerdigstillelse = LocalDate.of(2024, 1, 3),
            benk = "Inn",
            tildelt = null,
            opprettetTidspunkt = LocalDate.of(2024, 1, 3).atTime(10, 45),
            målgruppe = Målgruppe.NEDSATT__ARBEIDSEVNE,
        )
        assertThat(oppgave().tilOppgaveArena()).isEqualTo(oppgave)
    }

    @Nested
    inner class BenkOgTildelt {

        @Test
        fun `skal bruke benk sitt verdi hvis det er 4 sifrer og det ikke finnes mapping`() {
            val oppgave = oppgave(benk = "1111").tilOppgaveArena()
            assertThat(oppgave.benk).isEqualTo("1111")
            assertThat(oppgave.tildelt).isNull()
        }

        @Test
        fun `hvis benk er noe annet enn 4 sifrer så skal det settes til tildelt`() {
            val oppgave = oppgave(benk = "ABC10333").tilOppgaveArena()
            assertThat(oppgave.benk).isNull()
            assertThat(oppgave.tildelt).isEqualTo("ABC10333")
        }
    }

    private fun oppgave(
        tittel: String = "tittel",
        kommentar: String = "kommentar",
        fristFerdigstillelse: LocalDate = LocalDate.of(2024, 1, 3),
        benk: String = "4462",
        opprettetTidspunkt: LocalDateTime = LocalDate.of(2024, 1, 3).atTime(10, 45),
        målgruppe: Målgruppe? = Målgruppe.NEDSATT__ARBEIDSEVNE,
    ) = Oppgave(
        tittel = tittel,
        kommentar = kommentar,
        fristFerdigstillelse = fristFerdigstillelse,
        benk = benk,
        opprettetTidspunkt = opprettetTidspunkt,
        målgruppe = målgruppe,
    )
}
