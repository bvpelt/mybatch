package nl.bsoft.mybatch.config.h2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public @Data
class BeschikkingsBevoegdheidH2Writer<BeschikkingsBevoegdheidH2> extends HibernateItemWriter<BeschikkingsBevoegdheidH2> {

    private SessionFactory sessionFactory = null;
    private boolean autoCommit = true;

    @Autowired
    public BeschikkingsBevoegdheidH2Writer(final SessionFactory sfH2) {
        super.setSessionFactory(sfH2);
        this.sessionFactory = sfH2;
    }

    @Bean("beschikkingsBevoegheidH2Writer")
    public HibernateItemWriter<BeschikkingsBevoegdheidH2> getBeschikkingsBevoegheidH2Writer() {
        return this;
    }

    @Override
    public void write(final List<? extends BeschikkingsBevoegdheidH2> items) {

        if (this.sessionFactory == null) {
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
                logger.error("Error during writing, exception: {}", e);
                throw new RuntimeException(e);
            }
        } else {
            super.write(items);
        }
    }
}
