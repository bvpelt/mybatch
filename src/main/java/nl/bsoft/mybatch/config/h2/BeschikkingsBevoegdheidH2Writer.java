package nl.bsoft.mybatch.config.h2;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeschikkingsBevoegdheidH2Writer<BeschikkingsBevoegdheidH2> extends HibernateItemWriter<BeschikkingsBevoegdheidH2> {
    private static final Logger logger = LoggerFactory.getLogger(BeschikkingsBevoegdheidH2Writer.class);

    private SessionFactory sessionFactory = null;
    private boolean autoCommit = true;

    @Autowired
    public BeschikkingsBevoegdheidH2Writer(@Qualifier("sfH2") final SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Bean("beschikkingsBevoegheidH2Writer")
    public HibernateItemWriter<BeschikkingsBevoegdheidH2> getBeschikkingsBevoegheidH2Writer() {
        return this;
    }

    @Override
    public void write(final List<? extends BeschikkingsBevoegdheidH2> items) {

        if (sessionFactory == null) {
            throw new MappingException("De sessionFactory moet toegewezen zijn voordat geschreven kan worden!");
        }

        logger.info("autoCommit set to: {}", autoCommit);

        if (autoCommit) {
            final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
            try {
                super.doWrite(sessionFactory, items);
                tx.commit();
            } catch (final Exception e) {
                tx.rollback();
                logger.error("Error during writing, exception: {}", e);
                throw new RuntimeException(e);
            }
        } else {
            super.write(items);
        }
    }

    /*
    @Autowired
    @Override
    public void setSessionFactory(@Qualifier("sfPostgres") final SessionFactory sessionFactory) {
        if (this.sessionFactory == null) {
            logger.warn("No session factory defined, default sessionfactory used");
            this.sessionFactory = sessionFactory;
        }
        super.setSessionFactory(this.sessionFactory);
    }

     */
}
