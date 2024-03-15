package no.nav.tilleggsstonader.arena.status

import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.tilleggsstonader.kontrakter.arena.ArenaStatusDto
import no.nav.tilleggsstonader.kontrakter.arena.ArenaStatusHarSakerDto
import no.nav.tilleggsstonader.kontrakter.felles.IdenterRequest
import no.nav.tilleggsstonader.kontrakter.felles.IdenterStønadstype
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/status"])
@ProtectedWithClaims(issuer = "azuread")
class StatusController(
    private val statusService: StatusService,
) {

    @PostMapping
    fun hentStatus(@RequestBody request: IdenterStønadstype): ArenaStatusDto {
        return statusService.hentStatus(request)
    }

    @PostMapping("har-saker")
    fun hentStatus(@RequestBody request: IdenterRequest): ArenaStatusHarSakerDto {
        return statusService.harSaker(request)
    }
}
