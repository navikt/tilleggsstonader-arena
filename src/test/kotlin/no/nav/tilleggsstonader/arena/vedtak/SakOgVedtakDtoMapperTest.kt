package no.nav.tilleggsstonader.arena.vedtak

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SakOgVedtakDtoMapperTest {
    @Test
    fun `skal mappe Vedtakfakta av type DATE til norsk format`() {
        val vedtakfakta = Vedtakfakta(1, "", "26-08-2024", "DATE")
        assertThat(SakOgVedtakDtoMapper.mapVerdi(vedtakfakta)).isEqualTo("26.08.2024")
    }
}
