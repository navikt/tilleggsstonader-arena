package no.nav.tilleggsstonader.arena

import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * Denne settes til en fixed port for å kunne bruke samme port som familie-dokument
 */
private val mockOauth2ServerPort: String = "11588"

@EnableMockOAuth2Server
class AppLocal : App()

fun main(args: Array<String>) {
    SpringApplicationBuilder(AppLocal::class.java)
        // .initializers(DbContainerInitializer())
        .profiles(
            "local",
        )
        .properties(mapOf("mock-oauth2-server.port" to mockOauth2ServerPort))
        .run(*args)
}
