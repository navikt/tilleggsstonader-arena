package no.nav.tilleggsstonader.arena.oppgave

import no.nav.tilleggsstonader.kontrakter.arena.oppgave.ArenaOppgaveDto
import no.nav.tilleggsstonader.kontrakter.arena.sak.Målgruppe
import org.springframework.data.relational.core.mapping.Column
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * https://confluence.adeo.no/display/ARENA/Arena+-+Datamodell+-+Oppgavebenk
 *
 * @param benk er benken oppgaven ligger i, kan være personlig eller:
 * 4462 = Tilleggsstønad INN
 * 4463 = Tilleggsstønad Klar
 * 4464 = Tilleggsstønad Vent
 * 9958 = SIAMO (usikker på hva det er)
 *
 * @param målgruppe eksisterer ikke på alle type oppgaver. Finnes blant annet på søknader som blitt automatisk journalførte
 */
data class Oppgave(
    val tittel: String,
    val kommentar: String,
    val fristFerdigstillelse: LocalDate,
    val benk: String,
    val opprettetTidspunkt: LocalDateTime,
    @Column("maalgruppe")
    val målgruppe: Målgruppe?,
)

/**
 * @param benk settes hvis det finnes mapping eller er 4 tall
 * @param tildelt mappes hvis benk er null
 */
fun Oppgave.tilOppgaveArena(): ArenaOppgaveDto {
    val navnBenk = mapNavnBenk(benk)
    return ArenaOppgaveDto(
        tittel = tittel,
        kommentar = kommentar,
        fristFerdigstillelse = fristFerdigstillelse,
        benk = navnBenk,
        tildelt = benk.takeIf { navnBenk == null },
        opprettetTidspunkt = opprettetTidspunkt,
        målgruppe = målgruppe,
    )
}

private val REGEX_4_TALL = """^\d{4}$""".toRegex()
private val MAPPING_BENK = mapOf(
    "4462" to "Inn",
    "4463" to "Klar",
    "4464" to "Vent",
    "9958" to "SIAMO",
)

/**
 * Mapper navn til benk hvis vi har mapping
 * Hvis ikke brukes benk hvis den inneholder 4 sifrer
 */
private fun mapNavnBenk(benk: String): String? =
    MAPPING_BENK[benk]
        ?: benk.takeIf { it.matches(REGEX_4_TALL) }
