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
        t.description tittel, t.note kommentar, t.duedate frist_ferdigstillelse, 
        t.username benk, t.reg_dato opprettet_tidspunkt,
        sf.maalgruppekode maalgruppe
        FROM taskinstance t
        LEFT JOIN variablebinding v ON v.instanceelementid = t.mainprocess_id AND v.variablename = 'caseContext'
        LEFT JOIN saksforhold sf ON sf.sak_id = v.storedobject
        WHERE t.casecontext in (:casecontext)
    """,
    )
    fun hentOppgaver(casecontext: Collection<String>): List<Oppgave>
}
