package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GegevenCsv2PgConfig {
    private static final Logger logger = LoggerFactory.getLogger(GegevenCsv2PgConfig.class);

    @Bean(name = "gegevensReader")
    public ItemReader<Gegeven> gegevensReader() {
        ItemReader<Gegeven> gegevenItemReader = new GegevensCsvReader();
        return gegevenItemReader;
    }

    @Bean(name = "gegevensWriter")
    public ItemWriter<BeschikkingsBevoegdheid> getGegevensWriter(@Qualifier("sfPostgres") SessionFactory sessionFactory) {
        GegevensPgWriter gegevensPgWriter = new GegevensPgWriter(sessionFactory);

        return gegevensPgWriter;
    }

    @Bean(name = "gegevensProcessor")
    public ItemProcessor<Gegeven, BeschikkingsBevoegdheid> getGegevensProcessor() {

        return new GegevensProcessor();
    }
}
