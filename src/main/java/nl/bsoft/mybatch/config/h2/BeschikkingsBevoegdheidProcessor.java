package nl.bsoft.mybatch.config.h2;


import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.Counter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheidH2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public @Data
class BeschikkingsBevoegdheidProcessor implements ItemProcessor<BeschikkingsBevoegdheid, BeschikkingsBevoegdheidH2> {

    private MeterRegistry registry;

    static final Counter gegevenCounter = Counter.build()
            .name("nl_bsoft_mybatch_beschikkingsbevoegdheid")
            .help("Totaal aantal verwerkte beschikkingsbevoegdheid records")
            .register();

    public BeschikkingsBevoegdheidProcessor(final MeterRegistry registry) {
        this.registry = registry;
        //gegevenCounter = this.registry.counter("nl.bsoft.mybatch", "aantal", "beschikkingsbevoegdheid");
    }

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

        gegevenCounter.inc();

        return beschikkingsBevoegdheidH2;
    }
}
