package nl.bsoft.mybatch.config.postgres;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.utils.DateUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.util.Date;

@Slf4j
public class GegevensCsvFieldSetMapper implements FieldSetMapper<Gegeven> {

    @Override
    public Gegeven mapFieldSet(FieldSet fieldSet) throws BindException {
        Gegeven gegeven = new Gegeven();

        String string;
        Date date;
        LocalDate localDate;

        final int CODE = 0;
        final int WAARDE = 1;
        final int DATUMVANAF = 2;
        final int DATUMTOT = 3;
        final int TOELICHTING = 4;
        final String dateFormat = "yyyy-MM-dd";

        // code
        gegeven.setCode(fieldSet.readLong(CODE));

        // waarde
        string = fieldSet.readString(WAARDE);
        if (string != null) {
            gegeven.setWaarde(string);
        }

        // datumvanaf
        date = fieldSet.readDate(DATUMVANAF, dateFormat);
        if (date != null) {
            gegeven.setDatumVanAf(DateUtils.asLocalDate(date));
        }

        // datumtot
        date = fieldSet.readDate(DATUMTOT, dateFormat);
        if (date != null) {
            gegeven.setDatumTot(DateUtils.asLocalDate(date));
        }

        // opmerking
        string = fieldSet.readString(TOELICHTING);
        if (string != null) {
            gegeven.setToelichting(string);
        }

        return gegeven;
    }
}
