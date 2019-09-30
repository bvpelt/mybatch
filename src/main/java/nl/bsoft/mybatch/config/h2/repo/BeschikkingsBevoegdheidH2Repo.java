package nl.bsoft.mybatch.config.h2.repo;

import nl.bsoft.mybatch.database.BeschikkingsBevoegdheidH2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeschikkingsBevoegdheidH2Repo extends CrudRepository<BeschikkingsBevoegdheidH2, Long> {

}
