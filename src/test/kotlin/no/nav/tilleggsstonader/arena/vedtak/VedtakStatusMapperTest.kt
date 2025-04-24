package no.nav.tilleggsstonader.arena.vedtak

import no.nav.tilleggsstonader.arena.vedtak.VedtakStatusMapper.tilVedtakStatus
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.Rettighet
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.StatusVedtak
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.TypeVedtak
import no.nav.tilleggsstonader.kontrakter.arena.vedtak.UtfallVedtak
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDate.now

class VedtakStatusMapperTest {
    private val alleUtfall = UtfallVedtak.entries + null

    @Nested
    inner class HarVedtak {
        @Test
        fun `er uavhengig utfall`() {
            alleUtfall.forEach {
                val vedtakStatus = tilVedtakStatus(listOf(vedtak(utfall = it)))
                assertThat(vedtakStatus.harVedtak).isTrue
            }
        }

        @Test
        fun `har ikke vedtak hvis listen er tom`() {
            val vedtakStatus = tilVedtakStatus(emptyList())
            assertThat(vedtakStatus.harVedtak).isFalse
        }
    }

    @Nested
    inner class HarAktivtVedtak {
        @Test
        fun `har kun aktivt vedtak hvis utfall er JA og tom er større enn dagens dato`() {
            val vedtak =
                vedtak(
                    utfall = UtfallVedtak.JA,
                    tom = now().plusDays(1),
                    datoInnstilt = now(),
                )
            val vedtakStatus = tilVedtakStatus(listOf(vedtak))
            assertThat(vedtakStatus.harAktivtVedtak).isTrue
        }

        @Test
        fun `vedtak må være innstilte for at man skal ha et aktivt vedtak`() {
            val vedtak = vedtak(utfall = UtfallVedtak.JA, tom = now().plusDays(1), datoInnstilt = null)
            val vedtakStatus = tilVedtakStatus(listOf(vedtak))
            assertThat(vedtakStatus.harAktivtVedtak).isFalse()
        }

        @Test
        fun `har ikke aktivt vedtak hvis utfall er JA og tom ikke er større enn dagens dato`() {
            listOf(now().minusDays(1), null).forEach { tom ->
                val vedtakStatus =
                    tilVedtakStatus(listOf(vedtak(utfall = UtfallVedtak.JA, tom = now().minusDays(1))))
                assertThat(vedtakStatus.harAktivtVedtak).isFalse
            }
        }

        @Test
        fun `har ikke aktivt vedtak utfall er annet enn JA`() {
            val datoer = listOf(now().minusDays(1), now().plusDays(1), null)
            alleUtfall.filter { it != UtfallVedtak.JA }.forEach { utfall ->
                datoer.forEach { tom ->
                    val vedtakStatus = tilVedtakStatus(listOf(vedtak(utfall = utfall, tom = tom)))
                    assertThat(vedtakStatus.harAktivtVedtak).isFalse
                }
            }
        }
    }

    @Nested
    inner class HarVedtakUtenUtfall {
        @Test
        fun `true hvis utfall er null`() {
            val vedtakStatus = tilVedtakStatus(listOf(vedtak(utfall = null)))
            assertThat(vedtakStatus.harVedtakUtenUtfall).isTrue
        }

        @Test
        fun `true hvis utfall er annet enn AVBRUTT og dato instillt er null`() {
            UtfallVedtak.entries
                .filter { it != UtfallVedtak.AVBRUTT }
                .forEach { utfall ->
                    val vedtakStatus = tilVedtakStatus(listOf(vedtak(utfall = utfall, datoInnstilt = null)))
                    assertThat(vedtakStatus.harVedtakUtenUtfall).isTrue
                }
        }

        @Test
        fun `false hvis utfall er annet enn AVBRUTT og dato instillt ikke er null`() {
            UtfallVedtak.entries
                .filter { it != UtfallVedtak.AVBRUTT }
                .forEach { utfall ->
                    val vedtakStatus = tilVedtakStatus(listOf(vedtak(utfall = utfall, datoInnstilt = now())))
                    assertThat(vedtakStatus.harVedtakUtenUtfall).isFalse
                }
        }

        @Test
        fun `false hvis utfall er avbrutt og dato instillt er null`() {
            val vedtakStatus = tilVedtakStatus(listOf(vedtak(utfall = UtfallVedtak.AVBRUTT, datoInnstilt = null)))
            assertThat(vedtakStatus.harVedtakUtenUtfall).isFalse
        }

        @Test
        fun `false hvis utfall er avbrutt og dato instillt ikke er null`() {
            val vedtakStatus = tilVedtakStatus(listOf(vedtak(utfall = UtfallVedtak.AVBRUTT, datoInnstilt = now())))
            assertThat(vedtakStatus.harVedtakUtenUtfall).isFalse
        }
    }

