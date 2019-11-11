package nl.bsoft.mybatch.config.postgres;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GegevensProcessor implements ItemProcessor<Gegeven, BeschikkingsBevoegdheid> {

    private Counter gegevenCounter;
    private MeterRegistry metrics;

    @Autowired
    public GegevensProcessor(MeterRegistry metrics) {
        this.metrics = metrics;
        gegevenCounter = metrics.counter(GegevensProcessor.class.getName(), "aantal");
    }

    @Override
    public BeschikkingsBevoegdheid process(Gegeven gegeven) throws Exception {

        BeschikkingsBevoegdheid beschikkingsBevoegdheid = new BeschikkingsBevoegdheid();
        beschikkingsBevoegdheid.setCode(gegeven.getCode());
        beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
        beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
        beschikkingsBevoegdheid.setWaarde(gegeven.getWaarde());
        beschikkingsBevoegdheid.setToelichting(gegeven.getToelichting());

        gegevenCounter.increment();

        return beschikkingsBevoegdheid;
    }
}
