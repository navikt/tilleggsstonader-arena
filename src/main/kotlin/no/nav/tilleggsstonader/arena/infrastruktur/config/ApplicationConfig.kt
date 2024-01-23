package no.nav.tilleggsstonader.arena.infrastruktur.config

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import no.nav.tilleggsstonader.libs.log.filter.LogFilterConfiguration
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootConfiguration
@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
@Import(
    LogFilterConfiguration::class,
)
@EnableScheduling
class ApplicationConfig
