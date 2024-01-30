package no.nav.tilleggsstonader.arena.felles

import java.sql.ResultSet

object SqlUtils {

    fun ResultSet.getIntOrNull(name: String): Int? {
        val verdi = getInt(name)
        return if (this.wasNull()) null else verdi
    }
}
