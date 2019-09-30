package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.csv.Gegeven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GegevensCsvFieldSetMapper implements FieldSetMapper<Gegeven> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensCsvFieldSetMapper.class);

    @Override
    public Gegeven mapFieldSet(FieldSet fieldSet) throws BindException {
        Gegeven gegeven = new Gegeven();

        String string;
        LocalDate date;

        // code
        gegeven.setCode(fieldSet.readLong(0));

        // waarde
        string = fieldSet.readString(1);
        if (string != null) {
            gegeven.setWaarde(fieldSet.readString(1));
        }

        // datumvanaf
        string = fieldSet.readString(2);
        if ((string != null) && (string.length() > 0)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(string, formatter);
            gegeven.setDatumVanAf(date);
        }

        // datumtot
        string = fieldSet.readString(3);
        if ((string != null) && (string.length() > 0)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(string, formatter);
            gegeven.setDatumTot(date);
        }

        // opmerking
        string = fieldSet.readString(4);
        if (string != null) {
            gegeven.setToelichting(fieldSet.readString(4));
        }

        return gegeven;
    }
}
