package no.nav.tilleggsstonader.arena.oppgave

import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.tilleggsstonader.kontrakter.arena.oppgave.ArenaOppgaveDto
import no.nav.tilleggsstonader.kontrakter.felles.IdenterRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/oppgave"])
@ProtectedWithClaims(issuer = "azuread")
class OppgaveController(
    private val oppgaveService: OppgaveService,
) {
    @PostMapping
    fun hentOppgaver(
        @RequestBody request: IdenterRequest,
    ): List<ArenaOppgaveDto> = oppgaveService.hentOppgaver(request.identer)
}
