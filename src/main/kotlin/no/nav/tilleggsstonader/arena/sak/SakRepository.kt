package no.nav.tilleggsstonader.arena.sak

import no.nav.tilleggsstonader.kontrakter.arena.sak.StatusSak
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SakRepository : CrudRepository<Sak, Int> {

    @Query(
        """
    SELECT 
     count(*) antall
    FROM sak s
      JOIN person p ON p.person_id = s.objekt_id
    WHERE p.fodselsnr IN (:identer)
    """,
    )
    fun antallSaker(identer: Collection<String>): Int

    @Query(
        """
    SELECT 
     s.sak_id,p.person_id,
     s.reg_dato,s.reg_user,s.mod_dato,s.mod_user,s.dato_avsluttet,
     s.sakstatuskode,s.status_endret,
     sf.saksforhold_id,sf.maalgruppekode,sf.dato_fra,sf.dato_til,sf.kilde
    FROM sak s
      JOIN person p ON p.person_id = s.objekt_id
      LEFT JOIN saksforhold sf ON sf.sak_id = s.sak_id
    WHERE p.fodselsnr IN (:identer)
    """,
    )
    fun finnSaker(identer: Collection<String>): List<Sak>

    @Query(
        """
    SELECT 
     count(*)
    FROM sak s
      JOIN person p ON p.person_id = s.objekt_id
      LEFT JOIN vedtak v ON v.sak_id = s.sak_id
    WHERE p.fodselsnr IN (:identer)
      AND v.vedtak_id is null
      AND s.sakstatuskode IN (:status) 
    """,
    )
    fun antallSakerUtenVedtak(identer: Collection<String>, status: Collection<String>): Int
}

val SAK_AKTIVE_STATUSER = setOf(StatusSak.AKTIV, StatusSak.OPPRETTET).map { it.kodeArena }
