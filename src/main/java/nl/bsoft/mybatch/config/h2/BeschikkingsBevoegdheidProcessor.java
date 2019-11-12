package nl.bsoft.mybatch.config.h2;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheidH2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;



@Slf4j
@Configuration
public @Data
class BeschikkingsBevoegdheidProcessor implements ItemProcessor<BeschikkingsBevoegdheid, BeschikkingsBevoegdheidH2> {
/*
    private MeterRegistry metrics;
    private Counter gegevenCounter;
*/

    @Autowired
    public BeschikkingsBevoegdheidProcessor( /* MeterRegistry metrics */) {
/*
        this.metrics = metrics;
        this.gegevenCounter = metrics.counter(BeschikkingsBevoegdheidProcessor.class.getName(), "aantal");

 */
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

        //gegevenCounter.increment();

        return beschikkingsBevoegdheidH2;
    }
}
