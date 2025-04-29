package no.nav.tilleggsstonader.arena.infrastruktur.config

import no.nav.tilleggsstonader.libs.log.SecureLogger.secureLogger
import org.slf4j.LoggerFactory
import org.springframework.core.NestedExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ApiFeil::class)
    fun handleThrowable(feil: ApiFeil): ProblemDetail {
        val metodeSomFeiler = finnMetodeSomFeiler(feil)
        secureLogger.info("En håndtert feil har oppstått(${feil.httpStatus}): ${feil.frontendFeilmelding}", feil)
        logger.info(
            "En håndtert feil har oppstått(${feil.httpStatus}) " +
                "metode=$metodeSomFeiler exception=${rootCause(feil)}: ${feil.message} ",
        )
        return ProblemDetail.forStatusAndDetail(feil.httpStatus, feil.frontendFeilmelding)
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(throwable: Throwable): ProblemDetail {
        val responseStatus =
            throwable::class
                .annotations
                .find { it is ResponseStatus }
                ?.let { it as ResponseStatus }
                ?.value
                ?: HttpStatus.INTERNAL_SERVER_ERROR
        logger.error("Ukjent feil status=${responseStatus.value()}")
        secureLogger.error("Ukjent feil status=${responseStatus.value()}", throwable)
        return ProblemDetail.forStatusAndDetail(responseStatus, "Ukjent feil")
    }

    fun finnMetodeSomFeiler(e: Throwable): String {
        val firstElement =
            e.stackTrace.firstOrNull {
                it.className.startsWith("no.nav.tilleggsstonader.arena") &&
                    !it.className.contains("$") &&
                    !it.className.contains("InsertUpdateRepositoryImpl")
            }
        if (firstElement != null) {
            val className = firstElement.className.split(".").lastOrNull()
            return "$className::${firstElement.methodName}(${firstElement.lineNumber})"
        }
        return e.cause?.let { finnMetodeSomFeiler(it) } ?: "(Ukjent metode som feiler)"
    }

    private fun rootCause(throwable: Throwable): String = throwable.getMostSpecificCause().javaClass.simpleName

    private fun Throwable.getMostSpecificCause(): Throwable = NestedExceptionUtils.getMostSpecificCause(this)
}
