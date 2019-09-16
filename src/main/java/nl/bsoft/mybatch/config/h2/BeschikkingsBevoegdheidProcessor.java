package nl.bsoft.mybatch.config.h2;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheidH2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeschikkingsBevoegdheidProcessor implements ItemProcessor<BeschikkingsBevoegdheid, BeschikkingsBevoegdheidH2> {
    private static final Logger logger = LoggerFactory.getLogger(BeschikkingsBevoegdheidProcessor.class);

    @Override
    public BeschikkingsBevoegdheidH2 process(BeschikkingsBevoegdheid beschikkingsBevoegdheid) throws Exception {

        final String status = "PROCESSED";
        BeschikkingsBevoegdheidH2 beschikkingsBevoegdheidH2 = new BeschikkingsBevoegdheidH2();

        beschikkingsBevoegdheidH2.setCode(beschikkingsBevoegdheid.getCode());
        beschikkingsBevoegdheidH2.setDatumTot(beschikkingsBevoegdheid.getDatumTot());
        beschikkingsBevoegdheidH2.setDatumVanAf(beschikkingsBevoegdheid.getDatumVanAf());
        beschikkingsBevoegdheidH2.setProcesstatus(status);
        beschikkingsBevoegdheidH2.setToelichting(beschikkingsBevoegdheid.getToelichting());
        beschikkingsBevoegdheidH2.setWaarde(beschikkingsBevoegdheid.getWaarde());

        return beschikkingsBevoegdheidH2;
    }
}
