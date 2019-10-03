package nl.bsoft.mybatch.config.postgres;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GegevensPgWriter<BeschikkingsBevoegdheid> extends HibernateItemWriter<BeschikkingsBevoegdheid> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensPgWriter.class);

    private SessionFactory sessionFactory = null;
    private boolean autoCommit = true;

    @Autowired
    public GegevensPgWriter(final SessionFactory sfPostgres) {
        setSessionFactory(sfPostgres);
        this.sessionFactory = sfPostgres;
    }

    @Override
    public void write(final List<? extends BeschikkingsBevoegdheid> items) {

        if (sessionFactory == null) {
            throw new MappingException("De sessionFactory moet toegewezen zijn voordat geschreven kan worden!");
        }

        logger.info("autoCommit set to: {}", autoCommit);

        if (autoCommit) {
            final Transaction tx = this.sessionFactory.getCurrentSession().beginTransaction();
            try {
                super.doWrite(this.sessionFactory, items);
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

}
