package nl.bsoft.mybatch.config.postgres;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public @Data
class GegevensPgReader<BeschikkingsBevoegdheid> extends HibernateCursorItemReader<BeschikkingsBevoegdheid> {
    private final SessionFactory sessionFactory;
    private boolean autoCommit = true;
    private Counter gegevensPgReaderCounter;
    private PrometheusMeterRegistry prometheusRegistry;

    public GegevensPgReader(final SessionFactory sfPostgres,
                            final PrometheusMeterRegistry prometheusRegistry) {
        log.debug("Created GegevensPgReader");
        this.sessionFactory = sfPostgres;
        this.prometheusRegistry = prometheusRegistry;

        this.gegevensPgReaderCounter = Counter.builder("reader")
                //.baseUnit("gegevens")
                .tags("bron", "postgres")
                .description("Aantal gelezen records")
                .register(prometheusRegistry);

        setSessionFactory(sfPostgres);
        setQueryString("from BeschikkingsBevoegdheid");
        setUseStatelessSession(true);
    }

    @Override
    protected BeschikkingsBevoegdheid doRead() throws Exception {
        this.gegevensPgReaderCounter.increment();
        return super.doRead();
    }

    @Bean(name = "pgItemReader")
    public HibernateCursorItemReader<BeschikkingsBevoegdheid> pgItemReader() {
        log.debug("Created pgItemReader");

        return this;
    }

}