    @Nested
    inner class HarVedtakTom {
        @Test
        fun `skal kun telle de med utfall JA`() {
            val tom = LocalDate.of(2024, 1, 31)
            val vedtakStatus =
                tilVedtakStatus(
                    listOf(
                        vedtak(utfall = UtfallVedtak.JA, tom = tom, datoInnstilt = now()),
                        vedtak(utfall = UtfallVedtak.JA, tom = tom.plusDays(1), datoInnstilt = now()),
                        vedtak(utfall = UtfallVedtak.JA, tom = tom.minusDays(1), datoInnstilt = now()),
                    ),
                )
            assertThat(vedtakStatus.vedtakTom).isEqualTo(tom.plusDays(1))
        }

        @Test
        fun `vedtak må være innstilte for at man skal ha et innvilget vedtak`() {
            val vedtak = vedtak(utfall = UtfallVedtak.JA, tom = LocalDate.of(2024, 1, 31), datoInnstilt = null)
            val vedtakStatus = tilVedtakStatus(listOf(vedtak))
            assertThat(vedtakStatus.vedtakTom).isNull()
        }

        @Test
        fun `skal ikke telle vedtak som ikke har utfall JA`() {
            val tom = LocalDate.of(2024, 1, 31)
            alleUtfall.filter { it != UtfallVedtak.JA }.forEach { utfall ->
                val vedtakStatus =
                    tilVedtakStatus(
                        listOf(
                            vedtak(utfall = utfall, tom = tom),
                        ),
                    )
                assertThat(vedtakStatus.vedtakTom).isNull()
            }
        }
    }

    fun vedtak(
        vedtakId: Int = 1,
        sakId: Int = 1,
        vedtakstatus: StatusVedtak = StatusVedtak.AVSLUTTET,
        vedtaktype: TypeVedtak = TypeVedtak.NY_RETTIGHET,
        registrertDato: LocalDate = now(),
        registrertAv: String = "Julenissen",
        modifisertDato: LocalDate = now(),
        modifisertAv: String = "Julenissen",
        utfall: UtfallVedtak? = null,
        rettighet: Rettighet = Rettighet.TILSYN_BARN,
        datoMottatt: LocalDate = now(),
        vedtakIdRelatert: Int? = null,
        personId: Int = 1,
        brukerIdAnsvarlig: String? = null,
        brukerIdBeslutter: String? = null,
        datoInnstilt: LocalDate? = null,
        erUtland: String = "N",
        fom: LocalDate? = null,
        tom: LocalDate? = null,
        totalbeløp: Int? = 100,
        begrunnelse: String? = "begrunnelse",
    ) = Vedtak(
        vedtakId = vedtakId,
        sakId = sakId,
        vedtakstatus = vedtakstatus,
        vedtaktype = vedtaktype,
        registrertDato = registrertDato,
        registrertAv = registrertAv,
        modifisertDato = modifisertDato,
        modifisertAv = modifisertAv,
        utfall = utfall,
        rettighet = rettighet,
        datoMottatt = datoMottatt,
        vedtakIdRelatert = vedtakIdRelatert,
        personId = personId,
        brukerIdAnsvarlig = brukerIdAnsvarlig,
        brukerIdBeslutter = brukerIdBeslutter,
        datoInnstilt = datoInnstilt,
        erUtland = erUtland,
        fom = fom,
        tom = tom,
        totalbeløp = totalbeløp,
        begrunnelse = begrunnelse,
    )
}
