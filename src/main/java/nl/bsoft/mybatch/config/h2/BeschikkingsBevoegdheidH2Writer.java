package nl.bsoft.mybatch.config.h2;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.h2.repo.BeschikkingsBevoegdheidH2Repo;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheidH2;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Configuration
public @Data
class BeschikkingsBevoegdheidH2Writer extends HibernateItemWriter<BeschikkingsBevoegdheidH2> {

    @Autowired
    private BeschikkingsBevoegdheidH2Repo beschikkingsBevoegdheidH2Repo;

    private PrometheusMeterRegistry prometheusRegistry;
    private Counter writerH2Counter;

    @Autowired
    public BeschikkingsBevoegdheidH2Writer(final SessionFactory sessionFactoryH2,
                                           PrometheusMeterRegistry prometheusRegistry) {
        super.setSessionFactory(sessionFactoryH2);
        this.prometheusRegistry = prometheusRegistry;
        this.writerH2Counter = Counter.builder("writer")
                //.baseUnit("gegevens")
                .tags("bron", "h2")
                .description("Aantal verwerkte records")
                .register(prometheusRegistry);
    }

    @Transactional("transactionManagerH2")
    @Override
    public void write(final List<? extends BeschikkingsBevoegdheidH2> items) {
        log.debug("Saving {} items", items.size());
        beschikkingsBevoegdheidH2Repo.saveAll(items);
        this.writerH2Counter.increment(items.size());
    }
}
