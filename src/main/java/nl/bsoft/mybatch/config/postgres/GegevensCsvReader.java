package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.csv.Gegeven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GegevensCsvReader implements ItemReader<Gegeven> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensCsvReader.class);
    private static final String WILL_BE_INJECTED = null;

    private FlatFileItemReader<Gegeven> itemReader;

    @Override
    public Gegeven read() throws Exception {
        logger.info("Read gegeven");
        Gegeven gegeven = itemReader.read();
        return gegeven;
    }


}
