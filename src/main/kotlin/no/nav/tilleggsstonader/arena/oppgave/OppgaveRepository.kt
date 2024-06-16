package no.nav.tilleggsstonader.arena.oppgave

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OppgaveRepository : CrudRepository<Oppgave, Int> {

    @Query(
        """
    SELECT 
      substr(o.casecontext, 4) AS ident
    FROM taskinstance o
    WHERE o.casecontext = 'TS:' || :ident
    """,
    )
    fun finnOppgaver(ident: String): List<Oppgave>
}
