package nl.bsoft.mybatch.config.postgres;


import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GegevensProcessor implements ItemProcessor<Gegeven, BeschikkingsBevoegdheid> {

    private MeterRegistry registry;

    static final Counter gegevenCounter = Counter.build()
            .name("nl_bsoft_mybatch_gegeven")
            .help("Totaal aantal ingelezen gegeven records")
            .register();

    public GegevensProcessor(final MeterRegistry registry) {
        this.registry = registry;
        //this.gegevenCounter.build().name("nl.bsoft.mybatch.gegeven").help("Totaal aantal ingelezen gegeven records").register();
//        gegevenCounter = this.registry.counter("nl.bsoft.mybatch", "aantal", "gegeven");
    }

    @Override
    public BeschikkingsBevoegdheid process(Gegeven gegeven) throws Exception {

        BeschikkingsBevoegdheid beschikkingsBevoegdheid = new BeschikkingsBevoegdheid();
        beschikkingsBevoegdheid.setCode(gegeven.getCode());
        beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
        beschikkingsBevoegdheid.setDatumTot(gegeven.getDatumTot());
        beschikkingsBevoegdheid.setWaarde(gegeven.getWaarde());
        beschikkingsBevoegdheid.setToelichting(gegeven.getToelichting());

        gegevenCounter.inc();

        return beschikkingsBevoegdheid;
    }
}
