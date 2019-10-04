package nl.bsoft.mybatch.services;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.mybatch.config.postgres.repo.BeschikkingsBevoegdheidRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostgresServices {

    @Autowired
    private BeschikkingsBevoegdheidRepo beschikkingsBevoegdheidRepo;


    public PostgresServices() {
    }

    public BeschikkingsBevoegdheidRepo getBeschikkingsBevoegdheidRepo() {
        return this.beschikkingsBevoegdheidRepo;
    }
}
