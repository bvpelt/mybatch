package nl.bsoft.mybatch.config.h2.repo;

import nl.bsoft.mybatch.database.VanPgNaarH2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VanPgNaarH2Repo extends CrudRepository<VanPgNaarH2, Long> {

}
