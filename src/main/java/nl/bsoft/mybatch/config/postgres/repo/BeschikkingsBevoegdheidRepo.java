package nl.bsoft.mybatch.config.postgres.repo;

import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeschikkingsBevoegdheidRepo extends CrudRepository<BeschikkingsBevoegdheid, Long> {

}
