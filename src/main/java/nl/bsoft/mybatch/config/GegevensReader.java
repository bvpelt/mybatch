package nl.bsoft.mybatch.config;

import nl.bsoft.mybatch.csv.Gegeven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

public class GegevensReader implements ItemReader<Gegeven> {
    private static final Logger logger = LoggerFactory.getLogger(GegevensReader.class);

//    @Autowired
//    private ResourceLoader resourceLoader;

    private FlatFileItemReader<Gegeven> itemReader;

    public GegevensReader() {
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
        itemReader.setResource(new FileSystemResource("src/main/resources/Beschikkingsbevoegdheid.csv")); //DelimitedLineTokenizer defaults to comma as its delimiter
        DefaultLineMapper<Gegeven> lineMapper = new DefaultLineMapper<Gegeven>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        lineMapper.setFieldSetMapper(new GegevensFieldSetMapper());
        itemReader.setLineMapper(lineMapper);
        itemReader.open(new ExecutionContext());

        return itemReader;
    }


}
