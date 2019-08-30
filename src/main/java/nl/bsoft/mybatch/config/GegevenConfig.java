package nl.bsoft.mybatch.config;

import nl.bsoft.mybatch.csv.Gegeven;
import nl.bsoft.mybatch.database.BeschikkingsBevoegdheid;
import nl.bsoft.mybatch.repository.BeschikkingsBevoegdheidRepo;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class GegevenConfig {
    private static final Logger logger = LoggerFactory.getLogger(GegevenConfig.class);

    @Bean("gegevensReader")
    public ItemReader<Gegeven> gegevensReader() {
        ItemReader<Gegeven> gegevenItemReader = new GegevensReader();
        return gegevenItemReader;
    }

    @Bean("gegevensWriter")
    public ItemWriter<BeschikkingsBevoegdheid> getGegevensWriter(@Qualifier("sfPostgres") SessionFactory sessionFactory) {
        GegevensWriter gegevensWriter = new GegevensWriter(sessionFactory);

        return gegevensWriter;
    }

    @Bean("gegevensProcessor")
    public ItemProcessor<Gegeven, BeschikkingsBevoegdheid> getGegevensProcessor() {

        return new GegevensProcessor();
    }
}
