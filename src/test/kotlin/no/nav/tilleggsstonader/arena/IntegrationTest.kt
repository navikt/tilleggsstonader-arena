package no.nav.tilleggsstonader.arena

import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import no.nav.tilleggsstonader.libs.test.fnr.FnrGenerator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import java.util.UUID

@Configuration
class DefaultRestTemplateConfiguration {
    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder) = restTemplateBuilder.build()
}

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [App::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(
    "integrasjonstest",
)
@EnableMockOAuth2Server
abstract class IntegrationTest {
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected lateinit var restTemplate: RestTemplate
    protected val headers = HttpHeaders()

    @LocalServerPort
    private var port: Int? = 0

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private lateinit var mockOAuth2Server: MockOAuth2Server

    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Autowired
    protected lateinit var utilRepository: UtilRepository

    @AfterEach
    fun tearDown() {
        headers.clear()
        resetDatabase()
    }

    private fun resetDatabase() {
        listOf("person", "saksforhold", "spesialutbetaling", "sak", "vedtak", "taskinstance", "variablebinding", "ram_aktivitet").forEach {
            try {
                jdbcTemplate.update("DELETE FROM $it", emptyMap<String, String>())
            } catch (e: Exception) {
                throw RuntimeException("Feilet sletting av $it", e)
            }
        }
    }

    protected fun localhost(path: String): String = "$LOCALHOST$port/$path"

    protected fun søkerBearerToken(personident: String = FnrGenerator.generer()): String = mockOAuth2Server.token(subject = personident)

    private fun MockOAuth2Server.token(
        subject: String,
        issuerId: String = "tokenx",
        clientId: String = UUID.randomUUID().toString(),
        audience: String = "tilleggsstonader-app",
        claims: Map<String, Any> =
            mapOf(
                "acr" to "Level4",
                "pid" to subject,
            ),
    ): String =
        this
            .issueToken(
                issuerId,
                clientId,
                DefaultOAuth2TokenCallback(
                    issuerId = issuerId,
                    subject = subject,
                    audience = listOf(audience),
                    claims = claims,
                    expiry = 3600,
                ),
            ).serialize()

    companion object {
        private const val LOCALHOST = "http://localhost:"
    }
}
