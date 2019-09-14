package nl.bsoft.mybatch.config.postgres;

import nl.bsoft.mybatch.csv.Gegeven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

public class GegevensCsvReader implements ItemReader<Gegeven> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensCsvReader.class);

    private FlatFileItemReader<Gegeven> itemReader;

    public GegevensCsvReader() {
        itemReader = getItemReader();
    }

    @Override
    public Gegeven read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        logger.info("Read gegeven");

        Gegeven gegeven = itemReader.read();

        return gegeven;
    }

    public FlatFileItemReader<Gegeven> getItemReader() {
        FlatFileItemReader<Gegeven> itemReader = new FlatFileItemReader<Gegeven>();
        itemReader.setLinesToSkip(1);
//        itemReader.setResource(new FileSystemResource("src/main/resources/#{jobParameters['filename']}"));
        itemReader.setResource(new FileSystemResource("src/main/resources/Beschikkingsbevoegdheid.csv")); //DelimitedLineTokenizer defaults to comma as its delimiter
        DefaultLineMapper<Gegeven> lineMapper = new DefaultLineMapper<Gegeven>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        lineMapper.setFieldSetMapper(new GegevensCsvFieldSetMapper());
        itemReader.setLineMapper(lineMapper);
        itemReader.open(new ExecutionContext());

        return itemReader;
    }


}
