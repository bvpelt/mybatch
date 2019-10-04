package nl.bsoft.mybatch.config.postgres;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class GegevensProcessor implements ItemProcessor<Gegeven, BeschikkingsBevoegdheid> {

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
