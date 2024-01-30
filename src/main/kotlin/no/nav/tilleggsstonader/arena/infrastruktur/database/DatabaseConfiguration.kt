package no.nav.tilleggsstonader.arena.infrastruktur.database

import no.nav.tilleggsstonader.arena.felles.KodeArena
import no.nav.tilleggsstonader.arena.sak.Målgruppe
import no.nav.tilleggsstonader.arena.sak.StatusSak
import no.nav.tilleggsstonader.arena.vedtak.Rettighet
import no.nav.tilleggsstonader.arena.vedtak.StatusVedtak
import no.nav.tilleggsstonader.arena.vedtak.TypeVedtak
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource
import kotlin.reflect.KClass

@Configuration
@EnableJdbcRepositories("no.nav.tilleggsstonader.arena")
class DatabaseConfiguration : AbstractJdbcConfiguration() {

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    override fun jdbcCustomConversions(): JdbcCustomConversions {
        return JdbcCustomConversions(
            listOf(
                StatusSakConverter(),
                MålgruppeConverter(),
                StatusVedtakConverter(),
                TypeVedtakConverter(),
                RettighetConverter(),
            ),
        )
    }
}

@ReadingConverter
abstract class EnumConverter<T>(clazz: KClass<T>) : Converter<String, T> where T : Enum<T>, T : KodeArena {

    private val arenaKoder = clazz.java.enumConstants.associateBy { it.kodeArena }
    override fun convert(verdi: String): T {
        return arenaKoder[verdi] ?: error("Finner ikke mapping for $verdi")
    }
}

@ReadingConverter
class StatusSakConverter : EnumConverter<StatusSak>(StatusSak::class)

@ReadingConverter
class MålgruppeConverter : EnumConverter<Målgruppe>(Målgruppe::class)

@ReadingConverter
class StatusVedtakConverter : EnumConverter<StatusVedtak>(StatusVedtak::class)

@ReadingConverter
class TypeVedtakConverter : EnumConverter<TypeVedtak>(TypeVedtak::class)

@ReadingConverter
class RettighetConverter : EnumConverter<Rettighet>(Rettighet::class)
