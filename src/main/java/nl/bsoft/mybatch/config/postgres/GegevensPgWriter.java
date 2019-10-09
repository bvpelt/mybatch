package nl.bsoft.mybatch.config.postgres;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.postgres.repo.BeschikkingsBevoegdheidRepo;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Configuration
public
class GegevensPgWriter<S> extends HibernateItemWriter<S> {

    private SessionFactory sessionFactory = null;

    @Autowired
    private BeschikkingsBevoegdheidRepo beschikkingsBevoegdheidRepo;

    @Autowired
    public GegevensPgWriter(final SessionFactory sfPostgres) {
        setSessionFactory(sfPostgres);
        this.sessionFactory = sfPostgres;
    }

    @Transactional(transactionManager = "transactionManagerPg", propagation = Propagation.REQUIRED)
    @Override
    public void write(final List<? extends S> items) {
        for (S item : items) {
            BeschikkingsBevoegdheid i = (BeschikkingsBevoegdheid) item;
            beschikkingsBevoegdheidRepo.save(i);
        }
    }

}
