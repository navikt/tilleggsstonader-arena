package no.nav.tilleggsstonader.arena.vedtak

import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.ArenaSakOgVedtakDto
import no.nav.tilleggsstonader.kontrakter.felles.IdenterRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/vedtak"])
@ProtectedWithClaims(issuer = "azuread")
class VedtakController(
    private val vedtakService: VedtakService,
) {
    @PostMapping
    fun hentVedtak(
        @RequestBody request: IdenterRequest,
    ): ArenaSakOgVedtakDto = SakOgVedtakDtoMapper.map(vedtakService.hentSakOgVedtak(request.identer))
}
