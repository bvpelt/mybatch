package nl.bsoft.mybatch.config.postgres;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.postgres.repo.BeschikkingsBevoegdheidRepo;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Configuration
public
class GegevensPgWriter extends HibernateItemWriter<BeschikkingsBevoegdheid> {

    private PrometheusMeterRegistry prometheusRegistry;
    private Counter gegevensPgWriterCounter;

    @Autowired
    private BeschikkingsBevoegdheidRepo beschikkingsBevoegdheidRepo;

    @Autowired
    public GegevensPgWriter(final SessionFactory sfPostgres,
                            PrometheusMeterRegistry prometheusRegistry) {
        setSessionFactory(sfPostgres);
        this.prometheusRegistry = prometheusRegistry;
        this.gegevensPgWriterCounter = this.prometheusRegistry.counter("gegevenspgWriter", "aantal", "waarde");
    }

    @Transactional(transactionManager = "transactionManagerPg", propagation = Propagation.REQUIRED)
    @Override
    public void write(final List<? extends BeschikkingsBevoegdheid> items) {
        log.debug("Writing {} beschikkingsbevoegdheid", items.size());
        this.gegevensPgWriterCounter.increment(items.size());
        beschikkingsBevoegdheidRepo.saveAll(items);
    }
}
