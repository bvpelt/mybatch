package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.csv.Gegeven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class GegevensCsvReader implements ItemReader<Gegeven> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensCsvReader.class);
    private static final String WILL_BE_INJECTED = null;

    private FlatFileItemReader<Gegeven> itemReader;

    @Override
    public Gegeven read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        logger.info("Read gegeven");
        Gegeven gegeven = itemReader.read();
        return gegeven;
    }




}
