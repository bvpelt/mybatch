package nl.bsoft.mybatch.config.postgres;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GegevensPgReader<BeschikkingsBevoegdheid> extends HibernateCursorItemReader<BeschikkingsBevoegdheid> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensPgReader.class);
    private final SessionFactory sessionFactory;
    private boolean autoCommit = true;

    public GegevensPgReader(final SessionFactory sfPostgres) {
        logger.debug("Created GegevensPgReader");
        this.sessionFactory = sfPostgres;
        setSessionFactory(sfPostgres);
        setQueryString("from BeschikkingsBevoegdheid");
        setUseStatelessSession(true);
    }

    @Bean(name = "pgItemReader")
    public HibernateCursorItemReader<BeschikkingsBevoegdheid> pgItemReader() {
        logger.debug("Created pgItemReader");

        return this;
    }

}
