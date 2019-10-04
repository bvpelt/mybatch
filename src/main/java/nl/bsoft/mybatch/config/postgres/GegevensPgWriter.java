package nl.bsoft.mybatch.config.postgres;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public
class GegevensPgWriter<BeschikkingsBevoegdheid> extends HibernateItemWriter<BeschikkingsBevoegdheid> {

    private SessionFactory sessionFactory = null;

    @Getter
    @Setter
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

        log.info("autoCommit set to: {}", autoCommit);

        if (autoCommit) {
            final Transaction tx = this.sessionFactory.getCurrentSession().beginTransaction();
            try {
                super.doWrite(this.sessionFactory, items);
                tx.commit();
            } catch (final Exception e) {
                tx.rollback();
                log.error("Error during writing, exception: {}", e);
                throw new RuntimeException(e);
            }
        } else {
            super.write(items);
        }
    }

}
