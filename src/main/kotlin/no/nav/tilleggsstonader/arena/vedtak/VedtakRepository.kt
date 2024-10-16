package no.nav.tilleggsstonader.arena.vedtak

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VedtakRepository : CrudRepository<Vedtak, Int> {

    @Query(
        """
    SELECT 
      v.vedtak_id,v.sak_id,v.vedtakstatuskode,v.vedtaktypekode,
      v.reg_dato,v.reg_user,v.mod_dato,v.mod_user,v.utfallkode,
      v.rettighetkode,v.dato_mottatt,v.vedtak_id_relatert,v.person_id,
      v.brukerid_beslutter,v.dato_innstilt,v.er_utland,v.fra_dato,v.til_dato, 
      v.totalbelop 
    FROM vedtak v
      JOIN person p ON p.person_id = v.person_id
    WHERE p.fodselsnr IN (:identer)
      AND v.rettighetkode IN (:rettigheter)
    """,
    )
    fun finnVedtak(identer: Set<String>, rettigheter: Collection<String>): List<Vedtak>

    @Query(
        """
    SELECT 
      vf.vedtak_id, vft.skjermbildetekst, vf.vedtakverdi, vft.oracletype
    FROM vedtakfakta vf
     JOIN vedtakfaktatype vft on vft.vedtakfaktakode = vf.vedtakfaktakode
    WHERE vedtak_id IN (:vedtakIder)
    """,
    )
    fun finnVedtakFakta(vedtakIder: Set<Int>): List<Vedtakfakta>
}
