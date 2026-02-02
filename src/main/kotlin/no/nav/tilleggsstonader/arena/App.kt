package no.nav.tilleggsstonader.arena

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration
import java.nio.file.Files
import kotlin.io.path.Path

@SpringBootApplication(
    exclude = [ErrorMvcAutoConfiguration::class],
)
class App

fun main(args: Array<String>) {
    lesDatabaseCredentialsFraFilHvisFinnes()
    runApplication<App>(*args)
}

private fun lesDatabaseCredentialsFraFilHvisFinnes() {
    val dbUserPath = System.getenv()["DB_USER_PATH"]
    val dbPasswordPath = System.getenv()["DB_PASSWORD_PATH"]
    if (dbUserPath != null && dbPasswordPath != null) {
        println("Setter properties DB_USERNAME fra $dbUserPath og DB_PASSWORD fra $dbPasswordPath")
        System.setProperty("DB_USERNAME", Files.readString(Path(dbUserPath)))
        System.setProperty("DB_PASSWORD", Files.readString(Path(dbPasswordPath)))
    }
}
