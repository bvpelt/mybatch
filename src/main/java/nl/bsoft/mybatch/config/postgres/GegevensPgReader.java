package nl.bsoft.mybatch.config.postgres;

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

    public GegevensPgReader(final SessionFactory sfPostgres) {
        log.debug("Created GegevensPgReader");
        this.sessionFactory = sfPostgres;
        setSessionFactory(sfPostgres);
        setQueryString("from BeschikkingsBevoegdheid");
        setUseStatelessSession(true);
    }

    @Bean(name = "pgItemReader")
    public HibernateCursorItemReader<BeschikkingsBevoegdheid> pgItemReader() {
        log.debug("Created pgItemReader");

        return this;
    }

}
