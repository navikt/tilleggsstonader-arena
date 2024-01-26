package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.felles.SqlUtils.getIntOrNull
import no.nav.tilleggsstonader.kontrakter.felles.Stønadstype
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class VedtakRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    // language=PostgreSQL
    val queryVedtak = """
       SELECT 
         v.vedtak_id,v.sak_id,v.vedtakstatuskode,v.vedtaktypekode,
         v.reg_dato,v.reg_user,v.mod_dato,v.mod_user,v.utfallkode,
         v.rettighetkode,v.dato_mottatt,v.vedtak_id_relatert,v.person_id,
         v.brukerid_beslutter,v.dato_innstilt,v.er_utland,v.fra_dato,v.til_dato, 
         v.totalbelop 
       FROM vedtak v
         JOIN person p ON p.person_id = v.person_id
       WHERE p.fodselsnr IN (:identer)
         AND v.rettighetkode IN (:rettighettyper)
    """.trimIndent()

    fun hentVedtak(identer: Set<String>, stønadstype: Stønadstype): List<Vedtak> {
        val values =
            MapSqlParameterSource()
                .addValue("identer", identer)
                .addValue("rettighettyper", stønadstype.rettigheter().map { it.kode })
        return jdbcTemplate.query(
            queryVedtak,
            values,
            vedtakMapper,
        )
    }
}

private val vedtakMapper: (rs: ResultSet, rowNum: Int) -> Vedtak? = { rs, _ ->
    Vedtak(
        vedtakId = rs.getInt("vedtak_id"),
        sakId = rs.getInt("sak_id"),
        vedtakstatus = rs.getString("vedtakstatuskode").let { StatusVedtak.fraKode(it) },
        vedtaktype = rs.getString("vedtaktypekode").let { TypeVedtak.fraKode(it) },
        registrertDato = rs.getDate("reg_dato").toLocalDate(),
        registrertAv = rs.getString("reg_user"),
        modifisertDato = rs.getDate("mod_dato").toLocalDate(),
        modifisertAv = rs.getString("mod_user"),
        utfall = rs.getString("utfallkode")?.let { UtfallVedtak.valueOf(it) },
        rettighet = rs.getString("rettighetkode").let { Rettighet.fraKode(it) },
        datoMottatt = rs.getDate("dato_mottatt").toLocalDate(),
        vedtakIdRelatert = rs.getIntOrNull("vedtak_id_relatert"),
        personId = rs.getInt("person_id"),
        brukerIdBeslutter = rs.getString("brukerid_beslutter"),
        datoInnstilt = rs.getDate("dato_innstilt")?.toLocalDate(),
        erUtland = rs.getString("er_utland").let { it == "J" },
        fom = rs.getDate("fra_dato")?.toLocalDate(),
        tom = rs.getDate("til_dato")?.toLocalDate(),
        totalbeløp = rs.getIntOrNull("totalbelop"),
    )
}
