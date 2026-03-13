package no.nav.tilleggsstonader.arena.oppgave

import no.nav.tilleggsstonader.kontrakter.arena.oppgave.ArenaOppgaveDto
import no.nav.tilleggsstonader.libs.log.SecureLogger.secureLogger
import org.springframework.stereotype.Service

@Service
class OppgaveService(
    private val oppgaveRepository: OppgaveRepository,
) {
    fun hentOppgaver(identer: Set<String>): List<ArenaOppgaveDto> {
        val identliste =
            identer.flatMap { ident ->
                listOf("TS:$ident", "TA:$ident")
            }
        secureLogger.info("Gjør spørring mot taskInstance for identer=$identliste")
        return oppgaveRepository
            .hentOppgaver(identliste)
            .map { it.tilOppgaveArena() }
            .also {
                secureLogger.info(
                    "Fant ${it.size} oppgaver med titler ${
                        it.joinToString { oppgave -> oppgave.tittel }
                    }",
                )
            }
    }
}
