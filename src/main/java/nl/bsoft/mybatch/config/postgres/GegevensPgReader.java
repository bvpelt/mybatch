package nl.bsoft.mybatch.config.postgres;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GegevensPgReader<BeschikkingsBevoegdheid> extends HibernateCursorItemReader<BeschikkingsBevoegdheid> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensPgReader.class);

    private boolean autoCommit = true;

    public GegevensPgReader(@Qualifier("sfPostgres") final SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    public HibernateCursorItemReader<BeschikkingsBevoegdheid> pgItemReader() {
        this.setQueryString("from BeschikkingsBevoegdheidH2");
        this.setUseStatelessSession(true);

        return this;
    }

}
