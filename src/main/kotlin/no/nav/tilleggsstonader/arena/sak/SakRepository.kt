package no.nav.tilleggsstonader.arena.sak

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class SakRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    // language=PostgreSQL
    val querySak = """
    SELECT 
     s.sak_id,p.person_id,
     s.reg_dato,s.reg_user,s.mod_dato,s.mod_user,s.dato_avsluttet,
     s.sakstatuskode,s.status_endret,
     sf.saksforhold_id,sf.maalgruppekode,sf.dato_fra,sf.dato_til,sf.kilde
    FROM sak s
      JOIN person p ON p.person_id = s.objekt_id
      LEFT JOIN saksforhold sf ON sf.sak_id = s.sak_id
    """.trimIndent()

    fun hentSaker(identer: Set<String>): List<Sak> {
        val values = MapSqlParameterSource().addValue("identer", identer)
        return jdbcTemplate.query(
            """
                   $querySak
                     WHERE p.fodselsnr IN (:identer)
                   """,
            values,
            sakMapper,
        )
    }

    fun harAktiveSakerUtenVedtak(identer: Set<String>): Boolean {
        val values = MapSqlParameterSource()
            .addValue("identer", identer)
            .addValue("status", listOf(StatusSak.OPPRETTET, StatusSak.AKTIV).map { it.kode })
        return jdbcTemplate.query(
            """
                   $querySak
                   LEFT JOIN vedtak v ON v.sak_id = s.sak_id
                     WHERE p.fodselsnr IN (:identer)
                     AND v.vedtak_id is null
                     AND s.sakstatuskode IN (:status) 
                   """,
            values,
            sakMapper,
        ).isNotEmpty()
    }
}

private val sakMapper: (rs: ResultSet, rowNum: Int) -> Sak? = { rs, _ ->
    Sak(
        sakId = rs.getInt("sak_id"),
        personId = rs.getInt("person_id"),
        registrertDato = rs.getDate("reg_dato").toLocalDate(),
        registrertAv = rs.getString("reg_user"),
        modifisertDato = rs.getDate("mod_dato").toLocalDate(),
        modifisertAv = rs.getString("mod_user"),
        datoAvsluttet = rs.getDate("dato_avsluttet")?.toLocalDate(),
        status = rs.getString("sakstatuskode").let { StatusSak.fraKode(it) },
        statusEndret = rs.getDate("status_endret").toLocalDate(),
        saksforhold = saksforholdMapper(rs),
    )
}

private val saksforholdMapper: (rs: ResultSet) -> Saksforhold? = { rs ->
    if (rs.getString("saksforhold_id") == null) {
        null
    } else {
        Saksforhold(
            målgruppe = rs.getString("maalgruppekode")?.let { Målgruppe.fraKode(it) },
            fom = rs.getDate("dato_fra")?.toLocalDate(),
            tom = rs.getDate("dato_til")?.toLocalDate(),
            kilde = rs.getString("kilde"),
        )
    }
}
