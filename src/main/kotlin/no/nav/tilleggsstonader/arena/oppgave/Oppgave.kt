package no.nav.tilleggsstonader.arena.oppgave

import java.time.LocalDate

/**
 * https://confluence.adeo.no/display/ARENA/Arena+-+Datamodell+-+Oppgavebenk
 * @param ident mappes i [OppgaveRepository] då den inneholder et prefix som er unødvendig å ta med ut
 */
data class Oppgave(
    val ident: String,
    /*val description: String?,
    val note: String?,
    val priority: Int,
    val duedate: LocalDate?,*/
)
