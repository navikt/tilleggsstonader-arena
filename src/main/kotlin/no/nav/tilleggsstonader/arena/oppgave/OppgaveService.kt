package no.nav.tilleggsstonader.arena.oppgave

import no.nav.tilleggsstonader.kontrakter.arena.oppgave.ArenaOppgaveDto
import org.springframework.stereotype.Service

@Service
class OppgaveService(
    private val oppgaveRepository: OppgaveRepository,
) {
    fun hentOppgaver(identer: Set<String>): List<ArenaOppgaveDto> {
        val identliste = identer.flatMap { ident ->
            listOf("TS:$ident", "TA:$ident")
        }
        return oppgaveRepository
            .hentOppgaver(identliste)
            .map { it.tilOppgaveArena() }
    }
}
