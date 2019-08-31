package nl.bsoft.mybatch.config;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class GegevensWriter<BeschikkingsBevoegdheid> extends HibernateItemWriter<BeschikkingsBevoegdheid> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensWriter.class);

    private SessionFactory sessionFactory = null;
    private boolean autoCommit = true;

    @Autowired
    public GegevensWriter(@Qualifier("sfPostgres") final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void write(final List<? extends BeschikkingsBevoegdheid> items) {

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

    @Autowired
    @Override
    public void setSessionFactory(@Qualifier("sfPostgres") final SessionFactory sessionFactory) {
        if (this.sessionFactory == null) {
            logger.warn("No session factory defined, default sessionfactory used");
            this.sessionFactory = sessionFactory;
        }
        super.setSessionFactory(this.sessionFactory);
    }
}
