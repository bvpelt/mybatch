package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class GegevensProcessor implements ItemProcessor<Gegeven, BeschikkingsBevoegdheid> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensProcessor.class);

    @Override
    public BeschikkingsBevoegdheid process(Gegeven gegeven) throws Exception {

        BeschikkingsBevoegdheid beschikkingsBevoegdheid = new BeschikkingsBevoegdheid();
        beschikkingsBevoegdheid.setCode(gegeven.getCode());
        beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
        beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
        beschikkingsBevoegdheid.setWaarde(gegeven.getWaarde());
        beschikkingsBevoegdheid.setToelichting(gegeven.getToelichting());

        return beschikkingsBevoegdheid;
    }
}
