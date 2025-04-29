package no.nav.tilleggsstonader.arena.infrastruktur.config

import org.springframework.http.HttpStatus

open class ApiFeil(
    val feil: String,
    val frontendFeilmelding: String = feil,
    val httpStatus: HttpStatus,
) : RuntimeException(feil) {
    constructor(feil: String, httpStatus: HttpStatus) :
        this(feil = feil, frontendFeilmelding = feil, httpStatus = httpStatus)
}
