package nl.bsoft.mybatch.config.postgres;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
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
    private Timer gegevenTimer;
    private PrometheusMeterRegistry prometheusRegistry;

    @Autowired
    public GegevensProcessor(PrometheusMeterRegistry prometheusRegistry) {
        this.prometheusRegistry = prometheusRegistry;
        this.gegevenCounter = Counter.builder("process")
                //.baseUnit("gegevens")
                .tags("bron", "postgres")
                .description("Aantal verwerkte records")
                .register(prometheusRegistry);
        this.gegevenTimer = Timer.builder("processTimer")
                .tag("bron", "postgres")
                .description("Snelheid verwerking van een record")
                .register(prometheusRegistry);
    }

    @Override
    public BeschikkingsBevoegdheid process(Gegeven gegeven) throws Exception {
        this.gegevenCounter.increment();
        Timer.Sample pgSampleTime = Timer.start(this.prometheusRegistry);

        BeschikkingsBevoegdheid beschikkingsBevoegdheid = null;
        try {
            beschikkingsBevoegdheid = new BeschikkingsBevoegdheid();
            beschikkingsBevoegdheid.setCode(gegeven.getCode());
            beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
            beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
            beschikkingsBevoegdheid.setWaarde(gegeven.getWaarde());
            beschikkingsBevoegdheid.setToelichting(gegeven.getToelichting());
        } finally {
            pgSampleTime.stop(this.gegevenTimer);
        }

        return beschikkingsBevoegdheid;
    }
}
