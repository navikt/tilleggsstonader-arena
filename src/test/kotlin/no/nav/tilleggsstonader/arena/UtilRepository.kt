package no.nav.tilleggsstonader.arena

import no.nav.tilleggsstonader.kontrakter.arena.sak.StatusSak
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UtilRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun lagPerson() {
        jdbcTemplate.update(
            """
            Insert into person (PERSON_ID,
                          FODSELSDATO,
                          STATUS_DNR,
                          PERSONNR,
                          FODSELSNR,
                          ETTERNAVN,
                          FORNAVN, DATO_FRA,
                          STATUS_SAMTYKKE, DATO_SAMTYKKE, VERNEPLIKTKODE, MAALFORM, LANDKODE_STATSBORGER, KONTONUMMER,
                          STATUS_BILDISP, FORMIDLINGSGRUPPEKODE, VIKARGRUPPEKODE, KVALIFISERINGSGRUPPEKODE,
                          RETTIGHETSGRUPPEKODE, REG_DATO, REG_USER, MOD_DATO, MOD_USER, AETATORGENHET, LONNSLIPP_EPOST,
                          DATO_OVERFORT_AMELDING, DATO_SIST_INAKTIV, BEGRUNNELSE_FORMIDLINGSGRUPPE, HOVEDMAALKODE,
                          BRUKERID_NAV_KONTAKT, FR_KODE, ER_DOED, PERSON_ID_STATUS, SPERRET_KOMMENTAR, SPERRET_TIL,
                          SPERRET_DATO, SPERRET_AV)
        values ('1',
        to_date('25.11.1969', 'DD.MM.RRRR'),
        'N',
        '35813',
        :fnr,
        'HVITKLØVER',
        'VOKSENDE',
        to_date('01.02.2019', 'DD.MM.RRRR'),
        'G',
        to_date('01.02.2019', 'DD.MM.RRRR'),
        null,
        'NO',
        'NO',
        null,
        null,
        null,
        'IVIK',
        'BATT',
        'IYT',
        to_date('01.02.2022', 'DD.MM.RRRR'),
        'ABC',
        to_date('18.03.2022', 'DD.MM.RRRR'),
        'ABC',
        '1102',
        null,
        to_date('01.02.2022', 'DD.MM.RRRR'),
        null,
        null,
        'SKAFFEA',
        null,
        null,
        null,
        'AKTIV',
        null,
        null,
        null,
        null);
        """,
            mapOf(
                "personId" to 1,
                "fnr" to TestConstants.FNR,
            ),
        )
    }

    fun lagSak(
        status: StatusSak = StatusSak.AKTIV,
    ) {
        jdbcTemplate.update(
            """
            INSERT INTO sak (SAK_ID,SAKSKODE,REG_DATO,REG_USER,MOD_DATO,MOD_USER,TABELLNAVNALIAS,
            OBJEKT_ID,AAR,LOPENRSAK,DATO_AVSLUTTET,SAKSTATUSKODE,ARKIVNOKKEL,AETATENHET_ARKIV,
            ARKIVHENVISNING,BRUKERID_ANSVARLIG,AETATENHET_ANSVARLIG,OBJEKT_KODE,STATUS_ENDRET,PARTISJON,ER_UTLAND) values 
            ('100',
            'TILSTOVER',
            to_date('20.02.2020','DD.MM.RRRR'),
            'ABC1234',
            to_date('20.02.2021','DD.MM.RRRR'),
            'GRENSESN',
            'PERS',
            :personId,
            '2016',
            '772969',
            null,
            :status,
            null,
            null,
            null,
            'ABC1235',
            '4416',
            null,
            to_date('20.02.2022','DD.MM.RRRR'),
            null,
            'N'
            );
        """,
            mapOf(
                "personId" to 1,
                "status" to status.kodeArena,
            ),
        )
    }

    fun lagSaksforhold() {
        jdbcTemplate.update(
            """
            Insert into saksforhold (
            SAKSFORHOLD_ID,MAALGRUPPEKODE,AKTIVITET_ID,SAK_ID,PERSON_ID,DATO_FRA,DATO_TIL,
            KILDE,REG_DATO,REG_USER,MOD_DATO,MOD_USER,TILTAKDELTAKER_ID,HIST_TILTAKDELTAKER_ID) values 
            ('101',
            'NEDSARBEVN',
            :aktivitetId,
            :sakId,
            :personId,
            to_date('25.02.2021','DD.MM.RRRR'),
            to_date('25.02.2022','DD.MM.RRRR'),
            'BRUKERREGISTRERT',
            to_date('25.02.2021','DD.MM.RRRR'),
            'ABC4321',
            to_date('25.02.2021','DD.MM.RRRR'),
            'ABC4321',
            null,
            null);

        """,
            mapOf(
                "sakId" to "100",
                "personId" to 1,
                "aktivitetId" to "200",
            ),
        )
    }

    fun lagAktivitet(aktivitetkode: String = "JOBBKLUBB") {
        jdbcTemplate.update(
            """
               INSERT INTO ram_aktivitet (aktivitetkode, beskrivelse, aktivitet_id, aktivitetstatuskode, dato_fra, dato_til) VALUES 
               (
               :aktivitetkode,
               :beskrivelse,
               :aktivitetId,
               :statuskode,
                to_date('12.01.2016','DD.MM.RRRR'),
                to_date('12.01.2018','DD.MM.RRRR')
               ) 
            """,
            mapOf(
                "aktivitetkode" to aktivitetkode,
                "beskrivelse" to "En beskrivelse",
                "aktivitetId" to "200",
                "statuskode" to "GJENN",
            ),
        )
    }

    fun lagVedtak() {
        jdbcTemplate.update(
            """
            Insert into vedtak (VEDTAK_ID, SAK_ID, VEDTAKSTATUSKODE, VEDTAKTYPEKODE, REG_DATO, REG_USER, MOD_DATO, MOD_USER,
                    UTFALLKODE, BEGRUNNELSE, BRUKERID_ANSVARLIG, AETATENHET_BEHANDLER, AAR, LOPENRSAK, LOPENRVEDTAK,
                    RETTIGHETKODE, AKTFASEKODE, BREV_ID, TOTALBELOP, DATO_MOTTATT, VEDTAK_ID_RELATERT,
                    AVSNITTLISTEKODE_VALGT, PERSON_ID, BRUKERID_BESLUTTER, STATUS_SENSITIV, VEDLEGG_BETPLAN, PARTISJON,
                    OPPSUMMERING_SB2, DATO_UTFORT_DEL1, DATO_UTFORT_DEL2, OVERFORT_NAVI, FRA_DATO, TIL_DATO,
                    SF_OPPFOLGING_ID, STATUS_SOSIALDATA, KONTOR_SOSIALDATA, TEKSTVARIANTKODE, VALGT_BESLUTTER,
                    TEKNISK_VEDTAK, DATO_INNSTILT, ER_UTLAND)
values (:vedtakId,
        :sakId,
        'AVSLU',
        'O',
        to_date('12.01.2016','DD.MM.RRRR'),
        'ABC1234',
        to_date('20.02.2022','DD.MM.RRRR'),
        'GRENSESN',
        'JA',
        'Syntetisert rettighet',
        'ABC1235',
        '4402',
        '2016',
        '772969',
        '1',
        'TSOTILBARN',
        'IKKE',
        '30985685',
        '20130',
        to_date('14.01.2016','DD.MM.RRRR'),
        null,
        null,
        :personId,
        'AA1234',
        null,
        'N',
        null,
        null,
        null,
        null,
        null,
        to_date('19.01.2023','DD.MM.RRRR'),
        to_date('04.06.2023','DD.MM.RRRR'),
        null,
        'N',
        null,
        null,
        'AB1234',
        null,
        to_date('26.02.2022','DD.MM.RRRR'),
        'N');

        """,
            mapOf(
                "vedtakId" to 400,
                "sakId" to 100,
                "personId" to 1,
            ),
        )
    }

    fun lagVedtakFakta() {
        jdbcTemplate.update(
            "INSERT INTO vedtakfakta (vedtak_id, vedtakfaktakode, vedtakverdi) VALUES" +
                " (:vedtakId, :vedtakfaktakode, :vedtakverdi)",
            mapOf(
                "vedtakId" to 400,
                "vedtakfaktakode" to "AAPJUSTFD",
                "vedtakverdi" to "2023-01-01",
            ),
        )
    }

    fun lagVilkårsvurdering() {
        jdbcTemplate.update(
            "INSERT INTO vilkaarvurdering (vedtak_id, vilkaarkode, vilkaarstatuskode, vurdert_av) VALUES" +
                " (:vedtakId, :vilkaarkode, :vilkaarstatuskode, :vurdert_av)",
            mapOf(
                "vedtakId" to 400,
                "vilkaarkode" to "OPPHINST",
                "vilkaarstatuskode" to "J",
                "vurdert_av" to "Nissen",
            ),
        )
    }

    /**
     * Oppretter en oppgave
     */
    fun opprettTaskInstance(
        kommentar: String? = "kommentar",
    ) {
        val mainProcessId = 456
        jdbcTemplate.update(
            """            
                INSERT INTO taskinstance (ID,DESCRIPTION,NOTE,DUEDATE,MAINPROCESS_ID,CASECONTEXT,USERNAME,REG_DATO) VALUES 
            (300,
            'tittel',
            :kommentar,
            to_date('21.02.2023','DD.MM.RRRR'),
            :mainProcessId,
            'TS:' || :fnr,
            '4462',
            to_date('20.02.2022T19:01','DD.MM.RRRRTHH24:MI')
            );
        """,
            mapOf(
                "fnr" to TestConstants.FNR,
                "mainProcessId" to mainProcessId,
                "kommentar" to kommentar,
            ),
        )
    }
}
