package nl.bsoft.mybatch.services;

import nl.bsoft.mybatch.config.postgres.repo.BeschikkingsBevoegdheidRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostgresServices {
    private static final Logger logger = LoggerFactory.getLogger(PostgresServices.class);

    @Autowired
    private BeschikkingsBevoegdheidRepo beschikkingsBevoegdheidRepo;


    public PostgresServices() {
    }

    public BeschikkingsBevoegdheidRepo getBeschikkingsBevoegdheidRepo() {
        return this.beschikkingsBevoegdheidRepo;
    }
}
