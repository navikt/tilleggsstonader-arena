package no.nav.tilleggsstonader.arena.oppgave

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.stereotype.Repository

@Repository
interface OppgaveRepository : org.springframework.data.repository.Repository<Oppgave, Int> {

    /**
     * @param casecontext er av verdien "TS:<ident>"
     */
    @Query(
        """
        SELECT 
        t.id, t.description tittel, t.note kommentar, 
        t.username benk, t.reg_dato opprettet_tidspunkt
        FROM taskinstance t
        WHERE t.casecontext in (:casecontext)
    """,
    )
    fun hentOppgaver(casecontext: Collection<String>): List<Oppgave>
}
